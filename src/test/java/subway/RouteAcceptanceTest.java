package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RouteAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철 노선을 생성합니다.")
    @Test
    void createStationRoute() {
        //given
        ExtractableResponse<Response> 지하철역 = StationAcceptanceTest.createSubwayStation("지하철역");
        ExtractableResponse<Response> 새로운지하철역 = StationAcceptanceTest.createSubwayStation("새로운지하철역");
        Long upStationId = StationAcceptanceTest.extractStationId(지하철역);
        Long downStationId = StationAcceptanceTest.extractStationId(새로운지하철역);

        // when
        Map<String, Object> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", 10);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/routes")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        long id = response.jsonPath().getLong("id");
        assertThat(response.header("Location")).isEqualTo("/routes/" + id);

        assertThat(response.jsonPath().getString("name")).isEqualTo("신분당선");
        assertThat(response.jsonPath().getString("color")).isEqualTo("bg-red-600");

        List<String> names = response.jsonPath().getList("stations.name", String.class);
        assertThat(names).containsOnly("지하철역", "새로운지하철역");
    }
}
