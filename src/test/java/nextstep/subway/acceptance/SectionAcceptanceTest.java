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
    @DisplayName("정상적으로 역을 생성하여 기존 노선 끝에 구간을 추가한다")
    public void addSection() {
        // given
        Long lineId = 지하철_노선_생성("2호선", "bg-blue-600", upStationId, downStationId, 10).jsonPath().getLong("id");
        Long stationId = 지하철역_생성("성수역").jsonPath().getLong("id");

        // when
        int statusCode = 지하철_구간_등록_요청(lineId, downStationId, stationId, 1);

        // then
        ExtractableResponse<Response> response = 지하철_단일_노선_조회(lineId);
        List<String> stationList = response.jsonPath().getList("stations.name", String.class);
        assertAll(
                () -> assertThat(statusCode).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(stationList.get(2)).isEqualTo("성수역")
        );
    }

    /**
     * Given 지하철 노선을 1개 생성
     * Given 추가할 역 1개 생성
     * When 기존 노선에 등록된 상행역을 상행역으로 하고
     * When 새로 생성한 역을 하행역으로 하고
     * When 구간 생성 요청을 하면
     * Then 예외를 발생시킨다
     */
    @Test
    @DisplayName("기존 노선의 하행이 신규 구간의 상행과 일치하지 않는경우 실패한다")
    public void addSectionWithInvalidUpStation() {
        // given
        Long lineId = 지하철_노선_생성("2호선", "bg-blue-600", upStationId, downStationId, 10).jsonPath().getLong("id");
        Long stationId = 지하철역_생성("성수역").jsonPath().getLong("id");

        // when
        int statusCode = 지하철_구간_등록_요청(lineId, upStationId, stationId, 3);

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public int 지하철_구간_등록_요청(Long lineId, Long upStationId, Long downStationId, Integer distance) {
        HashMap<String, String> requestParam = new HashMap<>();
        requestParam.put("upStationId", upStationId.toString());
        requestParam.put("downStationId", downStationId.toString());
        requestParam.put("distance", distance.toString());

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestParam)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract()
                .statusCode();
    }
}
