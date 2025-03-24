package com.github.indeednb.taskstatusquery.domain.gateway;

import java.util.Collection;

import com.github.indeednb.taskstatusquery.infrastructure.gatewayimpl.database.dataobject.TaskDO;

public interface TaskGateway {
    Boolean hasName(String name);
    TaskDO get(Long id);
    Collection<TaskDO> list();
    void save(TaskDO taskDO);
    void del(Long id);
}
