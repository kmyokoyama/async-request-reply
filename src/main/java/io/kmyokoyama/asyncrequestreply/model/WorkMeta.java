package io.kmyokoyama.asyncrequestreply.model;

public class WorkMeta {

    private final Long delay;
    private final Boolean shouldFail;

    public WorkMeta(final Long delay, final Boolean shouldFail) {
        this.delay = delay;
        this.shouldFail = shouldFail;
    }

    public Long getDelay() {
        return this.delay;
    }

    public Boolean getShouldFail() {
        return this.shouldFail;
    }
}
