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
}
