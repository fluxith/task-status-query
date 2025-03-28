package com.github.fluxith.taskstatusquery.client.dto;

import com.alibaba.cola.dto.Command;

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
public class TaskUpdateCmd extends Command{
    private Long id;
    private String name;
    private String description;
    private String status;
}
