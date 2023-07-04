package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import subway.CommonStep.StationStep;
import subway.dto.StationResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        //given
        Long lineId1 = StationStep.지하철역_생성( "강남역").body().as(StationResponse.class).getId();
        Long lineId2  = StationStep.지하철역_생성( "역삼역").body().as(StationResponse.class).getId();

        // when
        Map<String, String> params = new HashMap<>();
        params.put("name","2호선");
        params.put("color","green");
        params.put("upStationId",String.valueOf(lineId1));
        params.put("downStationId",String.valueOf(lineId2));
        params.put("distance","10");

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/lines")
                        .then().log().all()
                        .extract();

        //then
        List<String> lineNames =
                RestAssured.given().log().all()
                        .when().get("/lines")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);
        assertThat(lineNames).containsAnyOf("2호선");
    }
}
