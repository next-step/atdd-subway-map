package subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
@DisplayName("지하철 노선의 인수 테스트입니다.")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {

    @LocalServerPort
    int port;

    List<Long> stationIds;

    int stationIdIdx;

    @BeforeEach
    @DisplayName("RestAssured 에서 요청 보낼 포트를 설정하고, 4개의 지하철 역 정보를 생성합니다.")
    void setup() {
        RestAssured.port = port;
        stationIds = Stream.of("강남역", "양재역", "신사역", "논현역")
            .map(StationAcceptanceTest::insertStation)
            .map(i -> i.jsonPath().getLong("id"))
            .collect(Collectors.toList());
        stationIdIdx = 0;
    }

    /**
     * given 상행종점역, 하행종점역, 라인명, 라인색상, 라인길이를 통해
     * when 지하철 노선을 생성하면
     * then 지하철 노선이 정상 생성되고(CREATED),
     * then 저장한 노선을 조회할 수 있습니다.
     * */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // given
        Long upStationId = stationIds.get(stationIdIdx++);
        Long downStationId = stationIds.get(stationIdIdx++);
        String name = "신분당선";
        String color = "bg-red-600";
        Long distance = 10L;
        Map<String, Object> requestBody = Map.of(
            "upStationId", upStationId,
            "downStationId", downStationId,
            "name", name,
            "color", color,
            "distance", distance
        );

        // when
        ExtractableResponse<Response> saved = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody)
            .when()
            .post("/lines")
            .then().log().all().extract();

        // then
        assertThat(saved.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> found = RestAssured.when()
            .get("/lines/{id}", saved.jsonPath().getLong("id"))
            .then().log().all()
            .extract();
        assertAll(
            () -> assertThat(found.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(found.jsonPath().getString("name")).isEqualTo(name),
            () -> assertThat(found.jsonPath().getString("color")).isEqualTo(color),
            () -> assertThat(found.jsonPath().getList("stations.id", Long.class)).contains(upStationId, downStationId)
        );
    }

    /** given 지하철 노선을 2개 생성하고
     * when 지하철 노선 목록을 조회하면
     * then 전체 지하철 노선 목록이 조회되고
     * then 생성한 지하철 노선 2개가 포함됩니다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void selectAllLines() {
        // given
        Long upStationId = stationIds.get(stationIdIdx++);
        Long downStationId = stationIds.get(stationIdIdx++);
        String name = "신분당선";
        String color = "bg-red-600";
        Long distance = 10L;
        Map<String, Object> requestBody = Map.of(
            "upStationId", upStationId,
            "downStationId", downStationId,
            "name", name,
            "color", color,
            "distance", distance
        );

        ExtractableResponse<Response> saved = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody)
            .when()
            .post("/lines")
            .then().log().all().extract();

        assertThat(saved.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        Long upStationId2 = stationIds.get(stationIdIdx++);
        Long downStationId2 = stationIds.get(stationIdIdx++);
        String name2 = "2호선";
        String color2 = "bg-green-600";
        Long distance2 = 15L;
        Map<String, Object> requestBody2 = Map.of(
            "upStationId", upStationId2,
            "downStationId", downStationId2,
            "name", name2,
            "color", color2,
            "distance", distance2
        );

        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody2)
            .when()
            .post("/lines")
            .then().log().all().extract();

        assertThat(saved.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        ExtractableResponse<Response> found = RestAssured.when()
            .get("/lines")
            .then().log().all()
            .extract();

        // then
        List<List<Integer>> list = found.jsonPath().getList("lines.stations.id");
        Set<Long> stationIdSet = list.stream()
            .flatMap(List::stream)
            .mapToLong(Long::valueOf)
            .boxed()
            .collect(Collectors.toSet());
        assertAll(
            () -> assertThat(found.jsonPath().getList("lines.name", String.class)).containsExactly(name, name2),
            () -> assertThat(found.jsonPath().getList("lines.color", String.class)).containsExactly(color, color2),
            () -> assertThat(stationIdSet).containsExactly(upStationId, downStationId, upStationId2, downStationId2)
        );
    }

    /**
     * given 지하철 노선을 생성하고
     * when 생성한 지하철 노선을 조회하면
     * then 생성한 지하철 노선 정보를 응답받습니다.
     **/
    @DisplayName("지하철 노선 조회")
    @Test
    void selectALine() {
        // given
        Long upStationId = stationIds.get(stationIdIdx++);
        Long downStationId = stationIds.get(stationIdIdx++);
        String name = "신분당선";
        String color = "bg-red-600";
        Long distance = 10L;
        Map<String, Object> requestBody = Map.of(
            "upStationId", upStationId,
            "downStationId", downStationId,
            "name", name,
            "color", color,
            "distance", distance
        );

        ExtractableResponse<Response> saved = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody)
            .when()
            .post("/lines")
            .then().log().all().extract();

        assertThat(saved.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        ExtractableResponse<Response> found = RestAssured.when()
            .get("/lines/{id}", saved.jsonPath().getLong("id"))
            .then().log().all()
            .extract();

        // then
        assertAll(
            () -> assertThat(found.jsonPath().getLong("id")).isEqualTo(saved.jsonPath().getLong("id")),
            () -> assertThat(found.jsonPath().getString("name")).isEqualTo(name),
            () -> assertThat(found.jsonPath().getString("color")).isEqualTo(color),
            () -> assertThat(found.jsonPath().getList("stations.id", Long.class)).contains(upStationId, downStationId)
        );
    }

    /**
     * given 지하철 노선을 생성하고
     * and 노선 수정 정보를 가진 상태에서
     * when 생성한 노선을 수정 정보로 수정 하면
     * then 정상 수정되고
     * then 생성 노선을 조회했을때 정보가 수정 정보와 일치합니다.
     **/
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        Long upStationId = stationIds.get(stationIdIdx++);
        Long downStationId = stationIds.get(stationIdIdx++);
        String name = "신분당선";
        String color = "bg-red-600";
        Long distance = 10L;
        Map<String, Object> requestBody = Map.of(
            "upStationId", upStationId,
            "downStationId", downStationId,
            "name", name,
            "color", color,
            "distance", distance
        );

        ExtractableResponse<Response> saved = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody)
            .when()
            .post("/lines")
            .then().log().all().extract();

        assertThat(saved.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // and
        Long newUpStationId = stationIds.get(stationIdIdx++);
        Long newDownStationId = stationIds.get(stationIdIdx++);
        String newName = "2호선";
        String newColor = "bg-green-600";
        Long newDistance = 20L;
        Map<String, Object> putRequestBody = Map.of(
            "upStationId", newUpStationId,
            "downStationId", newDownStationId,
            "name", newName,
            "color", newColor,
            "distance", newDistance
        );

        // when
        ExtractableResponse<Response> updated = RestAssured
            .given().contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(putRequestBody)
            .when()
            .put("/lines/{id}", saved.jsonPath().getLong("id"))
            .then().log().all()
            .extract();

        // then
        assertThat(updated.statusCode()).isEqualTo(HttpStatus.OK.value());

        // then
        ExtractableResponse<Response> found = RestAssured.when()
            .get("/lines/{id}", saved.jsonPath().getLong("id"))
            .then().log().all()
            .extract();

        assertAll(
            () -> assertThat(found.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(found.jsonPath().getLong("id")).isEqualTo(saved.jsonPath().getLong("id")),
            () -> assertThat(found.jsonPath().getString("name")).isEqualTo(newName),
            () -> assertThat(found.jsonPath().getString("color")).isEqualTo(newColor),
            () -> assertThat(found.jsonPath().getList("stations.id", Long.class)).contains(newUpStationId, newDownStationId)
        );
    }

    /**
     * given 지하철 노선을 생성하고
     * when 생성한 노선을 삭제하면
     * then 해당 지하철 노선을 조회했을때 결과가 존재하지 않습니다.
     **/
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        Long upStationId = stationIds.get(stationIdIdx++);
        Long downStationId = stationIds.get(stationIdIdx++);
        String name = "신분당선";
        String color = "bg-red-600";
        Long distance = 10L;
        Map<String, Object> requestBody = Map.of(
            "upStationId", upStationId,
            "downStationId", downStationId,
            "name", name,
            "color", color,
            "distance", distance
        );

        ExtractableResponse<Response> saved = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody)
            .when()
            .post("/lines")
            .then().log().all().extract();

        assertThat(saved.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when
        ExtractableResponse<Response> deleted = RestAssured.when()
            .delete("/lines/{id}", saved.jsonPath().getLong("id"))
            .then().log().all()
            .extract();

        // then
        assertAll(
            () -> assertThat(deleted.body().asString()).isBlank(),
            () -> assertThat(deleted.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value())
        );
    }

}
