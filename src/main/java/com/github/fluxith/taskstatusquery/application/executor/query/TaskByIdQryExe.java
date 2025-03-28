package com.github.fluxith.taskstatusquery.application.executor.query;

import org.springframework.stereotype.Component;

import com.alibaba.cola.dto.SingleResponse;
import com.github.fluxith.taskstatusquery.client.dto.co.TaskCO;
import com.github.fluxith.taskstatusquery.domain.gateway.TaskGateway;

import jakarta.annotation.Resource;

@Component
public class TaskByIdQryExe {
    @Resource
    private TaskGateway taskGateway;
    public SingleResponse<TaskCO> execute(Long id) {
        return SingleResponse.of( taskGateway.get(id) );
    }
}
