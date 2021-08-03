package io.kmyokoyama.asyncrequestreply.service;

import io.kmyokoyama.asyncrequestreply.repository.WorkRepository;
import io.kmyokoyama.asyncrequestreply.model.Work;
import io.kmyokoyama.asyncrequestreply.model.WorkMeta;
import io.kmyokoyama.asyncrequestreply.model.WorkStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class WorkService {

    @Autowired
    WorkRepository workRepository;

    @Value("${meta.delay}")
    private long defaultDelay;

    private void updateWork(final UUID id, final WorkStatus status) {
        final Optional<Work> maybeWork = workRepository.findById(id);

        maybeWork.ifPresent((work) -> {
            if (work.getStatus() == WorkStatus.PENDING) {
                work.setStatus(status);

                workRepository.upsert(work);
            }
        });
    }
    private void completeWork(final UUID id) {
        updateWork(id, WorkStatus.SUCCEEDED);
    }

    private void failWork(final UUID id) {
        updateWork(id, WorkStatus.FAILED);
    }

    public Work process(final Work work, final WorkMeta meta) {
        workRepository.upsert(work);

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        final long delay = meta.getDelay() == null ? defaultDelay : meta.getDelay();
        final boolean shouldFail = meta.getShouldFail() != null && meta.getShouldFail();

        if (shouldFail) {
            executorService.schedule(() -> failWork(work.getId()), delay, TimeUnit.SECONDS);
        } else {
            executorService.schedule(() -> completeWork(work.getId()), delay, TimeUnit.SECONDS);
        }

        return work;
    }

    public Optional<Work> getById(final UUID id) {
        return workRepository.findById(id);
    }

    public Optional<Work> getCompletedById(final UUID id) {
        final Optional<Work> maybeWork = workRepository.findById(id);

        if (maybeWork.isPresent() && maybeWork.get().isCompleted()) {
            return maybeWork;
        }

        return Optional.empty();
    }
}
