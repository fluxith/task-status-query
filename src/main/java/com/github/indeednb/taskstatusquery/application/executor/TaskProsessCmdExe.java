package com.github.indeednb.taskstatusquery.application.executor;

import org.springframework.stereotype.Component;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.Assert;
import com.github.indeednb.taskstatusquery.client.dto.TaskProsessCmd;
import com.github.indeednb.taskstatusquery.client.dto.co.ErrorCode;
import com.github.indeednb.taskstatusquery.domain.gateway.TaskGateway;
import com.github.indeednb.taskstatusquery.infrastructure.convertor.TaskConvertor;

import jakarta.annotation.Resource;

@Component
public class TaskProsessCmdExe {
    @Resource
    private TaskGateway taskGateway;
    public Response execute(TaskProsessCmd taskProsessCmd) {
        Assert.isFalse(
            taskGateway.hasName( taskProsessCmd.getName() ),
            ErrorCode.B_TASK_alreadyExists.getErrCode(),
            ErrorCode.B_TASK_alreadyExists.getErrDesc()
        );
        taskGateway.save( TaskConvertor.toDataObject(taskProsessCmd) );
        return Response.buildSuccess();
    }
}
