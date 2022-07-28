package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.line.LineRequest;
import nextstep.subway.applicaion.dto.section.SectionRequest;
import nextstep.subway.error.exception.LineNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("지하철노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철노선을 생성하면
     * Then 지하철노선이 생성된다
     * Then 지하철노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {
        // when
        createStations();
        LineRequest dto = new LineRequest("2호선", "bg-red-600", 1L, 3L, 10L);
        ExtractableResponse<Response> response = createLines(dto);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        assertThat(getList("name", "/lines").get(0)).isEqualTo("2호선");
        assertThat(getLine("distance", "1")).isEqualTo("10");
    }

    /**
     *       [신분당선]
     * [2호선]  강남 - 역삼 - 선릉
     *           ｜
     *          양재
     */
    private void createStations() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");
        create(params, "/stations");
        params.put("name", "역삼역");
        create(params, "/stations");
        params.put("name", "선릉역");
        create(params, "/stations");
        params.put("name", "양재역");
        create(params, "/stations");
    }

    private ExtractableResponse<Response> createLines(LineRequest dto) {
        ExtractableResponse<Response> response = create(dto, "/lines");
        return response;
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 2개의 지하철 노선을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        createStations();
        LineRequest dto = new LineRequest("2호선", "bg-red-600", 1L, 3L, 10L);
        createLines(dto);
        dto = new LineRequest("신분당선", "bg-green-300", 1L, 4L, 3L);
        createLines(dto);

        // then
        assertThat(getList("name", "/lines")).containsExactly("2호선", "신분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선을 응답 받는다
     */
    // TODO: 지하철 노선 조회 인수 테스트 메서드 생성
    @DisplayName("지하철노선을 조회한다.")
    @Test
    void getLine() {
        // given
        createStations();
        LineRequest dto = new LineRequest("2호선", "bg-red-600", 1L, 3L, 10L);
        createLines(dto);

        // then
        assertThat(getLine("name", "1")).isEqualTo("2호선");

        // given
        dto = new LineRequest("신분당선", "bg-green-300", 1L, 4L, 3L);
        createLines(dto);

        // then
        assertAll(() -> {
            assertThat(getLine("name", "1")).isEqualTo("2호선");
            assertThat(getLine("name", "2")).isEqualTo("신분당선");
        });
    }

    private String getLine(String variable, String id) {
        return RestAssured.given().log().all()
                .when().get("/lines/" + id)
                .then().log().all()
                .extract().jsonPath().getString(variable);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 수정된 지하철 노선을 응답 받는다
     */
    // TODO: 지하철 노선 수정 인수 테스트 메서드 생성
    @DisplayName("지하철노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        createStations();
        LineRequest dto = new LineRequest("2호선", "bg-red-600", 1L, 3L, 10L);
        createLines(dto);

        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "2호선-임시");
        params.put("color", "bg-blue-600");
        updateLine(params, "1");

        // then
        assertThat(getLine("name", "1")).isEqualTo("2호선-임시");
    }

    private void updateLine(Map params, String id) {
        update(params, "/lines/", id);
    }

    /**
     * Given 지하철노선을 생성하고
     * When 그 지하철노선을 삭제하면
     * Then 지하철노선 목록 조회 시 삭제한 노선을 찾을 수 없다
     */
    // TODO: 지하철노선 제거 인수 테스트 메서드 생성
    @DisplayName("지하철노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        createStations();
        LineRequest dto = new LineRequest("2호선", "bg-red-600", 1L, 3L, 10L);
        ExtractableResponse<Response> response = createLines(dto);
        String createdId = response.jsonPath().getString("id");
        assertThat(getLine("name", createdId)).isEqualTo("2호선");

        // when
        deleteLine(createdId);

        // then
        assertThat(getList("name", "/lines")).hasSize(0);
    }

    private void deleteLine(String id) {
        delete("/lines/", id);
    }

    /**
     * Given 추가할 구간을 생성하고
     * When 지하철노선에 구간을 추가하면
     * Then 지하철노선 마지막에 구간에 등록된다
     */
    // TODO: 구간 등록 인수 테스트 메서드 생성
    @DisplayName("구간을 등록한다.")
    @Test
    void addSection() {
        // given
        createStations();
        LineRequest dto = new LineRequest("2호선", "bg-red-600", 1L, 2L, 10L);
        ExtractableResponse<Response> response = createLines(dto);
        assertThat(getLine("downStation.id", "1")).isEqualTo("2");
        assertThat(getLine("distance", "1")).isEqualTo("10");
//        System.out.println("=======");
//        System.out.println(response.jsonPath().getString("."));
//        System.out.println(response.jsonPath().getString("downStation.id"));
//        System.out.println("=======");

        // when
        Long createdLineId = Long.parseLong(response.jsonPath().getString("id"));
        Long upStationId = Long.parseLong(response.jsonPath().getString("downStation.id"));
        Long downStationId = 3L;
        Long distance = 5L;
        SectionRequest sectionRequest = new SectionRequest(upStationId, downStationId, distance);
        ExtractableResponse<Response> response2 = addSection(createdLineId, sectionRequest);
//        System.out.println("-------");
//        System.out.println(response2.jsonPath().getString("."));
//        System.out.println(response2.jsonPath().getString("downStation.id"));
//        System.out.println("-------");

        // then
        assertThat(getLine("downStation.id", "1")).isEqualTo("3");
        assertThat(getLine("distance", "1")).isEqualTo("15");
    }

    private ExtractableResponse<Response> addSection(Long id, SectionRequest dto) {
        return RestAssured.given().log().all()
                .body(dto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + id + "/sections")
                .then().log().all()
                .extract();
    }

    /**
     * Given 추가할 구간에 해당하는 역을 생성하고(할 필요가 없다는 게 로직의 구멍...)
     * When 존재하지 않는 지하철노선에 구간을 추가하면
     * Then 예외가 발생된다
     */
    // TODO: 존재하지 않는 노선에 구간 등록 시 예외 발생
    @DisplayName("존재하지 않는 노선에 구간을 등록한다.")
    @Test
    void addSectionToNonPresentLine() {
//        assertThatThrownBy(() -> {
//            SectionRequest sectionRequest = new SectionRequest(1L, 2L, 3L);
//            addSection(2L, sectionRequest);
//        }).isInstanceOf(LineNotFoundException.class);

        SectionRequest sectionRequest = new SectionRequest(1L, 2L, 3L);
        ExtractableResponse<Response> response = addSection(2L, sectionRequest);

        // then
        assertThat(response.jsonPath().getString("errorCode")).isEqualTo("NOT_LINE_FOUND");
    }

    /**
     * Given 추가할 구간에 해당하는 역과 노선을 생성하고
     * When 지하철노선의 하행 종점역이 추가할 구간의 상행역과 다르다면
     * Then 예외가 발생된다
     */
    // TODO: 구간 등록 시 지하철노선의 하행 종점역이 구간의 상행역과 다를 경우 예외 발생
    @DisplayName("지하철노선의 하행 종점역이 추가할 구간의 상행역과 다를 경우 예외가 발생된다.")
    @Test
    void addSectionIfUpboundStationOfSectionIsDifferentFromDownboundStationOfLine() {
        // given
        createStations();
        LineRequest dto = new LineRequest("2호선", "bg-red-600", 1L, 2L, 10L);
        ExtractableResponse<Response> response = createLines(dto);
        assertThat(getLine("downStation.id", "1")).isEqualTo("2");

        // when
        Long createdLineId = Long.parseLong(response.jsonPath().getString("id"));
        Long upStationId = 3L;
        Long downStationId = 4L;
        Long distance = 5L;
        SectionRequest sectionRequest = new SectionRequest(upStationId, downStationId, distance);
        ExtractableResponse<Response> response2 = addSection(createdLineId, sectionRequest);

        // then
        assertThat(response2.jsonPath().getString("errorCode")).isEqualTo("MISMATCH_BETWEEN_UPPER_STATION_OF_SECTION_AND_LOWER_STATION_OF_LINE");
    }

    /**
     * Given 추가할 구간에 해당하는 역과 노선을 생성하고
     * When 지하철노선에 존재하는 역이 추가할 구간의 하행역과 일치한다면
     * Then 예외가 발생된다
     */
    // TODO: 구간 등록 시 지하철노선에 존재하는 역이 구간의 하행역과 같을 경우 예외 발생
    @DisplayName("지하철노선에 존재하는 역이 구간의 하행역과 같을 경우 예외가 발생된다.")
    @Test
    void addSectionIfDownboundStationOfSectionIsIncludedInStationOfLine() {
        // given
        createStations();
        LineRequest dto = new LineRequest("2호선", "bg-red-600", 1L, 3L, 10L);
        ExtractableResponse<Response> response = createLines(dto);
        assertThat(getLine("downStation.id", "1")).isEqualTo("3");

        // when
        Long createdLineId = Long.parseLong(response.jsonPath().getString("id"));
        Long upStationId = Long.parseLong(response.jsonPath().getString("downStation.id"));
        Long downStationId = 1L;
        Long distance = 5L;
        SectionRequest sectionRequest = new SectionRequest(upStationId, downStationId, distance);
        ExtractableResponse<Response> response2 = addSection(createdLineId, sectionRequest);

        // then
        assertThat(response2.jsonPath().getString("errorCode")).isEqualTo("FAIL_TO_ADD_DUPLICATE_STATION");
    }

    /**
     * Given 지하철노선에 두 개 이상의 구간이 존재할 때
     * When 지하철노선에 구간을 삭제하면
     * Then 지하철노선 마지막 구간이 삭제된다
     */
    // TODO: 구간 삭제 인수 테스트 메서드 생성
    @DisplayName("구간을 삭제한다.")
    @Test
    void deleteSection() {
        // given
        createStations();
        LineRequest dto = new LineRequest("2호선", "bg-red-600", 1L, 2L, 10L);
        ExtractableResponse<Response> response = createLines(dto);
        assertThat(getLine("downStation.id", "1")).isEqualTo("2");

        Long createdLineId = Long.parseLong(response.jsonPath().getString("id"));
        Long upStationId = Long.parseLong(response.jsonPath().getString("downStation.id"));
        Long downStationId = 4L;
        Long distance = 5L;
        SectionRequest sectionRequest = new SectionRequest(upStationId, downStationId, distance);
        ExtractableResponse<Response> response2 = addSection(createdLineId, sectionRequest);
        assertThat(getLine("downStation.id", "1")).isEqualTo("4");
        assertThat(getLine("distance", "1")).isEqualTo("15");

        // when
        deleteSection(1L, 4L);

        // then
        assertThat(getLine("downStation.id", "1")).isEqualTo("2");
        assertThat(getLine("distance", "1")).isEqualTo("10");
    }

    private ExtractableResponse<Response> deleteSection(Long lineId, Long stationId) {
        return RestAssured
                .given().log().all()
                        .param("stationId", stationId)
                .when().delete("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }

    /**
     * Given 삭제할 구간에 해당하는 역을 생성하고(할 필요가 없다는 게 로직의 구멍...)
     * When 존재하지 않는 지하철노선의 구간을 삭제하면
     * Then 예외가 발생된다
     */
    // TODO: 존재하지 않는 노선에  구간 삭제 시 예외 발생
    @DisplayName("존재하지 않는 노선의 구간을 삭제한다.")
    @Test
    void deleteSectionToNonPresentLine() {
        // when
        ExtractableResponse<Response> response = deleteSection(2L, 3L);

        // then
        assertThat(response.jsonPath().getString("errorCode")).isEqualTo("NOT_LINE_FOUND");
    }

    /**
     * Given 삭제할 구간에 해당하는 역과 노선을 생성하고
     * When 지하철노선의 구간이 유일한 경우 삭제 시
     * Then 예외가 발생된다
     */
    // TODO: 구간 삭제 시 지하철노선의 구간이 유일한 경우 예외 발생
    @DisplayName("구간 삭제 시 지하철노선의 구간이 유일한 경우 예외가 발생된다.")
    @Test
    void deleteSectionIfLengthOfLineIsOne() {
        // given
        createStations();
        LineRequest dto = new LineRequest("2호선", "bg-red-600", 1L, 2L, 10L);
        createLines(dto);

        // when
        ExtractableResponse<Response> response = deleteSection(1L, 2L);

        // then
        assertThat(response.jsonPath().getString("errorCode")).isEqualTo("FAIL_TO_DELETE_SECTION_OF_LINE_THAT_LENGTH_IS_ONE");
    }

    /**
     * Given 삭제할 구간에 해당하는 역과 노선을 생성하고
     * When 구간의 하행역이 지하철노선의 하행 종점역이 아닐 때
     * Then 예외가 발생된다
     */
    // TODO: 구간 삭제 시 구간의 하행역과 지하철노선의 하행 종점역이 다를 경우 예외 발생
    @DisplayName("구간 삭제 시 구간의 하행역과 지하철노선의 하행 종점역이 다를 경우 예외가 발생된다.")
    @Test
    void deleteSectionIfDownboundStationOfSectionIsDifferentFromDownboundStationOfLine() {
        // given
        createStations();
        LineRequest dto = new LineRequest("2호선", "bg-red-600", 1L, 2L, 10L);
        ExtractableResponse<Response> response = createLines(dto);

        Long createdLineId = Long.parseLong(response.jsonPath().getString("id"));
        Long upStationId = Long.parseLong(response.jsonPath().getString("downStation.id"));
        Long downStationId = 3L;
        Long distance = 5L;
        SectionRequest sectionRequest = new SectionRequest(upStationId, downStationId, distance);
        addSection(createdLineId, sectionRequest);
        assertThat(getLine("downStation.id", String.valueOf(createdLineId))).isEqualTo("3");

        // when
        ExtractableResponse<Response> response2 = deleteSection(1L, 2L);

        // then
        assertThat(response2.jsonPath().getString("errorCode")).isEqualTo("MISMATCH_BETWEEN_UPPER_STATION_OF_SECTION_AND_LOWER_STATION_OF_LINE");
    }
}
