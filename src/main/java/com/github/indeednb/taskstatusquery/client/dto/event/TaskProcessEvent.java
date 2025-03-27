package com.github.indeednb.taskstatusquery.client.dto.event;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskProcessEvent implements Serializable {
    private Long taskId;
}
