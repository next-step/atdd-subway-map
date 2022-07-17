package nextstep.subway.acceptance.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.acceptance.station.StationSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void 지하철역_생성_테스트() {
        // when 지하철역을 생성하면
        ExtractableResponse<Response> response = 지하철역_생성(GANGNAM_STATION_NAME);
        StationSteps.노선_생성_검증(response);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void 지하철역_목록_조회_테스트() {
        // given 2개의 지하철역을 생성하고
        지하철역_생성(GANGNAM_STATION_NAME);
        지하철역_생성(YUKSAM_STATION_NAME);

        // when 지하철역 목록을 조회하면
        List<String> stationNames = 지하철역_목록_조회().jsonPath().getList("name");

        // then 2개의 지하철역을 응답 받는다
        assertAll(
                // then 지하철역이 생성된다
                () -> assertThat(stationNames.size()).isEqualTo(2),
                // then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
                () -> assertThat(stationNames).containsAnyOf(GANGNAM_STATION_NAME),
                () -> assertThat(stationNames).containsAnyOf(YUKSAM_STATION_NAME)
        );
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역 삭제한다.")
    @Test
    void 지하철역_삭제_테스트() {
        // 지하철역 생성
        ExtractableResponse<Response> createResponse = 지하철역_생성(GANGNAM_STATION_NAME);
        // 지하철역 삭제
        ExtractableResponse<Response> deleteResponse = 지하철역_삭제(createResponse.jsonPath().get("id"));
        // 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
        역_삭제_검증();
    }

    private ExtractableResponse<Response> 지하철역_삭제(int id) {
        return RestAssured.given().log().all()
                .when().delete("/stations/" + id)
                .then().log().all()
                .extract();
    }

}