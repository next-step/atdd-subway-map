package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.SectionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 구간 기능")
public class LineSectionAcceptanceTest extends AcceptanceTest {

    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private SectionRepository sectionRepository;

    private ExtractableResponse<Response> 강남역;
    private ExtractableResponse<Response> 양재역;
    private ExtractableResponse<Response> 판교역;
    private ExtractableResponse<Response> 광교역;

    private ExtractableResponse<Response> 분당선;

    private

    @BeforeEach
    void setUpLine() {
        강남역 = StationAcceptanceTest.지하철역_생성요청("강남역");
        양재역 = StationAcceptanceTest.지하철역_생성요청("양재역");
        판교역 = StationAcceptanceTest.지하철역_생성요청("판교역");
        광교역 = StationAcceptanceTest.지하철역_생성요청("광교역");

        final long 강남역_id = 강남역.jsonPath().getLong("id");
        final long 판교역_id = 판교역.jsonPath().getLong("id");

        분당선 = LineAcceptanceTest.지하철_노선을_생성한다("분당선", "bg-red-600", 강남역_id, 판교역_id, 5);
    }

    @AfterEach
    public void cleanUp() {
        sectionRepository.deleteAllInBatch();
        lineRepository.deleteAllInBatch();
    }

    /**
     * when 지하철 노선에 구간을 등록하면
     * then 구간이 등록된다
     */
    @DisplayName("지하철 노선 구간을 추가한다.")
    @Test
    void 지하철노선_구간_생성_인수테스트() {
        // when
        지하철노선_구간을_추가요청한다(분당선, 판교역, 광교역, "10");

        // then
        지하철노선_구간에서_찾을수있다("광교역");
    }

    // when 지하철 노선에 새로운 구간의 상행역이 노선의 하행 종점역이 아닌 구간을 등록한다
    // then 구간의 상행역이 노선의 하행역이 아니면 노선 추가에 실패한다
    @DisplayName("구간의 상행역과 노선의 하행역 불일치시 구간 추가에 실패한다")
    @Test
    void 지하철노선_구간_추가_실패_인수테스트() {
        // when
        final ExtractableResponse<Response> response = 지하철노선_구간을_추가요청한다(분당선, 양재역, 광교역, "15");

        // then
        구간_등록에_실패한다(response);
    }

    // when 추가하려는 구간의 하행역이 이미 해당 노선에 등록되어 있는 역일경우
    // then 구간 등록에 실패한다
    @DisplayName("구간의 하행역이 이미 노선에 등록된 역일 경우 구간 추가에 실패한다")
    @Test
    void 지하철노선_구간_추가_실패_인수테스트_2() {
        // when
        final ExtractableResponse<Response> response = 지하철노선_구간을_추가요청한다(분당선, 판교역, 강남역, "10");

        // then
        구간_등록에_실패한다(response);
    }

    private void 지하철노선_구간에서_찾을수있다(String stationName) {
        final JsonPath jsonPath = LineAcceptanceTest.지하철노선_목록_조회요청().jsonPath();
        final String names = jsonPath.getString("stations.name");
        assertThat(names.contains(stationName)).isTrue();
    }

    private ExtractableResponse<Response> 지하철노선_구간을_추가요청한다(ExtractableResponse<Response> line, ExtractableResponse<Response> upStationId, ExtractableResponse<Response> downStationId, String distance) {
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId.jsonPath().getString("id"));
        params.put("downStationId", downStationId.jsonPath().getString("id"));
        params.put("distance", distance);

        final long lineId = line.jsonPath().getLong("id");
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }

    private void 구간_등록에_실패한다(ExtractableResponse<Response> response) {
        final int statusCode = response.statusCode();
        assertThat(statusCode).isEqualTo(500);
    }

}
