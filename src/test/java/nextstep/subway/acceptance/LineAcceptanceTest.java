package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.SubwayTestUtils.BUNDANG_LINE_REQUEST;
import static nextstep.subway.acceptance.SubwayTestUtils.SINBUNDANG_LINE_REQUEST;
import static nextstep.subway.acceptance.SubwayTestUtils.createLine;
import static nextstep.subway.acceptance.SubwayTestUtils.getAllLines;
import static nextstep.subway.acceptance.SubwayTestUtils.getLine;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.mapper.ObjectMapperType;
import nextstep.subway.applicaion.dto.LineModificationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("노선 관련 기능")
class LineAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void canFindTheLineCreatedWhenLineWasCreated() {
        // when
        var creationResponse = createLine(SINBUNDANG_LINE_REQUEST);

        // then
        var lineNames = getAllLines().jsonPath().getList("name", String.class);

        assertAll(
                () -> assertThat(creationResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(lineNames).containsExactlyInAnyOrder(SINBUNDANG_LINE_REQUEST.getName())
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
        createLine(SINBUNDANG_LINE_REQUEST);
        createLine(BUNDANG_LINE_REQUEST);

        // when
        var lineQueryResponse = getAllLines();
        var lineNames = lineQueryResponse.jsonPath().getList("name");

        // then
        assertAll(
                () -> assertThat(lineQueryResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(lineNames).containsExactlyInAnyOrder(
                        SINBUNDANG_LINE_REQUEST.getName(),
                        BUNDANG_LINE_REQUEST.getName()
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
        var creationResponse = createLine(BUNDANG_LINE_REQUEST);
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
                () -> assertThat(name).isEqualTo(BUNDANG_LINE_REQUEST.getName()),
                () -> assertThat(color).isEqualTo(BUNDANG_LINE_REQUEST.getColor())
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
        var creationResponse = createLine(SINBUNDANG_LINE_REQUEST);
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

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선 삭제")
    @Test
    void removeCreatedLine() {
        // given
        var creationResponse = createLine(BUNDANG_LINE_REQUEST);
        var createdLineId = creationResponse.jsonPath().getLong("id");

        // when
        var deleteResponse = RestAssured
                .given()
                    .pathParam("lineId", createdLineId)
                .when()
                    .delete("/lines/{lineId}")
                .then()
                    .extract();

        // then
        var lineQueryResponse = getLine(createdLineId);

        assertAll(
                () -> assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(lineQueryResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
        );
    }
}
