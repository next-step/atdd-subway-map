package subway.line;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

@DisplayName("노선 관리 기능")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LineAcceptanceTest {

    /**
     * When 새로운 지하철 노선을 입력하고, 관리자가 노선을 생성하면
     * Then 해당 노선이 생성된다.
     * Then 노선 목록에 포함된다.
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {

        // when
        Long upStationId = addStation("신사역");
        Long downStationId = addStation("논현역");

        Map<String, Object> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", "10");

        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //then
        String name = response.jsonPath().getString("name");
        assertThat(name).isEqualTo("신분당선");

        List<Long> stationIds = response.jsonPath().getList("stations.id", Long.class);
        assertThat(stationIds).containsExactlyInAnyOrder(upStationId, downStationId);
    }

    private static Long addStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        ExtractableResponse<Response> response =
            RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        String location = response.header("Location");
        Long stationId = Long.parseLong(location.substring(location.lastIndexOf("/") + 1));

        return stationId;
    }
}
