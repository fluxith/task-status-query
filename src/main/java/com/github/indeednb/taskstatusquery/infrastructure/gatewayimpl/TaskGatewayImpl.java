package com.github.indeednb.taskstatusquery.infrastructure.gatewayimpl;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.springframework.stereotype.Component;

import com.github.indeednb.taskstatusquery.client.dto.TaskUpdateCmd;
import com.github.indeednb.taskstatusquery.client.dto.co.TaskCO;
import com.github.indeednb.taskstatusquery.domain.gateway.TaskGateway;
import com.github.indeednb.taskstatusquery.domain.taskprocess.TaskProcessStatus;
import com.github.indeednb.taskstatusquery.infrastructure.common.utils.RedisUtil;
import com.github.indeednb.taskstatusquery.infrastructure.common.utils.SimpleDBUtil;
import com.github.indeednb.taskstatusquery.infrastructure.convertor.SetterConvertor;
import com.github.indeednb.taskstatusquery.infrastructure.convertor.TaskConvertor;
import com.github.indeednb.taskstatusquery.infrastructure.event.TaskProcessEventPublisher;
import com.github.indeednb.taskstatusquery.infrastructure.gatewayimpl.database.dataobject.TaskDO;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TaskGatewayImpl implements TaskGateway {
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private TaskProcessEventPublisher taskProcessEventPublisher;
    private Map<Long, TaskDO> simpleDataBase = new LinkedHashMap<>();

    @Override
    public TaskCO get(Long id) {
        TaskCO taskCO = TaskConvertor.toClientObject( simpleDataBase.get(id) );
        taskCO.setProgress(this.getProgress(id));
        return taskCO;
    }

    @Override
    public void save(TaskUpdateCmd taskUpdateCmd, Boolean cover) {
        log.info("保存 {}", taskUpdateCmd);
        TaskDO newTaskDO = TaskConvertor.toDataObject(taskUpdateCmd);
        Consumer<TaskDO> operationAfterSavingTheDatabase = taskDO -> {};
        if(newTaskDO.getId() == null){
            // 创建任务时
            // 分配一个新的id
            newTaskDO.setId(SimpleDBUtil.getSelfIncreasingId());
            // 保存后再 提交任务处理消息
            operationAfterSavingTheDatabase = taskDO -> 
                taskProcessEventPublisher.publish(TaskConvertor.toTaskProcessEvent(newTaskDO));
        }else if(!Boolean.TRUE.equals(cover)){
            // 修改 且 非覆盖方式修改
            // 为兼容覆盖更新与添加功能的逻辑
            // 非覆盖更新的逻辑为: 将新数据的空置字段设置为对应的旧数据字段值，后走覆盖更新的逻辑
            TaskDO oldTaskDO = simpleDataBase.get(newTaskDO.getId());
            List<Function<TaskDO, Object>> taskDOGetters = List.of(
                TaskDO::getName,
                TaskDO::getDescription,
                TaskDO::getStatus
            );
            List<BiConsumer<TaskDO, Object>> taskDOSetters = List.of(
                SetterConvertor.conver(TaskDO::setName),
                SetterConvertor.conver(TaskDO::setDescription),
                SetterConvertor.conver(TaskDO::setStatus)
            );
            // 对于 null 的值，使用旧值覆盖
            for(int index = 0; index < taskDOGetters.size(); index++){
                Function<TaskDO, ?> getterFun = taskDOGetters.get(index);
                BiConsumer<TaskDO, Object> setterFun = taskDOSetters.get(index);
                if(getterFun.apply(newTaskDO) == null){
                    setterFun.accept(newTaskDO, getterFun.apply(oldTaskDO));
                } 
            }
        }
        simpleDataBase.put(newTaskDO.getId(), newTaskDO);
        operationAfterSavingTheDatabase.accept(newTaskDO);
    }

    @Override
    public void save(TaskUpdateCmd taskUpdateCmd) {
        this.save(taskUpdateCmd, false);
    }

    @Override
    public void del(Long id) {
        simpleDataBase.remove(id);
    }

    @Override
    public Boolean hasName(String name) {
        for (TaskDO taskDO : simpleDataBase.values()) {
            if(Objects.equals(taskDO.getName(), name)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<TaskCO> list() {
        List<TaskCO> taskCOList = simpleDataBase.values().stream()
            .map(TaskConvertor::toClientObject)
        .toList();
        Predicate<TaskCO> isProcessing = taskCO -> taskCO.getStatus().equals(TaskProcessStatus.PROCESSING.getCode());
        List<Long> processingTaskIdList = taskCOList.stream()
            .filter(isProcessing)
            .map(TaskCO::getId)
            .toList();
        Map<Long, Integer> idToProgressMap = this.getProgressMap(processingTaskIdList);
        taskCOList.forEach(taskCO ->
            taskCO.setProgress(idToProgressMap.get(taskCO.getId()))
        );
        return taskCOList;
    }

    public static final String TASK_PROCESS_KEY = "taskProcess:task:";
    @Override
    public void changeProgress(Long taskId, Integer progressNum){
        log.info("更改处理进度");
        log.info("changeProgress(Long taskId({}), Integer progressNum({}))", taskId, progressNum);
        // 过期时间 1 分钟
        redisUtil.setEx(
            TASK_PROCESS_KEY + taskId,
            progressNum.toString(),
            1,
            TimeUnit.MINUTES
        );
    }

    @Override
    public Integer getProgress(Long taskId){
        log.info("获取处理进度");
        log.info("getProgress(Long taskId({}))", taskId);
        return Optional.ofNullable(
            redisUtil.get(TASK_PROCESS_KEY + taskId)
        ).map(Integer::parseInt)
        .orElse(null);
    }

    @Override
    public Map<Long,Integer> getProgressMap(List<Long> taskIdList){
        log.info("同时获取多个处理进度");
        log.info("getProgressMap(List<Long> taskIdList({}))", taskIdList);
        if(taskIdList != null && !taskIdList.isEmpty()){
            List<String> progressStrList = redisUtil.multiGet(
                taskIdList.stream()
                .map(id -> TASK_PROCESS_KEY + id)
                .toList()
            );
            Map<Long, Integer> idToProgressMap = new HashMap<>();
            for(int index = 0; index < taskIdList.size(); index++){
                idToProgressMap.put(
                    taskIdList.get(index),
                    Integer.parseInt(progressStrList.get(index))
                );
            }
            return idToProgressMap;
        }
        return Map.of();
    }
    
    @Override
    public void removeProgressCache(Long taskId){
        log.info("删除进度缓存");
        log.info("removeProgressCache(Long taskId({}))", taskId);
        redisUtil.delete(TASK_PROCESS_KEY + taskId);
    }
}
