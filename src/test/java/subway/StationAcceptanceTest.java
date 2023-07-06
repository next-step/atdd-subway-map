package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import subway.config.IntegrationTest;
import subway.station.StationResponse;
import subway.step.StationStep;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends IntegrationTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = StationStep.지하철역_이름_전체조회_요청();
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 전체 목록을 조회한다.")
    @Test
    void 지하철역_목록조회() {
        // given
        List<String> 역_생성_목록 = List.of("강남역", "사당역");
        StationStep.지하철역_다중_생성_요청(역_생성_목록);

        // when
        List<String> 역_목록 = StationStep.지하철역_이름_전체조회_요청();

        // then
        Assertions.assertThat(역_목록)
            .asList()
            .isEqualTo(역_생성_목록);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역 삭제")
    @Test
    void 지하철역_삭제 () {
        // given
        String 역_이름 = "판교역";
        StationResponse 역 = StationStep.지하철역_생성_요청(역_이름);

        // when
        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("id", 역.getId())
            .when().delete("/stations/{id}")
            .then().log().all();

        // then
        List<String> stationNames = StationStep.지하철역_이름_전체조회_요청();
        Assertions.assertThat(stationNames)
            .asList()
            .doesNotContain(역_이름);
    }

}
