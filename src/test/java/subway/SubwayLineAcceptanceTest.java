package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.controller.StationResponse;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

// TODO: controller dto로 이동. 테스트 작성을 위해 임시로 여기에 위치
class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations;

    public LineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}

@DisplayName("지하철역 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SubwayLineAcceptanceTest {
    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createSubwayLine() {
        Map<String, Serializable> params = Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", 1,
                "downStationId", 2,
                "distance", 10
        );

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/lines")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> getLinesResponse =
                RestAssured.given().log().all()
                        .when().get("/lines")
                        .then().log().all()
                        .extract();


        assertThat(getLinesResponse.jsonPath().getString("color")).isEqualTo("bg-red-600");
        assertThat(getLinesResponse.jsonPath().getString("name")).isEqualTo("신분당선");
        assertThat(getLinesResponse.jsonPath().getList("stations.id")).containsExactly(1,2);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getSubwayLineList() {
        List<Map<String, Serializable>> params = List.of(
                Map.of(
                        "name", "분당선",
                        "color", "bg-yellow-600",
                        "upStationId", 1,
                        "downStationId", 2,
                        "distance", 10
                ),
                Map.of(
                        "name", "2호선",
                        "color", "bg-green-600",
                        "upStationId", 1,
                        "downStationId", 3,
                        "distance", 15
                )
        );

        // given
        for (Map<String, Serializable> param:params) {
            RestAssured.given().log().all()
                    .body(param)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post("/lines")
                    .then().log().all();
        }

        // when
        ExtractableResponse<Response> getLinesResponse =
                RestAssured.given().log().all()
                        .when().get("/lines")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract();

        // then
        assertThat(getLinesResponse.jsonPath().getList("name")).containsExactly("분당선", "2호선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선 상세 정보를 조회한다.")
    @Test
    void getSubwayLine() {
        Map<String, Serializable> params = Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", 1,
                "downStationId", 2,
                "distance", 10
        );

        // given
        Long createdId = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract().jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .pathParam("id", createdId)
                        .when().get("/lines/{id}")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract();

        // then
        assertThat(response.jsonPath().getString("name")).isEqualTo("신분당선");
        assertThat(response.jsonPath().getString("color")).isEqualTo("bg-red-600");
        assertThat(response.jsonPath().getList("stations.id")).containsExactly(1,2);
        assertThat(response.jsonPath().getString("distance")).isNullOrEmpty();
    }


    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateSubwayLine() {
        Map<String, Serializable> params = Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", 1,
                "downStationId", 2,
                "distance", 10
        );

        // given
        Long createdId = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract().jsonPath().getLong("id");

        // when
        RestAssured.given().log().all()
                .pathParam("id", createdId)
                .body(Map.of("name", "구분당선", "color", "bg-blue-600"))
                .when().put("/lines/{id}")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        // then
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .pathParam("id", createdId)
                        .when().get("/lines/{id}")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract();

        // then
        assertThat(response.jsonPath().getString("name")).isEqualTo("구분당선");
        assertThat(response.jsonPath().getString("color")).isEqualTo("bg-blue-600");
        assertThat(response.jsonPath().getList("stations.id")).containsExactly(1,2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteSubwayLine() {
        Map<String, Serializable> params = Map.of(
                "name", "신분당선",
                "color", "bg-red-600",
                "upStationId", 1,
                "downStationId", 2,
                "distance", 10
        );

        // given
        Long createdId = RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract().jsonPath().getLong("id");

        // when
        RestAssured.given().log().all()
                .pathParam("id", createdId)
                .when().delete("/lines/{id}")
                .then().log().all().statusCode(HttpStatus.NO_CONTENT.value());

        // then
        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .pathParam("id", createdId)
                        .when().get("/lines")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract();

        // then
        assertThat(response.jsonPath().getList("name")).doesNotContain("신분당선");

    }
}
