package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능")
@AcceptanceTest
public class SectionAcceptanceTest {

    @BeforeEach
    void setUp() {
        StationAcceptanceTest.createStation("A역");
        StationAcceptanceTest.createStation("C역");
        LineAcceptanceTest.createLine(Map.of(
            "name", "1호선",
            "color", "bg-red-600",
            "upStationId", 1L,
            "downStationId", 2L,
            "distance", 10
        ));
        StationAcceptanceTest.createStation("B역");
    }

    /**
     * When 지하철 구간을 등록하면
     * Then 지하철 구간이 등록된다
     * Then 지하철 노선 조회 시 등록한 구간을 찾을 수 있다
     */
    @Test
    @DisplayName("지하철 구간을 등록한다")
    void registerSection() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all().pathParam("id", 1)
            .body(Map.of(
                "downStationId", 3L,
                "upStationId", 2L,
                "distance", 10
            ))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines/{id}/sections")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> lineResponse = RestAssured.given().log().all().pathParam("id", 1)
            .when().get("/lines/{id}")
            .then().log().all()
            .extract();

        assertThat(lineResponse.jsonPath().getList("stations.id.flatten()", Long.class)).containsExactly(1L, 2L, 3L);
    }

    /**
     * When 해당 노선의 하행 종점역이 아닌 역을 새로운 구간의 상행역으로 등록하면
     * Then 400에러가 발생하고
     * Then 에러 메시지를 응답받는다
     * Then 지하철 노선 조회 시 등록한 구간을 찾을 수 없다
     */
    @Test
    @DisplayName("하행 종점역이 아닌 역을 새로운 구간의 상행역으로 등록하면 에러가 발생한다")
    void registerNonDownEndStationAsUpStation() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all().pathParam("id", 1)
            .body(Map.of(
                "downStationId", 3L,
                "upStationId", 1L,
                "distance", 10
            ))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines/{id}/sections")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        // then
        assertThat(response.body().asString()).isEqualTo("하행 종점역이 아닌 역을 새로운 구간의 상행역으로 등록할 수 없습니다.");

        // then
        ExtractableResponse<Response> lineResponse = RestAssured.given().log().all().pathParam("id", 1)
            .when().get("/lines/{id}")
            .then().log().all()
            .extract();

        assertThat(lineResponse.jsonPath().getList("stations.id.flatten()", Long.class)).containsExactly(1L, 2L);
    }

    /**
     * When 해당 노선에 이미 등록된 역을 새로운 구간의 하행역으로 등록하면
     * Then 400에러가 발생하고
     * Then 에러 메시지를 응답받는다
     * Then 지하철 노선 조회 시 등록한 구간을 찾을 수 없다
     */
    @Test
    @DisplayName("해당 노선에 이미 등록된 역을 새로운 구간의 하행역으로 등록하면 에러가 발생한다")
    void registerAlreadyRegisteredStationAsDownStation() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all().pathParam("id", 1L)
            .body(Map.of(
                "downStationId", 1L,
                "upStationId", 2L,
                "distance", 10
            ))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines/{id}/sections")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        // then
        assertThat(response.body().asString()).isEqualTo("해당 노선에 이미 등록된 역을 새로운 구간의 하행역으로 등록할 수 없습니다.");

        // then
        ExtractableResponse<Response> lineResponse = RestAssured.given().log().all().pathParam("id", 1L)
            .when().get("/lines/{id}")
            .then().log().all()
            .extract();

        assertThat(lineResponse.jsonPath().getList("stations.id.flatten()", Long.class)).containsExactly(1L, 2L);
    }

    /**
     * Given 새로운 지하철 구간을 등록하고
     * When 해당 지하철 구간을 제거하면
     * Then 해당 지하철 구간 정보는 제거된다
     * Then 지하철 노선 조회 시 구간이 제거된 것을 확인할 수 있다
     */
    @Test
    @DisplayName("지하철 구간을 제거한다")
    void deleteSection() {
        // given
        RestAssured.given().log().all().pathParam("id", 1)
            .body(Map.of(
                "downStationId", 3L,
                "upStationId", 2L,
                "distance", 10
            ))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines/{id}/sections")
            .then().log().all()
            .extract();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .pathParam("id", 1L)
            .queryParam("stationId", 3L)
            .when().delete("/lines/{id}/sections")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        ExtractableResponse<Response> lineResponse = RestAssured.given().log().all().pathParam("id", 1L)
            .when().get("/lines/{id}")
            .then().log().all()
            .extract();

        assertThat(lineResponse.jsonPath().getList("stations.id.flatten()", Long.class)).containsExactly(1L, 2L);
    }


    /**
     * Given 지하철 구간을 등록하고
     * When 마지막이 아닌 구간을 제거하면
     * Then 400에러가 발생하고
     * Then 에러 메시지를 응답받는다
     */
    @Test
    @DisplayName("마지막이 아닌 지하철 구간을 제거하면 에러가 발생한다")
    void deleteNonLastSection() {
        // given
        RestAssured.given().log().all().pathParam("id", 1)
            .body(Map.of(
                "downStationId", 3L,
                "upStationId", 2L,
                "distance", 10
            ))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/lines/{id}/sections")
            .then().log().all()
            .extract();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .pathParam("id", 1L)
            .queryParam("stationId", 2L)
            .when().delete("/lines/{id}/sections")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        // then
        assertThat(response.body().asString()).isEqualTo("마지막이 아닌 구간은 제거할 수 없습니다.");
    }

    /**
     * When 유일한 지하철 구간을 제거하면
     * Then 400에러가 발생한다
     * Then 에러 메시지를 응답받는다
     */
    @Test
    @DisplayName("유일한 지하철 구간을 제거하면 에러가 발생한다")
    void deleteOnlySection() {
        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .pathParam("id", 1L)
            .queryParam("stationId", 2L)
            .when().delete("/lines/{id}/sections")
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        // then
        assertThat(response.body().asString()).isEqualTo("구간이 1개 남은 경우 제거할 수 없습니다.");
    }
}
