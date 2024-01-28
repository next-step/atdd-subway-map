package subway;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import subway.controller.dto.LineCreateRequestBody;
import subway.controller.dto.LineUpdateRequestBody;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@Sql(value = "/sql/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
    private final String routePrefix = "/lines";

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createSubwayLine() {
        LineCreateRequestBody lineCreateRequestBody = new LineCreateRequestBody(
            "신분당선", "bg-red-600", 1L, 2L, 10
        );

        // when
        RestAssured.given().log().all()
                .body(lineCreateRequestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(routePrefix)
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        // then
        JsonPath getLinesResponseJson = this.getJsonPathOfGetLineList();

        assertThat(getLinesResponseJson.getList("color", String.class))
                .containsExactly(lineCreateRequestBody.getColor());

        assertThat(getLinesResponseJson.getList("name", String.class))
                .containsExactly(lineCreateRequestBody.getName());

        assertThat(getLinesResponseJson.getList("stations[0].id", Long.class))
                .containsExactly(
                        lineCreateRequestBody.getUpStationId(),
                        lineCreateRequestBody.getDownStationId()
                );
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getSubwayLineList() {
        // given
        JsonPath createdFixture1 = this.createDefaultLineFixture(
                "분당선", "bg-yellow-600", 1L, 2L, 10
        );
        JsonPath createdFixture2 = this.createDefaultLineFixture(
                "2호선", "bg-green-600", 1L, 3L, 15
        );

        // when
        JsonPath getLinesResponseJson =
                RestAssured.given().log().all()
                        .when().get(routePrefix)
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract().jsonPath();

        // then
        assertThat(getLinesResponseJson.getList("name", String.class))
                .containsExactly(
                        createdFixture1.getString("name"),
                        createdFixture2.getString("name")
                );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     * XXX: EntityNotFound 상황에 대한 테스트 추가
     */
    @DisplayName("지하철 노선 상세 정보를 조회한다.")
    @Test
    void getSubwayLine() {
        JsonPath createdFixture = this.createDefaultLineFixture(
                "신분당선", "bg-red-600", 1L, 2L, 10
        );

        // when
        JsonPath response =
                RestAssured.given().log().all()
                        .pathParam("id", createdFixture.getLong("id"))
                        .when().get(routePrefix + "/{id}")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract().jsonPath();

        // then
        assertThat(response.getString("name"))
                .isEqualTo(createdFixture.getString("name"));

        assertThat(response.getString("color"))
                .isEqualTo(createdFixture.getString("color"));

        assertThat(response.getList("stations.id", Long.class))
                .isEqualTo(createdFixture.getList("stations.id", Long.class)
        );
        assertThat(response.getString("distance")).isNullOrEmpty();
    }


    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateSubwayLine() {
        // given
        JsonPath createdFixture = this.createDefaultLineFixture(
                "신분당선", "bg-red-600", 1L, 2L, 10
        );

        // when
        LineUpdateRequestBody lineUpdateRequestBody = new LineUpdateRequestBody("구분당선", "bg-blue-600");

        RestAssured.given().log().all()
                .pathParam("id", createdFixture.getLong("id"))
                .body(lineUpdateRequestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(routePrefix + "/{id}")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        // then
        JsonPath getLineJsonPath = this.getJsonPathOfGetLine(createdFixture.getLong("id"));

        assertThat(getLineJsonPath.getString("name"))
                .isEqualTo(lineUpdateRequestBody.getName());

        assertThat(getLineJsonPath.getString("color"))
                .isEqualTo(lineUpdateRequestBody.getColor());

        assertThat(getLineJsonPath.getList("stations.id", Long.class))
                .isEqualTo(createdFixture.getList("stations.id", Long.class));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteSubwayLine() {
        // given
        JsonPath createdFixture = this.createDefaultLineFixture(
                "신분당선", "bg-red-600", 1L, 2L, 10
        );

        // when
        RestAssured.given().log().all()
                .pathParam("id", createdFixture.getLong("id"))
                .when().delete(routePrefix + "/{id}")
                .then().log().all().statusCode(HttpStatus.NO_CONTENT.value());

        // then
        assertThat(this.getJsonPathOfGetLineList()
                .getList("name", String.class))
                .doesNotContain(createdFixture.getString("name"));
    }

    /***
     * 테스트를 위해 노선을 생성하고 생성된 리스폰스의 JsonPath를 반환합니다.
     */
    private JsonPath createDefaultLineFixture(
            String name,
            String color,
            Long upStationId,
            Long downStationId,
            int distance
    ) {
        LineCreateRequestBody lineCreateRequestBody = new LineCreateRequestBody(
                name, color, upStationId, downStationId, distance
        );
        return RestAssured.given().log().all()
                .body(lineCreateRequestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(routePrefix)
                .then().log().all().extract().jsonPath();
    }

    private JsonPath getJsonPathOfGetLineList() {
        return RestAssured.given().log().all()
                .when().get(routePrefix)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath();
    }

    private JsonPath getJsonPathOfGetLine(Long lineId) {
        return RestAssured.given().log().all()
                .pathParam("id", lineId)
                .when().get(routePrefix + "/{id}")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath();
    }
}
