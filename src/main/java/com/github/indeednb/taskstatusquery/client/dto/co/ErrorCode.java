package com.github.indeednb.taskstatusquery.client.dto.co;

/**
 * ErrorCode
 *
 * @author Frank Zhang
 * @date 2019-01-04 11:00 AM
 */
public enum ErrorCode {

    B_TASK_alreadyExists("B_TASK_ALREADY_EXISTS", "任务已存在"),
    S_TASK_ID_IS_EMPTY("S_TASK_ID_IS_EMPTY", "任务id为空");

    private final String errCode;
    private final String errDesc;

    private ErrorCode(String errCode, String errDesc) {
        this.errCode = errCode;
        this.errDesc = errDesc;
    }

    public String getErrCode() {
        return errCode;
    }

    public String getErrDesc() {
        return errDesc;
    }

    public static ErrorCode statOf(String ecode) {
        for (ErrorCode errorCode : values()){
            if (errorCode.getErrCode().equals(ecode))
                return errorCode;
        }
        return null;
    }
}
