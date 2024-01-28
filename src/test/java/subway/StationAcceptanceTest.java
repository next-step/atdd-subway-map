package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    @DisplayName("지하철 역을 2개 생성하고 등록된 역들을 조회한다.")
    @Test
    void test() {
        // given
        createStation("건대입구역");
        createStation("어린이대공원역");

        // when
        List<String> stationNames = RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract()
                .response().jsonPath().getList("name", String.class);

        // then
        assertThat(stationNames.size()).isEqualTo(2);
        assertThat(stationNames).containsAnyOf(
                "건대입구역", "어린이대공원역"
        );
    }

    @DisplayName("생성한 역을 삭제하면 204 코드를 반환한다.")
    @Test
    void test2() {
        // given
        ExtractableResponse<Response> station = createStation("건대입구역");
        Long stationId = station.response().jsonPath().getLong("id");
        String id = String.valueOf(stationId);

        // when
        Response response = RestAssured.given().log().all()
                .when().delete("/stations/" + id)
                .then().log().all()
                .extract()
                .response();

        // then
        assertThat(response.getStatusCode()).isEqualTo(204);
    }

    @DisplayName("생성한 역이 아닌 역을 삭제하면 204 코드를 반환하지 않는다.")
    @Test
    void test3() {
        // given
        ExtractableResponse<Response> station = createStation("건대입구역");
        Long stationId = station.response().jsonPath().getLong("id");
        String id = String.valueOf(stationId + 1);

        // when
        Response response = RestAssured.given().log().all()
                .when().delete("/stations/" + id)
                .then().log().all()
                .extract()
                .response();

        // then
        assertThat(response.getStatusCode()).isNotEqualTo(204);
    }

    private ExtractableResponse<Response> createStation(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }
}