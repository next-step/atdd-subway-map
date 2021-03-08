package nextstep.subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.line.LineSteps.지하철_노선_생성요청;
import static nextstep.subway.station.StationSteps.지하철역_생성_요청;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    /*
     1. 노선 구간 추가
        노선 구간 추가 요청
        노선 구간 추가 요청 응답
     2. 노선 구간 제거
        노선 구간 제거 요청
        노선 구간 제거 요청 응답
     3. 노선 구간 목록
        노선 구간 조회 요청
        노선 구간 목록 조회 응답
     */

    private LineResponse pinkLine;
    private StationResponse 천호역;
    private StationResponse 송파역;
    private StationResponse 잠실역;
    private StationResponse 석촌역;
    private StationResponse 남한산성입구역;



    @BeforeEach
    void setup() {
        pinkLine = 지하철_노선_생성요청("8호선", "pink").as(LineResponse.class);

        천호역 = 지하철역_생성_요청("천호역").as(StationResponse.class);
        송파역 = 지하철역_생성_요청("송파역").as(StationResponse.class);
        잠실역 = 지하철역_생성_요청("잠실역").as(StationResponse.class);
        석촌역 = 지하철역_생성_요청("석촌역").as(StationResponse.class);
        남한산성입구역 = 지하철역_생성_요청("남한산성입구역").as(StationResponse.class);
    }

    @DisplayName("지하철 노선 구간 추가 한다.")
    @Test
    void createLineSection() {
        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", 석촌역.getId());
        params.put("downStationId", 남한산성입구역.getId());
        params.put("distance", 4);

        ExtractableResponse<Response> createResponse =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post("/{id}/sections", pinkLine.getId())
                        .then()
                        .log().all().extract();

    }


    @DisplayName("지하철 노선 구간 역 목록을 조회한다.")
    @Test
    void getLineSection() {

    }

    @DisplayName("지하철 노선 구간 제거 한다.")
    @Test
    void deleteLineSection() {
        //{id}/sections
    }
}
