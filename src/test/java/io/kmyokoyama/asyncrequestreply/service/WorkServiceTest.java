package io.kmyokoyama.asyncrequestreply.service;

import io.kmyokoyama.asyncrequestreply.model.Work;
import io.kmyokoyama.asyncrequestreply.model.WorkMeta;
import io.kmyokoyama.asyncrequestreply.model.WorkStatus;
import io.kmyokoyama.asyncrequestreply.repository.WorkRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class WorkServiceTest {

    @Mock
    WorkRepository workRepository;

    @Mock
    WorkExecutor workExecutor;

    @InjectMocks
    WorkService workService;

    @Test
    public void testProcessNewWork() {
        final Work work = spy(new Work("some test message"));
        final UUID id = work.getId();
        final WorkMeta meta = new WorkMeta.Builder().build();

        doAnswer((invocation) -> {
            Runnable r = invocation.getArgument(0);

            r.run();

            return null;
        }).when(workExecutor).schedule(any(), anyLong(), any());

        when(workRepository.findById(id)).thenReturn(Optional.of(work));

        workService.process(work, meta);

        verify(workExecutor).schedule(any(), anyLong(), any());
        verify(workRepository).findById(id);
        verify(workRepository, times(2)).upsert(work);
        verify(work).setStatus(WorkStatus.SUCCEEDED);
    }

    @Test
    public void testProcessExistingWork() {
        final Work work = spy(new Work("some test message"));
        final UUID id = work.getId();
        final WorkMeta meta = new WorkMeta.Builder().build();

        doAnswer((invocation) -> {
            Runnable r = invocation.getArgument(0);

            r.run();

            return null;
        }).when(workExecutor).schedule(any(), anyLong(), any());

        when(workRepository.findById(id)).thenReturn(Optional.of(work));
        when(work.getStatus()).thenReturn(WorkStatus.SUCCEEDED);

        workService.process(work, meta);

        verify(workExecutor).schedule(any(), anyLong(), any());
        verify(workRepository).findById(id);
        verify(workRepository).upsert(work);
        verify(work, never()).setStatus(any());
    }

    @Test
    public void testGetById() {
        final Work work = new Work("some test message");
        final UUID id = UUID.randomUUID();

        when(workRepository.findById(id)).thenReturn(Optional.of(work));

        final Optional<Work> actual = workService.findById(id);

        verify(workRepository, atMostOnce()).findById(any(UUID.class));
        assertTrue(actual.isPresent());
        assertEquals(actual.get(), work);
    }

    @Test
    public void testFindPendingById() {
        final Work work = new Work("some test message");
        final UUID id = work.getId();

        when(workRepository.findById(id)).thenReturn(Optional.of(work));

        final Optional<Work> maybeWork = workService.findCompletedById(id);

        assertFalse(maybeWork.isPresent());
    }

    @Test
    public void testFindCompletedById() {
        final Work work = new Work("some test message");
        final UUID id = work.getId();
        work.setStatus(WorkStatus.SUCCEEDED);

        when(workRepository.findById(id)).thenReturn(Optional.of(work));

        final Optional<Work> maybeWork = workService.findCompletedById(id);

        assertTrue(maybeWork.isPresent());
    }
}
