package subway;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
    RequestSpecification requestSpecification;

    @BeforeEach
    void setting() {
        requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철 역을 생성한다.")
    @Test
    void createStation() {
        String name = "강남역";
        // when
        createStation(name);

        // then
        List<String> stationNames =
                RestAssured
                        .given().spec(requestSpecification)
                        .when().get("/stations")
                        .then().extract().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf(name);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철 역을 조회한다.")
    @Test
    void selectStation() {
        // given
        createStation("강남역");
        createStation("판교역");

        // when
        List<String> list = selectStations().jsonPath()
                .getList("name", String.class);

        // then
        assertThat(list).containsExactly("강남역", "판교역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철 역을 삭제한다.")
    @Test
    void deleteStation() {
        // given
        createStation("강남역");

        // when
        ExtractableResponse<Response> response =
                RestAssured
                        .given().spec(requestSpecification).log().all()
                        .when().delete("/stations/1")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        List<String> list = selectStations().jsonPath()
                .getList("name", String.class);
        assertThat(list).isEmpty();
    }

    void createStation(String name) {
        ExtractableResponse<Response> response =
                RestAssured
                        .given().spec(requestSpecification)
                        .body(Map.entry("name", name))
                        .when().post("/stations")
                        .then()
                        .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    ExtractableResponse<Response> selectStations() {
        return RestAssured
                .given().spec(requestSpecification)
                .when().get("/stations")
                .then()
                .extract();
    }

}