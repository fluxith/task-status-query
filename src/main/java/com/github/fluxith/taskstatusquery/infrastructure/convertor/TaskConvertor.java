package com.github.fluxith.taskstatusquery.infrastructure.convertor;

import com.github.fluxith.taskstatusquery.client.dto.TaskAddCmd;
import com.github.fluxith.taskstatusquery.client.dto.TaskUpdateCmd;
import com.github.fluxith.taskstatusquery.client.dto.co.TaskCO;
import com.github.fluxith.taskstatusquery.client.dto.event.TaskProcessEvent;
import com.github.fluxith.taskstatusquery.infrastructure.gatewayimpl.database.dataobject.TaskDO;

public class TaskConvertor {
    private TaskConvertor() {
        throw new IllegalStateException("Utility class");
    }
    public static TaskDO toDataObject(TaskUpdateCmd taskProsessCmd) {
        if(taskProsessCmd == null){
            return null;
        }
        TaskDO taskDO = new TaskDO();
        taskDO.setId(taskProsessCmd.getId());
        taskDO.setName(taskProsessCmd.getName());
        taskDO.setDescription(taskProsessCmd.getDescription());
        taskDO.setStatus(taskProsessCmd.getStatus());
        return taskDO;
    }

    public static TaskUpdateCmd toUpdateCmd(TaskAddCmd taskProsessCmd) {
        if(taskProsessCmd == null){
            return null;
        }
        TaskUpdateCmd taskUpdateCmd = new TaskUpdateCmd();
        taskUpdateCmd.setName(taskProsessCmd.getName());
        taskUpdateCmd.setDescription(taskProsessCmd.getDescription());
        taskUpdateCmd.setStatus(taskProsessCmd.getStatus());
        return taskUpdateCmd;
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

    public static TaskProcessEvent toTaskProcessEvent(TaskDO taskDO){
        if(taskDO.getId() == null){
            return null;
        }
        return new TaskProcessEvent(taskDO.getId());
    }
}
