package org.exoplatform.task.model;

public enum Status {
    OPEN(1),

    INPROGRESS(2),

    RESOLVED(3),
    
    REFUSED(4);

    private final int status;

    Status(int status) {
        this.status = status;
    }

    public int status() {
        return this.status;
    }

    public String getLabel() {
        switch (this.status) {
            case 4:
                return "Refused";
            case 3:
                return "Resolved";
            case 2:
                return "In Progress";
            case 1:
                return "Open";
            default:
                return "No name";
        }
    }

    public static Status getStatus(int status) {
        for (Status type : Status.values()) {
            if (type.status() == status) {
                return type;
            }
        }

        return OPEN;
    }
}
