package com.github.indeednb.taskstatusquery.client.dto;

import com.alibaba.cola.dto.Command;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class TaskAddCmd extends Command{
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
