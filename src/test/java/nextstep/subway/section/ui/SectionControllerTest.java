package nextstep.subway.section.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.line.application.dto.LineResponse;
import nextstep.subway.section.application.SectionService;
import nextstep.subway.section.application.dto.SectionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static nextstep.subway.section.SectionTestSource.sectionRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SectionController.class)
class SectionControllerTest {

    @Autowired
    private MockMvc target;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SectionService sectionService;

    private final long lineId = 1L;

    @Test
    void 구간등록() throws Exception {
        final SectionRequest sectionRequest = sectionRequest();

        doReturn(new LineResponse())
                .when(sectionService)
                .addSection(anyLong(), any(SectionRequest.class));

        // when
        final ResultActions result = target.perform(
                MockMvcRequestBuilders.post("/lines/{id}/sections", lineId)
                        .content(objectMapper.writeValueAsString(sectionRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isCreated());
    }

    @Test
    void 구간삭제() throws Exception {
        // given
        final String stationId = "7";

        doNothing()
                .when(sectionService)
                .deleteSection(lineId, stationId);

        // when
        final ResultActions result = target.perform(
                MockMvcRequestBuilders.delete("/lines/{id}/sections", lineId)
                        .param("stationId", stationId)
        );

        // then
        result.andExpect(status().isNoContent());
    }

}