package com.github.indeednb.taskstatusquery.domain.taskprocess;

import java.util.Optional;

import com.alibaba.cola.domain.DomainFactory;
import com.alibaba.cola.domain.Entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.indeednb.taskstatusquery.client.dto.TaskUpdateCmd;
import com.github.indeednb.taskstatusquery.domain.gateway.TaskGateway;

import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Entity
@Data
@Slf4j
public class TaskProcess {
    private Long id;
    @JsonIgnore
    @Resource
    private TaskGateway taskGateway;
    public static TaskProcess valueOf(Long id){
        TaskProcess taskProcess = DomainFactory.create(TaskProcess.class);
        taskProcess.setId(id);
        log.info("TaskProcess.valueOf 生成的实例：{}", taskProcess);
        return taskProcess;
    }
    public void process() {
        log.info("开始处理任务：{}", this.id);
        // 检查任务是否存在
        if (taskGateway.get(this.id) == null) {
            log.warn("任务不存在：{}", this.id);
            return;
        }
        try {
            log.info("将状态设置为处理中：{}", this.id);
            // 将状态设置为处理中
            taskGateway.save(
                TaskUpdateCmd.builder()
                    .id(this.id)
                    .status(TaskProcessStatus.PROCESSING.getCode())
                .build()
            );
            // 模拟耗时操作
            Integer crrentProgress = Optional.ofNullable(
                taskGateway.getProgress(this.id)
            ).orElse(0);
            while (crrentProgress < 100) {
                Thread.sleep(1000);
                crrentProgress += 1;
                // 更新任务进度缓存
                log.info("更新任务进度缓存： {}", crrentProgress);
                taskGateway.changeProgress(this.id, crrentProgress);
            }
            log.info("将状态设置为成功 {}", this.id);
            // 将状态设置为成功
            taskGateway.save(
                TaskUpdateCmd.builder()
                    .id(this.id)
                    .status(TaskProcessStatus.SUCCESS.getCode())
                .build()
            );
        } catch (InterruptedException e) {
            log.warn("InterruptedException", e);
            // 将状态设置为失败
            taskGateway.save(
                TaskUpdateCmd.builder()
                    .id(this.id)
                    .status(TaskProcessStatus.FAIL.getCode())
                .build()
            );
            Thread.currentThread().interrupt();
        } finally {
            log.info("删除进度缓存 {}", this.id);
            // 删除进度缓存
            taskGateway.removeProgressCache(this.id);
        }
    }
}
