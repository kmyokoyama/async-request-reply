package io.kmyokoyama.asyncrequestreply.repository;

import io.kmyokoyama.asyncrequestreply.model.Work;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class WorkRepository {

    private final Map<UUID, Work> works = new HashMap<>();

    public Optional<Work> findById(final UUID id) {
        return Optional.ofNullable(works.get(id));
    }

    public void upsert(final Work work) {
        works.put(work.getId(), work);
    }
}
