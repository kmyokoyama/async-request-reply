package io.kmyokoyama.asyncrequestreply.model;

import java.util.Optional;

public class WorkMeta {

    private Long delay;
    private Boolean shouldFail;

    private WorkMeta() {}

    public Optional<Long> getDelay() {
        return Optional.ofNullable(this.delay);
    }

    public Optional<Boolean> getShouldFail() {
        return Optional.ofNullable(this.shouldFail);
    }

    public static class Builder {

        Long delay;
        Boolean shouldFail;

        public Builder delay(final Long delay) {
            this.delay = delay;

            return this;
        }

        public Builder shouldFail(final Boolean shouldFail) {
            this.shouldFail = shouldFail;

            return this;
        }

        public WorkMeta build() {
            final WorkMeta meta = new WorkMeta();

            meta.delay = this.delay;
            meta.shouldFail = this.shouldFail;

            return meta;
        }
    }
}
