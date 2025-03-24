package com.github.indeednb.taskstatusquery.infrastructure.gatewayimpl;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.github.indeednb.taskstatusquery.domain.gateway.TaskGateway;
import com.github.indeednb.taskstatusquery.infrastructure.common.utils.SimpleDBUtil;
import com.github.indeednb.taskstatusquery.infrastructure.gatewayimpl.database.dataobject.TaskDO;

@Component
public class TaskGatewayImpl implements TaskGateway {
    private Map<Long, TaskDO> simpleDataBase = new LinkedHashMap<>();

    @Override
    public TaskDO get(Long id) {
        return simpleDataBase.get(id);
    }

    @Override
    public void save(TaskDO newTaskDO) {
        Long id = Optional
            .ofNullable(newTaskDO.getId())
            .orElse(SimpleDBUtil.getSelfIncreasingId());
        newTaskDO.setId(id);
        simpleDataBase.put(id, newTaskDO);
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
    public Collection<TaskDO> list() {
        return simpleDataBase.values();
    }

}
