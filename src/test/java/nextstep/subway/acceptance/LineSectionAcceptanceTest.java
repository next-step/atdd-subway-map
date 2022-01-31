package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.step.LineRequest.*;
import static nextstep.subway.acceptance.step.LineRequest.lineCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import nextstep.subway.acceptance.step.StationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class LineSectionAcceptanceTest extends AcceptanceTest {

    private static final String LINE_NAME_A = "신분당선";
    private static final String LINE_COLOR_A = "bg-red-600";
    private static final int FIRST_DISTANCE = 2;

    @DisplayName("노선에 구간 추가")
    @Test
    void addNewStationSectionTest() {
        // given
        Long upStationId =
                StationRequest.stationCreateRequest(StationNames.강남역.stationName()).jsonPath().getLong("id");
        Long downStationId =
          StationRequest.stationCreateRequest(StationNames.역삼역.stationName()).jsonPath().getLong("id");
        Long newDownStationId =
          StationRequest.stationCreateRequest(StationNames.선릉역.stationName()).jsonPath().getLong("id");

        ExtractableResponse<Response> response =
                lineCreateRequest(
                        LINE_NAME_A, LINE_COLOR_A, upStationId, downStationId, FIRST_DISTANCE);

        String uri = response.header(LOCATION);

        Map<String, Object> createRequest = new HashMap<>();
        createRequest.put(UP_STATION_ID, downStationId);
        createRequest.put(DOWN_STATION_ID, newDownStationId);
        createRequest.put(DISTANCE, 3);

        // when
        ExtractableResponse<Response> newSectionResponse =
                RestAssured.given()
                        .log()
                        .all()
                        .body(createRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post(uri + "/sections")
                        .then()
                        .log()
                        .all()
                        .extract();

        // then
        assertThat(newSectionResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        // TODO 노선 구간 추가된 것에 대한 검증 필요
    }

    @DisplayName("노선 구간 제거")
    @Test
    void deleteStationSectionTest() {
        // given
        Long upStationId =
          StationRequest.stationCreateRequest(StationNames.강남역.stationName()).jsonPath().getLong("id");
        Long downStationId =
          StationRequest.stationCreateRequest(StationNames.역삼역.stationName()).jsonPath().getLong("id");
        Long newDownStationId =
          StationRequest.stationCreateRequest(StationNames.선릉역.stationName()).jsonPath().getLong("id");

        ExtractableResponse<Response> response =
                lineCreateRequest(
                        LINE_NAME_A, LINE_COLOR_A, upStationId, downStationId, FIRST_DISTANCE);

        Map<String, Object> createRequest = new HashMap<>();
        createRequest.put(UP_STATION_ID, downStationId);
        createRequest.put(DOWN_STATION_ID, newDownStationId);
        createRequest.put(DISTANCE, 3);

        String uri = response.header(LOCATION);

        ExtractableResponse<Response> newSectionResponse =
                RestAssured.given()
                        .log()
                        .all()
                        .body(createRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .post(uri + "/sections")
                        .then()
                        .log()
                        .all()
                        .extract();

        // when
        ExtractableResponse<Response> deleteSectionResponse =
                RestAssured.given()
                        .log()
                        .all()
                        .body(createRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when()
                        .delete(uri + "/sections?stationId=" + newDownStationId)
                        .then()
                        .log()
                        .all()
                        .extract();

        // then
        assertThat(deleteSectionResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        // TODO 노선 구간 삭제된 것에 대한 검증 필요
    }

    @DisplayName("노선 구간 조회")
    @Test
    void listSectionsFromQueryToLineTest() {
        Long upStationId =
          StationRequest.stationCreateRequest(StationNames.강남역.stationName()).jsonPath().getLong("id");
        Long downStationId =
          StationRequest.stationCreateRequest(StationNames.역삼역.stationName()).jsonPath().getLong("id");

        ExtractableResponse<Response> response =
          lineCreateRequest(
            LINE_NAME_A, LINE_COLOR_A, upStationId, downStationId, FIRST_DISTANCE);

        String lineUrl = response.header(LOCATION);

        ExtractableResponse<Response> queryResponse = specificLineReadRequest(lineUrl);

        assertThat(queryResponse.jsonPath().getList("stations.name", String.class)).containsExactly(StationNames.강남역.stationName(), StationNames.역삼역.stationName());
    }
}
