package com.github.fluxith.taskstatusquery.application.executor;

import org.springframework.stereotype.Component;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.Assert;
import com.github.fluxith.taskstatusquery.client.dto.TaskAddCmd;
import com.github.fluxith.taskstatusquery.client.dto.co.ErrorCode;
import com.github.fluxith.taskstatusquery.domain.gateway.TaskGateway;
import com.github.fluxith.taskstatusquery.infrastructure.convertor.TaskConvertor;

import jakarta.annotation.Resource;

@Component
public class TaskProsessCmdExe {
    @Resource
    private TaskGateway taskGateway;
    public Response execute(TaskAddCmd taskAddCmd) {
        Assert.isFalse(
            taskGateway.hasName( taskAddCmd.getName() ),
            ErrorCode.B_TASK_alreadyExists.getErrCode(),
            ErrorCode.B_TASK_alreadyExists.getErrDesc()
        );
        taskGateway.save(TaskConvertor.toUpdateCmd(taskAddCmd));
        return Response.buildSuccess();
    }
}
