package io.kmyokoyama.asyncrequestreply.controller;

import io.kmyokoyama.asyncrequestreply.model.Work;
import io.kmyokoyama.asyncrequestreply.model.WorkMeta;
import io.kmyokoyama.asyncrequestreply.model.WorkRequest;
import io.kmyokoyama.asyncrequestreply.model.WorkResponse;
import io.kmyokoyama.asyncrequestreply.service.WorkService;
import io.kmyokoyama.asyncrequestreply.model.WorkStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
public class WorkController {

    @Autowired
    private WorkService workService;

    private static final long RETRY_AFTER_SECONDS = 120L;

    @PostMapping(
            path = "/work",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkResponse> accept(final @RequestBody WorkRequest req) {
        final WorkMeta meta = new WorkMeta(req.getDelay(), req.getShouldFail());
        final Work work = workService.process(new Work(req.getMessage()), meta);

        return new ResponseEntity<>(new WorkResponse(work), HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/work/{id}/status")
    public ResponseEntity<?> status(final @PathVariable String id) {
        Optional<Work> maybeWork = workService.getById(UUID.fromString(id));

        if (maybeWork.isPresent()) {
            final Work work = maybeWork.get();

            final HttpHeaders headers = new HttpHeaders();

            if (work.getStatus() == WorkStatus.PENDING) {
                headers.set(HttpHeaders.RETRY_AFTER, String.valueOf(RETRY_AFTER_SECONDS));

                return ResponseEntity.accepted().headers(headers).build();
            }

            headers.set(HttpHeaders.LOCATION, "/work/" + work.getId());

            return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/work/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkResponse> fetch(final @PathVariable String id) {
        Optional<Work> maybeWork = workService.getCompletedById(UUID.fromString(id));

        if (maybeWork.isPresent()) {
            final Work work = maybeWork.get();

            return new ResponseEntity<>(new WorkResponse(work), HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
