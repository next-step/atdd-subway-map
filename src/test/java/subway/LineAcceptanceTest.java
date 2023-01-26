package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.domain.Station;
import subway.executor.StationServiceExecutor;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    //TODO 지하철 노선 생성

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Test
    @DisplayName("지하철 노선 생성")
    void createLine() {
        //given

        StationServiceExecutor.createStation("정자역");
        StationServiceExecutor.createStation("판교역");


        Map<String, String> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "red");
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "10");


        //when
        ExtractableResponse<Response> response = RestAssured.given().body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        //then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.jsonPath().getString("name")).isEqualTo("신분당선"),
                () -> assertThat(response.jsonPath().getList("stations", Station.class)).hasSize(2)
        );

    }


    //TODO 지하철 노선 목록 조회
    /**
     *   Given 2개의 지하철 노선을 생성하고
     *   When 지하철 노선 목록을 조회하면
     *   Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */

    //TODO 지하철노선 조회
    /**
     *  Given 지하철 노선을 생성하고
     *  When 생성한 지하철 노선을 조회하면
     *  Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */

    //TODO 지하철노선 수정
    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */

    //TODO 지하철노선 삭제
    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */


}
