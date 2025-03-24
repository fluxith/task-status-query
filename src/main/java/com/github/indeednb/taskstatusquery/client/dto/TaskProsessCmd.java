package com.github.indeednb.taskstatusquery.client.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import lombok.Data;

@Data
public class TaskProsessCmd {
    private String name;
    private String description;
    // waiting processing success fail
    @JsonSetter(nulls = Nulls.SKIP)
    private String status;
    // 默认值 waiting
    public void setStatus(String status){
        if(status == null || status.trim().isEmpty()){
            this.status = "waiting";
        }else{
            this.status = status;
        }
    }
}
