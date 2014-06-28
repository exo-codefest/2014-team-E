package org.exoplatform.task;

public class TaskServiceException extends RuntimeException {
    private static final long serialVersionUID = -6739016122540813895L;

    private int code;
    
    public static final int DUPLICATED = 1;
    
    public static final int NON_EXITS_PROJECT = 2;
    
    public static final int NON_EXITS_TASK = 3;
    
    public static final int NON_EXITS_OWNER = 4;
    
    public static final int NON_EXITS_COMMENT = 5;    
    
    public TaskServiceException(String message, Throwable throwable) {
        this(0, message, throwable);
    }
    
    public TaskServiceException(int code, String message) {
        this(code, message, null);
    }
    
    public TaskServiceException(int code, String message, Throwable throwable) {
        super(message, throwable);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
