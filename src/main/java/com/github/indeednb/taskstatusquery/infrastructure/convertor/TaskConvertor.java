package com.github.indeednb.taskstatusquery.infrastructure.convertor;

import com.github.indeednb.taskstatusquery.client.dto.TaskProsessCmd;
import com.github.indeednb.taskstatusquery.client.dto.co.TaskCO;
import com.github.indeednb.taskstatusquery.infrastructure.gatewayimpl.database.dataobject.TaskDO;

public class TaskConvertor {
    private TaskConvertor() {
        throw new IllegalStateException("Utility class");
    }
    public static TaskDO toDataObject(TaskProsessCmd taskProsessCmd) {
        if(taskProsessCmd == null){
            return null;
        }
        TaskDO taskDO = new TaskDO();
        taskDO.setName(taskProsessCmd.getName());
        taskDO.setDescription(taskProsessCmd.getDescription());
        taskDO.setStatus(taskProsessCmd.getStatus());
        return taskDO;
    }
    
    public static TaskCO toClientObject(TaskDO taskDO) {
        if(taskDO == null){
            return null;
        }
        TaskCO taskCO = new TaskCO();
        taskCO.setId(taskDO.getId());
        taskCO.setName(taskDO.getName());
        taskCO.setDescription(taskDO.getDescription());
        taskCO.setStatus(taskDO.getStatus());
        return taskCO;
    }
}
