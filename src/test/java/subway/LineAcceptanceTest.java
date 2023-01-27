package subway;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
//@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "10");

        postLine(params).statusCode(HttpStatus.CREATED.value());

        var response = 
                given().log().all().
                when()
                    .get("/lines").
                then().log().all()
                    .extract();

        assertThat(response.jsonPath().getList("id")).contains("신분당선");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void showLines() {
        Map<String, String> paramsA = new HashMap<>();
        paramsA.put("name", "신분당선");
        paramsA.put("color", "bg-red-600");
        paramsA.put("upStationId", "1");
        paramsA.put("downStationId", "2");
        paramsA.put("distance", "10");

        Map<String, String> paramsB = new HashMap<>();
        paramsB.put("name", "분당선");
        paramsB.put("color", "bg-green-600");
        paramsB.put("upStationId", "1");
        paramsB.put("downStationId", "3");
        paramsB.put("distance", "10");

        postLine(paramsA);
        postLine(paramsB);

        var response =
                given().log().all().
                        when()
                        .get("/lines").
                        then().log().all()
                        .extract();

        assertThat(response.jsonPath().getList("name"))
                .hasSize(2)
                .contains("신분당선", "분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void showLine() {
        Map<String, String> paramsA = new HashMap<>();
        paramsA.put("name", "신분당선");
        paramsA.put("color", "bg-red-600");
        paramsA.put("upStationId", "1");
        paramsA.put("downStationId", "2");
        paramsA.put("distance", "10");

        var createResponse = postLine(paramsA).extract();

        var response = given().log().all().
        when()
                .get(createResponse.header("location")).
        then().log().all()
                .extract();

        assertThat(response.jsonPath().getString("name")).isEqualTo("신분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다.
     */
    @DisplayName("")
    @Test
    void updateLine() {
        Map<String, String> createRequest = new HashMap<>();
        createRequest.put("name", "신분당선");
        createRequest.put("color", "bg-red-600");
        createRequest.put("upStationId", "1");
        createRequest.put("downStationId", "2");
        createRequest.put("distance", "10");

        var location = postLine(createRequest).extract().header("location");

        given().log().all().
        when()
                .delete(location).
        then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());

        var response = given().log().all().
                when()
                    .get("/lines").
                then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .extract();

        assertThat(response.jsonPath().getList("name")).doesNotContain("신분당선");
    }

    private ValidatableResponse postLine(Map<String, String> params) {
        return given().log().all()
                    .body(params).
                when()
                    .post("/lines").
                then().log().all();
    }
}
