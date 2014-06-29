package org.exoplatform.task.model;

public enum Priority {
    UNDEFINED(-1),

    BLOCKER(0),

    MAJOR(1),
    
    MINOR(2);

    private final int priority;

    Priority(int priority) {
        this.priority = priority;
    }

    public int priority() {
        return this.priority;
    }

    public static Priority getPriority(int priority) {
        for (Priority type : Priority.values()) {
            if (type.priority() == priority) {
                return type;
            }
        }

        return UNDEFINED;
    }

    public String getLabel() {
        switch (this.priority) {
            case 2:
                return "Minor";
            case 1:
                return "Major";
            case 0:
                return "Blocker";
            case -1:
            default:
                return "Undefined";
        }
    }
}
