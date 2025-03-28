package com.github.fluxith.taskstatusquery.domain.gateway;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.github.fluxith.taskstatusquery.client.dto.TaskUpdateCmd;
import com.github.fluxith.taskstatusquery.client.dto.co.TaskCO;

public interface TaskGateway {
    Boolean hasName(String name);
    TaskCO get(Long id);
    Collection<TaskCO> list();
    void save(TaskUpdateCmd taskUpdateCmd);
    void save(TaskUpdateCmd taskUpdateCmd, Boolean cover);
    void del(Long id);
    void changeProgress(Long taskId, Integer progressNum);
    Integer getProgress(Long taskId);
    Map<Long,Integer> getProgressMap(List<Long> taskIdList);
    void removeProgressCache(Long taskId);
}
