package io.kmyokoyama.asyncrequestreply.model;

public class WorkRequest {

    private String message;
    private Long delay;
    private Boolean shouldFail;

    public WorkRequest() {}

    public String getMessage() {
        return this.message;
    }

    public Long getDelay() {
        return this.delay;
    }

    public Boolean getShouldFail() {
        return this.shouldFail;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public void setDelay(final Long delay) {
        this.delay = delay;
    }

    public void setShouldFail(final Boolean shouldFail) {
        this.shouldFail = shouldFail;
    }
}
