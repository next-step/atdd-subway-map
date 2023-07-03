package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import subway.controller.dto.request.LineCreateRequest;
import subway.controller.dto.request.LineUpdateRequest;
import subway.controller.dto.response.LineResponse;
import subway.controller.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.fixture.LineFixture.*;

@DisplayName("지하철 노선 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     * */
    @DisplayName("지하철 노선을 생성한다")
    @Test
    void createLine() {
        // when
        LineCreateRequest request = new LineCreateRequest(
                "신분당선",
                "bg-red-600",
                지하철역_생성("강남역"),
                지하철역_생성("양재역"),
                10L
        );
        ExtractableResponse<Response> response = 지하철_노선_생성(request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<LineResponse> allLineResponse = 지하철_노선_전체_조회().jsonPath().getList("", LineResponse.class);
        assertThat(allLineResponse).hasSize(1);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다
     * */
    @DisplayName("지하철노선 목록 조회")
    @Test
    void getLines() {
        // given
        LineCreateRequest request1 = new LineCreateRequest(
                "신분당선",
                "bg-red-600",
                지하철역_생성("강남역"),
                지하철역_생성("양재역"),
                10L
        );
        LineCreateRequest request2 = new LineCreateRequest(
                "2호선",
                "bg-red-600",
                지하철역_생성("사당역"),
                지하철역_생성("방배역"),
                10L
        );
        지하철_노선_생성(request1);
        지하철_노선_생성(request2);

        // when
        List<LineResponse> response = 지하철_노선_전체_조회().jsonPath().getList("", LineResponse.class);

        // then
        assertThat(response).hasSize(2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다
     * */
    @DisplayName("지하철노선 조회")
    @Test
    void getLine() {
        // given
        LineCreateRequest request = new LineCreateRequest(
                "신분당선",
                "bg-red-600",
                지하철역_생성("강남역"),
                지하철역_생성("양재역"),
                10L
        );
        Long createLineId = 지하철_노선_생성(request).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회(createLineId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        LineResponse lineResponse = response.jsonPath().getObject("", LineResponse.class);
        assertAll(
                () -> assertThat(lineResponse.getName()).isEqualTo(request.getName()),
                () -> assertThat(lineResponse.getColor()).isEqualTo(request.getColor()),
                () -> assertThat(lineResponse.getStations().stream().map(StationResponse::getId).collect(Collectors.toList()))
                        .containsExactly(request.getUpStationId(), request.getDownStationId())
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     * */
    @DisplayName("지하철노선 수정")
    @Test
    void updateLine() {
        // given
        LineCreateRequest request = new LineCreateRequest(
                "신분당선",
                "bg-red-600",
                지하철역_생성("강남역"),
                지하철역_생성("양재역"),
                10L
        );
        Long createLineId = 지하철_노선_생성(request).jsonPath().getLong("id");

        // when
        LineUpdateRequest updateRequest = new LineUpdateRequest("다른분당선", "bg-black-600");
        ExtractableResponse<Response> response = 지하철_노선_수정(createLineId, updateRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        LineResponse updatedLine = 지하철_노선_조회(createLineId).jsonPath().getObject("", LineResponse.class);
        assertAll(
                () -> assertThat(updatedLine.getName()).isEqualTo(updateRequest.getName()),
                () -> assertThat(updatedLine.getColor()).isEqualTo(updateRequest.getColor())
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     * */
    @DisplayName("지하철노선 삭제")
    @Test
    void deleteLine() {
        // given
        LineCreateRequest request = new LineCreateRequest(
                "신분당선",
                "bg-red-600",
                지하철역_생성("강남역"),
                지하철역_생성("양재역"),
                10L
        );
        Long createLineId = 지하철_노선_생성(request).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 지하철_노선_삭제(createLineId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        List<Long> findLineIds = 지하철_노선_전체_조회().jsonPath().getList("id", Long.class);
        assertThat(findLineIds).doesNotContain(createLineId);
    }
}

