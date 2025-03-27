package com.github.indeednb.taskstatusquery.domain.taskprocess;

public enum TaskProcessStatus {
    WAITING("watting"),
    PROCESSING("processing"),
    SUCCESS("success"),
    FAIL("fail");

    private String code;
    TaskProcessStatus(String code){
        this.code = code;
    }
    public String getCode(){
        return code;
    }
    
    public static TaskProcessStatus of(String statusCode){
        if(statusCode == null){
            return null;
        }
        for (TaskProcessStatus taskProcessStatus : TaskProcessStatus.values()) {
            if(statusCode.equals(taskProcessStatus.code)){
                return taskProcessStatus;
            }
        }
        return null;
    }
}
