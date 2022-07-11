package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;

import static nextstep.subway.acceptance.LineRequestCollection.지하철_노선_생성;
import static nextstep.subway.acceptance.LineRequestCollection.지하철_단일_노선_조회;
import static nextstep.subway.acceptance.StationRequestCollection.지하철역_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 구간 괸련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    Long upStationId;
    Long downStationId;

    @BeforeEach
    void init() {
        upStationId = 지하철역_생성("강남역").jsonPath().getLong("id");
        downStationId = 지하철역_생성("건대입구역").jsonPath().getLong("id");
    }

    /**
     * Given 지하철 노선을 1개 생성
     * Given 추가할 역 1개 생성
     * When 기존의 노선에 등록된 하행역을 상행역으로 하고
     * When 새로 생성한 역을 하행역으로 하고
     * When 구간 생성 요청을 하면
     * Then 정상적으로 구간이 노선에 추가된다.
     */
    @Test
    public void addSection() {
        // given
        Long lineId = 지하철_노선_생성("2호선", "bg-blue-600", upStationId, downStationId, 10).jsonPath().getLong("id");
        Long stationId = 지하철역_생성("성수역").jsonPath().getLong("id");

        // when
        HashMap<String, String> requestParam = new HashMap<>();
        requestParam.put("upStationId", downStationId.toString());
        requestParam.put("downStationId", stationId.toString());
        requestParam.put("distance", "1");

        int statusCode = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestParam)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract()
                .statusCode();

        // then
        ExtractableResponse<Response> response = 지하철_단일_노선_조회(lineId);
        List<String> stationList = response.jsonPath().getList("stations.name", String.class);
        assertAll(
                () -> assertThat(statusCode).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(stationList.get(2)).isEqualTo("성수역")
        );
    }
}
