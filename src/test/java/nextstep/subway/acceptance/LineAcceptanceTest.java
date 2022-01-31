package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.request.LineRequest.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nextstep.subway.acceptance.request.StationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    // 자주 사용되는 문자열 상수로 분리
    private static final String LINE_NAME_A = "신분당선";
    private static final String LINE_COLOR_A = "bg-red-600";
    private static final String LINE_NAME_B = "2호선";
    private static final String LINE_COLOR_B = "bg-green-600";
    private static final int FIRST_DISTANCE = 2;

    // TODO: 역 없이 노선 생성이 가능할까?
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // given
        ExtractableResponse<Response> createFirstStationResponse =
                StationRequest.stationCreateRequest("강남역");
        ExtractableResponse<Response> createSecondStationResponse =
                StationRequest.stationCreateRequest("역삼역");

        Long upStationId = createFirstStationResponse.jsonPath().getLong("id");
        Long downStationId = createSecondStationResponse.jsonPath().getLong("id");

        // when 지하철 노선(상행 종점 구간, 하행 종점 구간 포함) 생성을 요청 하면
        ExtractableResponse<Response> response =
                lineCreateRequest(
                        LINE_NAME_A, LINE_COLOR_A, upStationId, downStationId, FIRST_DISTANCE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(LOCATION)).isNotBlank();
    }

    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createFirstStationResponse =
                StationRequest.stationCreateRequest("강남역");
        ExtractableResponse<Response> createSecondStationResponse =
                StationRequest.stationCreateRequest("역삼역");
        ExtractableResponse<Response> createThirdStationResponse =
                StationRequest.stationCreateRequest("양재역");

        Long upStationAId = createFirstStationResponse.jsonPath().getLong("id");
        Long downStationAId = createSecondStationResponse.jsonPath().getLong("id");
        Long downStationBId = createThirdStationResponse.jsonPath().getLong("id");

        ExtractableResponse<Response> responseA =
                lineCreateRequest(
                        LINE_NAME_A, LINE_COLOR_A, upStationAId, downStationAId, FIRST_DISTANCE);
        ExtractableResponse<Response> responseB =
                lineCreateRequest(
                        LINE_NAME_B, LINE_COLOR_B, upStationAId, downStationBId, FIRST_DISTANCE);

        // when
        ExtractableResponse<Response> response =
                RestAssured.given()
                        .log()
                        .all()
                        .when()
                        .get(PATH_PREFIX)
                        .then()
                        .log()
                        .all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> stationNames = response.jsonPath().getList(NAME);
        assertThat(stationNames).contains(LINE_NAME_A, LINE_NAME_B);
    }

    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createFirstStationResponse =
                StationRequest.stationCreateRequest("강남역");
        ExtractableResponse<Response> createSecondStationResponse =
                StationRequest.stationCreateRequest("역삼역");

        Long upStationId = createFirstStationResponse.jsonPath().getLong("id");
        Long downStationId = createSecondStationResponse.jsonPath().getLong("id");

        // when 지하철 노선(상행 종점 구간, 하행 종점 구간 포함) 생성을 요청 하면
        ExtractableResponse<Response> createResponse =
                lineCreateRequest(
                        LINE_NAME_A, LINE_COLOR_A, upStationId, downStationId, FIRST_DISTANCE);

        String uri = createResponse.header(LOCATION);
        // when
        ExtractableResponse<Response> readLineResponse = specificLineReadRequest(uri);

        // then
        assertThat(readLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        String responseLineName = readLineResponse.jsonPath().getString(NAME);
        assertThat(responseLineName).isEqualTo(LINE_NAME_A);
    }

    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createFirstStationResponse =
                StationRequest.stationCreateRequest("강남역");
        ExtractableResponse<Response> createSecondStationResponse =
                StationRequest.stationCreateRequest("역삼역");

        Long upStationId = createFirstStationResponse.jsonPath().getLong("id");
        Long downStationId = createSecondStationResponse.jsonPath().getLong("id");

        ExtractableResponse<Response> createResponse =
                lineCreateRequest(
                        LINE_NAME_A, LINE_COLOR_A, upStationId, downStationId, FIRST_DISTANCE);

        String uri = createResponse.header(LOCATION);
        // when
        String updateLineName = "구분당선";
        String updateLineColor = "bg-blue-600";
        ExtractableResponse<Response> updateResponse =
                lineUpdateRequest(uri, updateLineName, updateLineColor);

        // then
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // addition: to verify changed contents
        ExtractableResponse<Response> readUpdatedLineResponse = specificLineReadRequest(uri);
        assertThat(readUpdatedLineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        String readUpdatedLineName = readUpdatedLineResponse.jsonPath().getString(NAME);
        String readUpdatedLineColor = readUpdatedLineResponse.jsonPath().getString(COLOR);
        assertThat(readUpdatedLineName).isEqualTo(updateLineName);
        assertThat(readUpdatedLineColor).isEqualTo(updateLineColor);
    }

    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createFirstStationResponse =
                StationRequest.stationCreateRequest("강남역");
        ExtractableResponse<Response> createSecondStationResponse =
                StationRequest.stationCreateRequest("역삼역");

        Long upStationId = createFirstStationResponse.jsonPath().getLong("id");
        Long downStationId = createSecondStationResponse.jsonPath().getLong("id");

        ExtractableResponse<Response> createResponse =
                lineCreateRequest(
                        LINE_NAME_A, LINE_COLOR_A, upStationId, downStationId, FIRST_DISTANCE);

        // when
        String uri = createResponse.header(LOCATION);

        ExtractableResponse<Response> deleteResponse =
                RestAssured.given().log().all().when().delete(uri).then().log().all().extract();

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        // addition: to verify deleted contents
        ExtractableResponse<Response> readDeletedLineResponse = specificLineReadRequest(uri);
        assertThat(readDeletedLineResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("중복된 이름으로 노선을 생성할 수 없다.")
    @Test
    void duplicateNameCreationTest() {
        // given
        ExtractableResponse<Response> createFirstStationResponse =
                StationRequest.stationCreateRequest("강남역");
        ExtractableResponse<Response> createSecondStationResponse =
                StationRequest.stationCreateRequest("역삼역");

        Long upStationId = createFirstStationResponse.jsonPath().getLong("id");
        Long downStationId = createSecondStationResponse.jsonPath().getLong("id");

        ExtractableResponse<Response> createResponse =
                lineCreateRequest(
                        LINE_NAME_A, LINE_COLOR_A, upStationId, downStationId, FIRST_DISTANCE);

        // when
        ExtractableResponse<Response> duplicateCreationResponse =
                lineCreateRequest(
                        LINE_NAME_A, LINE_COLOR_A, upStationId, downStationId, FIRST_DISTANCE);

        // then
        assertThat(duplicateCreationResponse.statusCode())
                .isEqualTo(HttpStatus.CONFLICT.value());
    }

    @DisplayName("노선에 구간 추가")
    @Test
    void addNewStationSectionTest() {
        // given
        // 노선을 생성하고 해당 노선에 종점역을 추가한 후
        ExtractableResponse<Response> createFirstStationResponse =
          StationRequest.stationCreateRequest("강남역");
        ExtractableResponse<Response> createSecondStationResponse =
          StationRequest.stationCreateRequest("역삼역");
        ExtractableResponse<Response> createThirdStationResponse =
          StationRequest.stationCreateRequest("선릉역");

        Long upStationId = createFirstStationResponse.jsonPath().getLong("id");
        Long downStationId = createSecondStationResponse.jsonPath().getLong("id");
        Long newDownStationId = createThirdStationResponse.jsonPath().getLong("id");

        ExtractableResponse<Response> response =
          lineCreateRequest(
            LINE_NAME_A, LINE_COLOR_A, upStationId, downStationId, FIRST_DISTANCE);

        String uri = response.header(LOCATION);

        Map<String, Object> createRequest = new HashMap<>();
        createRequest.put(UP_STATION_ID, downStationId);
        createRequest.put(DOWN_STATION_ID, newDownStationId);
        createRequest.put(DISTANCE, 3);

        // when
        ExtractableResponse<Response> newSectionResponse = RestAssured.given()
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
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("노선 구간 제거")
    @Test
    void deleteStationSectionTest() {
        // given
        // 노선을 생성하고 해당 노선에 구간을 추가한 후
        ExtractableResponse<Response> createFirstStationResponse =
          StationRequest.stationCreateRequest("강남역");
        ExtractableResponse<Response> createSecondStationResponse =
          StationRequest.stationCreateRequest("역삼역");

        Long upStationId = createFirstStationResponse.jsonPath().getLong("id");
        Long downStationId = createSecondStationResponse.jsonPath().getLong("id");

        ExtractableResponse<Response> response =
          lineCreateRequest(
            LINE_NAME_A, LINE_COLOR_A, upStationId, downStationId, FIRST_DISTANCE);

        // when
        // 해당 구간을 삭제하면

        // then
        // 구간이 삭제된다.
    }

}
