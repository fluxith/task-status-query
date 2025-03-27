package com.github.indeednb.taskstatusquery.adapter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import com.github.indeednb.taskstatusquery.application.executor.TaskProsessCmdExe;
import com.github.indeednb.taskstatusquery.application.executor.query.TaskByIdQryExe;
import com.github.indeednb.taskstatusquery.application.executor.query.TaskListQryExe;
import com.github.indeednb.taskstatusquery.client.dto.TaskAddCmd;
import com.github.indeednb.taskstatusquery.client.dto.co.TaskCO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;

@RestController
@Tag(name = "任务")
@RequestMapping("/task")
public class TaskController {
    @Resource
    private TaskByIdQryExe taskByIdQryExe;
    @Resource
    private TaskListQryExe taskListQryExe;
    @Resource
    private TaskProsessCmdExe taskProsessCmdExe;

    
    @Operation(summary = "查询任务详情")
    @GetMapping("/{id}")
    public SingleResponse<TaskCO> get(@PathVariable Long id) {
        return taskByIdQryExe.execute(id);
    }

    @Operation(summary = "查询任务列表")
    @GetMapping("/list")
    public MultiResponse<TaskCO> list() {
        return taskListQryExe.execute();
    }

    @Operation(summary = "提交任务处理请求")
    @PostMapping("/process")
    public Response process(@RequestBody TaskAddCmd taskProsessCmd){
        return taskProsessCmdExe.execute(taskProsessCmd);
    }
}
