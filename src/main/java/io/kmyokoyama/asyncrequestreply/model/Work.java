package io.kmyokoyama.asyncrequestreply.model;

import java.util.UUID;

public class Work {

    private final UUID id;
    private final String message;
    private WorkStatus status;

    public Work(final String message) {
        this.id = UUID.randomUUID();
        this.message = message;
        this.status = WorkStatus.PENDING;
    }

    public UUID getId() {
        return this.id;
    }

    public String getMessage() {
        return this.message;
    }

    public WorkStatus getStatus() {
        return this.status;
    }

    public boolean isCompleted() {
        return this.status == WorkStatus.SUCCEEDED || this.status == WorkStatus.FAILED;
    }

    public void setStatus(final WorkStatus status) {
        this.status = status;
    }
}
