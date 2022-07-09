package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;

import static nextstep.subway.acceptance.StationRequestCollection.지하철역_생성;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Test
    public void createLine() {
        // given
        Long upStationId = 지하철역_생성("강남역").jsonPath().getLong("id");
        Long downStationId = 지하철역_생성("건대입구역").jsonPath().getLong("id");

        HashMap<String, String> requestParam = new HashMap<>();
        requestParam.put("name", "2호선");
        requestParam.put("color", "bg-green-600");
        requestParam.put("upStationId", upStationId.toString());
        requestParam.put("downStationId", downStationId.toString());
        requestParam.put("distance", "10");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestParam)
                .when().post("/lines")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();

        JsonPath responseBody = response.jsonPath();
        assertThat(responseBody.getLong("id")).isNotNull();
        assertThat(responseBody.getString("name")).isEqualTo("2호선");
        assertThat(responseBody.getString("color")).isEqualTo("bg-green-600");
        assertThat(responseBody.getList("stations.name")).contains("강남역", "건대입구역");
    }

}
