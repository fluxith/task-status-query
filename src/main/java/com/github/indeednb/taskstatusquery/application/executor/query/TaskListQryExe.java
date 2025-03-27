package com.github.indeednb.taskstatusquery.application.executor.query;

import org.springframework.stereotype.Component;

import com.alibaba.cola.dto.MultiResponse;
import com.github.indeednb.taskstatusquery.client.dto.co.TaskCO;
import com.github.indeednb.taskstatusquery.domain.gateway.TaskGateway;

import jakarta.annotation.Resource;

@Component
public class TaskListQryExe {
    @Resource
    private TaskGateway taskGateway;

    public MultiResponse<TaskCO> execute() {
        return MultiResponse.of(taskGateway.list());
    }
}
