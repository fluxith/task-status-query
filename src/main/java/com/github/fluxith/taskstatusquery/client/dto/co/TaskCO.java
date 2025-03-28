package com.github.fluxith.taskstatusquery.client.dto.co;

import lombok.Data;

@Data
public class TaskCO {
    private Long id;
    private String name;
    private String description;
    private String status;
    private Integer progress;
}
