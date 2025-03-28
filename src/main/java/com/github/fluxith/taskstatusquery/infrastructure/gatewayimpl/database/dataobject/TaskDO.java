package com.github.fluxith.taskstatusquery.infrastructure.gatewayimpl.database.dataobject;

@lombok.Data
public class TaskDO {
  private Long id;
  private String name;
  private String description;
  private String status;
}
