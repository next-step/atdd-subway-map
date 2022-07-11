package nextstep.subway.line.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.application.dto.LineRequest;
import nextstep.subway.line.application.dto.LineResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LineController.class)
class LineControllerTest {

    @Autowired
    private MockMvc target;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LineService lineService;

    @Test
    void 노선생성() throws Exception {
        final LineRequest lineRequest = lineRequest();

        doReturn(new LineResponse())
                .when(lineService)
                .createLine(any(LineRequest.class));

        // when
        final ResultActions result = target.perform(
                MockMvcRequestBuilders.post("/lines")
                        .content(objectMapper.writeValueAsString(lineRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isCreated());
    }

    @Test
    void 노선조회() throws Exception {
        final Long id = 1L;
        doReturn(new LineResponse())
                .when(lineService)
                .findLine(id);

        // when
        final ResultActions result = target.perform(
                MockMvcRequestBuilders.get("/lines/{id}", id)
        );

        // then
        result.andExpect(status().isOk());
    }

    @Test
    void 노선목록조회() throws Exception {
        doReturn(List.of(new LineResponse()))
                .when(lineService)
                .findAllLines();

        // when
        final ResultActions result = target.perform(
                MockMvcRequestBuilders.get("/lines")
        );

        // then
        result.andExpect(status().isOk());
    }

    @Test
    void 노선수정() throws Exception {
        final Long id = 1L;
        final LineRequest lineRequest = lineRequest();

        doReturn(new LineResponse())
                .when(lineService)
                .createLine(any(LineRequest.class));

        // when
        final ResultActions result = target.perform(
                MockMvcRequestBuilders.put("/lines/{id}", id)
                        .content(objectMapper.writeValueAsString(lineRequest))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk());
    }

    @Test
    void 노선삭제() throws Exception {
        final Long id = 1L;

        // when
        final ResultActions result = target.perform(
                MockMvcRequestBuilders.delete("/lines/{id}", id)
        );

        // then
        result.andExpect(status().isNoContent());
    }

    private LineRequest lineRequest() {
        return LineRequest.builder()
                .name("lineName")
                .color("bg-red-600")
                .upStationId(1L)
                .downStationId(2L)
                .distance(10L)
                .build();
    }
}