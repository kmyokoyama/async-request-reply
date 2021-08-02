package io.kmyokoyama.asyncrequestreply.model;

import java.util.UUID;

public class WorkResponse {

    private final UUID id;
    private final String message;
    private final String status;

    public WorkResponse(final Work work) {
        this.id = work.getId();
        this.message = work.getMessage();
        this.status = work.getStatus().name().toLowerCase();
    }

    public UUID getId() {
        return this.id;
    }

    public String getMessage() {
        return this.message;
    }

    public String getStatus() {
        return this.status;
    }
}
