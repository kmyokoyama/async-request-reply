package io.kmyokoyama.asyncrequestreply.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kmyokoyama.asyncrequestreply.ConfigProperties;
import io.kmyokoyama.asyncrequestreply.model.Work;
import io.kmyokoyama.asyncrequestreply.model.WorkRequest;
import io.kmyokoyama.asyncrequestreply.model.WorkStatus;
import io.kmyokoyama.asyncrequestreply.service.WorkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
//@EnableConfigurationProperties(ConfigProperties.class)
//@TestPropertySource(properties = { "meta.retryAfter=120" })
@SpringBootTest
public class WorkControllerTest {

    @Mock
    private WorkService workService;

    @InjectMocks
    private WorkController workController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.standaloneSetup(workController).build();
    }

    @Test
    public void testCreateNewWork() throws Exception {
        final WorkRequest request = new WorkRequest();

        request.setMessage("some test message");

        when(workService.process(any(Work.class), any())).thenReturn(new Work("some test message"));

        mockMvc.perform(post("/work")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.message").value("some test message"))
                .andExpect(jsonPath("$.status").value("pending"));
    }

    @Test
    public void testGetPendingWorkStatus() throws Exception {
        final UUID id = UUID.randomUUID();

        ReflectionTestUtils.setField(workController, "retryAfter", 120L);

        when(workService.findById(id)).thenReturn(Optional.of(new Work("some test message")));

        mockMvc.perform(get("/work/" + id.toString() + "/status"))
                .andExpect(status().isAccepted())
                .andExpect(header().string(HttpHeaders.RETRY_AFTER, "120"));
    }

    @Test
    public void testGetCompletedWorkStatus() throws Exception {
        final UUID id = UUID.randomUUID();
        final Work completedWork = new Work("some test message");

        completedWork.setStatus(WorkStatus.SUCCEEDED);

        when(workService.findById(id)).thenReturn(Optional.of(completedWork));

        mockMvc.perform(get("/work/" + id + "/status"))
                .andExpect(status().isFound())
                .andExpect(header().exists(HttpHeaders.LOCATION));
    }

    @Test
    public void testGetMissingWorkStatus() throws Exception {
        final UUID id = UUID.randomUUID();

        when(workService.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/work/" + id + "/status"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetCompletedWork() throws Exception {
        final UUID id = UUID.randomUUID();
        final Work completedWork = new Work("some test message");

        completedWork.setStatus(WorkStatus.SUCCEEDED);

        when(workService.findCompletedById(id)).thenReturn(Optional.of(completedWork));

        mockMvc.perform(get("/work/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetMissingWork() throws Exception {
        final UUID id = UUID.randomUUID();

        when(workService.findCompletedById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/work/" + id + "/status"))
                .andExpect(status().isNotFound());
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
