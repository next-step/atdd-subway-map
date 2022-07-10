package nextstep.subway.test.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.test.acceptance.StationRequest.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest{
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given
        ExtractableResponse<Response> response = 지하철역_생성("개봉역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        List<String> stationNames = 지하철목록조회();

        // then
        assertThat(stationNames).containsAnyOf("개봉역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void findStationList() {
        // given
        지하철역_생성("개봉역");
        지하철역_생성("구일역");

        // when
        List<String> response = 지하철목록조회();

        //then
        assertThat(response).contains("구일역", "개봉역");
    }
    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        지하철역_생성("오류역");
        long stationId = 지하철역_생성("개봉역")
                .jsonPath().getLong("id");
        ExtractableResponse<Response> response =
                RestAssured
                        .given().log().all()
                        .pathParam("id", stationId)
                        .when()
                        .delete("/stations/{id}")
                        .then().log().all()
                        .extract()
                ;
        List<String> stationNames = 지하철목록조회();

        assertThat(stationNames).doesNotContain("개봉역");
    }

}