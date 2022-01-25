package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.Builder;
import lombok.Getter;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.SectionRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.steps.LineSteps.지하철노선_생성요청;
import static nextstep.subway.acceptance.steps.SectionSteps.지하철노선_구간생성요청;
import static nextstep.subway.acceptance.steps.StationSteps.지하철역_생성요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    /**
     * Given 상행 종점역 생성을 요청하고
     * Given 하행 종점역 생성을 요청하고
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 및 구간 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // given
        long upStationId = 지하철역_생성요청("신도림").jsonPath().getLong("id");
        long downStationId = 지하철역_생성요청("문래").jsonPath().getLong("id");

        Map<String, String> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "bg-green");
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        params.put("distance", "7");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("중복이름으로 지하철 노선 생성")
    @Test
    void duplicateLineName() {
        // given
        CreateLineRequest request = CreateLineRequest.builder()
                .lineName("2호선")
                .lineColor("bg-green")
                .upStationName("신도림")
                .downStationName("문래")
                .distance(7)
                .build();

        테스트용_지하철노선_생성요청(request);

        // when
        CreateLineRequest request2 = CreateLineRequest.builder()
                .lineName("2호선")
                .lineColor("bg-green")
                .upStationName("영등포구청역")
                .downStationName("당산역")
                .distance(7)
                .build();
        ExtractableResponse<Response> response = 테스트용_지하철노선_생성요청(request2);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(jsonPath("$.exceptionMessage", is("중복된 노선 이름입니다.")));
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        CreateLineRequest request = CreateLineRequest.builder()
                .lineName("2호선")
                .lineColor("bg-green")
                .upStationName("신도림")
                .downStationName("문래")
                .distance(7)
                .build();
        테스트용_지하철노선_생성요청(request);

        CreateLineRequest request2 = CreateLineRequest.builder()
                .lineName("1호선")
                .lineColor("bg-blue")
                .upStationName("부천역")
                .downStationName("소사역")
                .distance(5)
                .build();
        테스트용_지하철노선_생성요청(request2);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("id").size()).isEqualTo(2);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 지하철 노선의 새로운 구간 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() throws Exception{
        // given
        CreateLineRequest request = CreateLineRequest.builder()
                .lineName("2호선")
                .lineColor("bg-green")
                .upStationName("신도림")
                .downStationName("문래")
                .distance(7)
                .build();

        ExtractableResponse<Response> lineCreateResponse = 테스트용_지하철노선_생성요청(request);
        Long lineId = lineCreateResponse.jsonPath().getLong("id");
        List<Map<String, Object>> stations = lineCreateResponse.jsonPath().getList("stations");

        long upStationId = Long.valueOf(String.valueOf(stations.get(stations.size() - 1).get("id")));
        long downStationId = 지하철역_생성요청("영등포구청역").jsonPath().getLong("id");
        SectionRequest sectionRequest = new SectionRequest(upStationId, downStationId, 5);
        지하철노선_구간생성요청(lineId, sectionRequest);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/lines/" + lineId)
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("stations").size()).isEqualTo(3);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        CreateLineRequest request = CreateLineRequest.builder()
                .lineName("2호선")
                .lineColor("bg-green")
                .upStationName("신도림")
                .downStationName("문래")
                .distance(7)
                .build();

        ExtractableResponse<Response> lineCreateResponse = 테스트용_지하철노선_생성요청(request);
        Long lineId = lineCreateResponse.jsonPath().getLong("id");

        Map<String, String> params = new HashMap<>();
        params.put("name", "신1호선");
        params.put("color", "bg-blue-200");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/" + lineId)
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(jsonPath("$.name", is("신1호선")));
        assertThat(jsonPath("$.color", is("bg-blue-200")));
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        CreateLineRequest request = CreateLineRequest.builder()
                .lineName("2호선")
                .lineColor("bg-green")
                .upStationName("신도림")
                .downStationName("문래")
                .distance(7)
                .build();

        ExtractableResponse<Response> lineCreateResponse = 테스트용_지하철노선_생성요청(request);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete(lineCreateResponse.header("Location"))
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 테스트용_지하철노선_생성요청(LineAcceptanceTest.CreateLineRequest createLineRequest) {
        long upStationId = 지하철역_생성요청(createLineRequest.getUpStationName()).jsonPath().getLong("id");
        long downStationId = 지하철역_생성요청(createLineRequest.getDownStationName()).jsonPath().getLong("id");

        LineRequest request = LineRequest.builder()
                .name(createLineRequest.getLineName())
                .color(createLineRequest.getLineColor())
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(createLineRequest.getDistance())
                .build();

        return 지하철노선_생성요청(request);
    }

    @Getter
    public static class CreateLineRequest {
        private String upStationName;
        private String downStationName;
        private String lineName;
        private String lineColor;
        private int distance;

        @Builder
        private CreateLineRequest(String upStationName, String downStationName, String lineName,
                                  String lineColor, int distance) {
            this.upStationName = upStationName;
            this.downStationName = downStationName;
            this.lineName = lineName;
            this.lineColor = lineColor;
            this.distance = distance;
        }
    }
}
