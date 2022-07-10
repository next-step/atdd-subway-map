package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineCreationRequest;
import nextstep.subway.applicaion.dto.LineModificationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("노선 관련 기능")
class LineAcceptanceTest extends AcceptanceTest {

    private final LineCreationRequest sinbundangLineCreationRequest = new LineCreationRequest(
            "신분당선", "bg-red-600", 1L, 2L, 10L);
    private final LineCreationRequest bundangLineCreationRequest = new LineCreationRequest(
            "분당선", "bg-green-600", 1L, 3L, 20L);

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void canFindTheLineCreatedWhenLineWasCreated() {
        // when
        var creationResponse = createLine(sinbundangLineCreationRequest);

        // then
        var lineNames = RestAssured
                .when()
                    .get("/lines")
                .then()
                    .extract().jsonPath().getList("name", String.class);

        assertAll(
                () -> assertThat(creationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(lineNames).containsExactlyInAnyOrder(sinbundangLineCreationRequest.getName())
        );
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록 조회")
    @Test
    void canFindSameNumberOfLinesWhenLinesWereCreated() {
        // given
        createLine(sinbundangLineCreationRequest);
        createLine(bundangLineCreationRequest);

        // when
        var lineQueryResponse = RestAssured
                .when()
                    .get("/lines")
                .then()
                    .extract();
        var lineNames = lineQueryResponse.jsonPath().getList("name");

        // then
        assertAll(
                () -> assertThat(lineQueryResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(lineNames).containsExactlyInAnyOrder(
                        sinbundangLineCreationRequest.getName(),
                        bundangLineCreationRequest.getName()
                )
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선 조회")
    @Test
    void canGetResponseOfLineInformationByLineId() {
        // given
        var creationResponse = createLine(bundangLineCreationRequest);
        var createdLineId = creationResponse.body().jsonPath().getLong("id");

        // when
        var lineQueryResponse = getLine(createdLineId);

        // then
        var id = lineQueryResponse.jsonPath().getLong("id");
        var name = lineQueryResponse.jsonPath().getString("name");
        var color = lineQueryResponse.jsonPath().getString("color");

        assertAll(
                () -> assertThat(lineQueryResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(id).isEqualTo(createdLineId),
                () -> assertThat(name).isEqualTo(bundangLineCreationRequest.getName()),
                () -> assertThat(color).isEqualTo(bundangLineCreationRequest.getColor())
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선 수정")
    @Test
    void canModifyLineInformationWhichCreated() {
        // given
        var creationResponse = createLine(sinbundangLineCreationRequest);
        var createdLineId = creationResponse.jsonPath().getLong("id");

        // when
        var modificationRequest = new LineModificationRequest("구분당선", "bg-red-600");
        var modificationResponse = RestAssured
                .given()
                    .pathParam("lineId", createdLineId)
                    .body(modificationRequest, ObjectMapperType.JACKSON_2)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .put("/lines/{lineId}")
                .then()
                    .extract();

        // then
        var lineQueryResponse = getLine(createdLineId);
        var modifiedName = lineQueryResponse.jsonPath().getString("name");
        var modifiedColor = lineQueryResponse.jsonPath().getString("color");

        assertAll(
                () -> assertThat(modificationResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(modifiedName).isEqualTo(modificationRequest.getName()),
                () -> assertThat(modifiedColor).isEqualTo(modificationRequest.getColor())
        );
    }

    private ExtractableResponse<Response> createLine(LineCreationRequest creationRequest) {
        return RestAssured
                .given()
                    .body(creationRequest, ObjectMapperType.JACKSON_2)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/lines")
                .then()
                    .extract();
    }

    private ExtractableResponse<Response> getLine(Long lineId) {
        return RestAssured
                .given()
                    .pathParam("lineId", lineId)
                .when()
                    .get("/lines/{lineId}")
                .then()
                    .extract();
    }
}
