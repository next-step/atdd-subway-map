package nextstep.subway.acceptance;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.StationSteps.지하철_역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공 한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        final ExtractableResponse<Response> response = 지하철_노선_생성_요청("신분당선", "bg-red-600");

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank()
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름 으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패 한다.
     */
    @DisplayName("중복 이름 으로 지하철 노선 생성")
    @Test
    void createLineWithDuplicateName() {
        // given
        final String 신분당선 = "신분당선";
        지하철_노선_생성_요청(신분당선, "bg-red-600", "강남역", "역삼역");

        // when
        final ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선, "bg-green-600", "합정역", "당산역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답 받는다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        final String 신분당선 = "신분당선";
        final String 지하철2호선 = "2호선";
        지하철_노선_생성_요청(신분당선, "bg-red-600", "강남역", "역삼역");
        지하철_노선_생성_요청(지하철2호선, "bg-green-600", "합정역", "당산역");

        // when
        final ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getList("name")).contains(신분당선, 지하철2호선)
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답 받는다.
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        final String 강남역 = "강남역";
        final String 역삼역 = "역삼역";
        final ExtractableResponse<Response> upStationCreateResponse = 지하철_역_생성_요청(강남역);
        final ExtractableResponse<Response> downStationCreateResponse = 지하철_역_생성_요청(역삼역);

        final String 신분당선 = "신분당선";
        final ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(
                신분당선,
                "bg-red-600",
                upStationCreateResponse.jsonPath().getLong("id"),
                downStationCreateResponse.jsonPath().getLong("id"),
                1
        );

        // when
        final String path = createResponse.header("Location");
        final ExtractableResponse<Response> response = 지하철_노선_조회_요청(path);

        // then
        final JsonPath responseBody = response.jsonPath();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(responseBody.getLong("id")).isNotNull(),
                () -> assertThat(responseBody.getString("name")).isEqualTo(신분당선),
                () -> assertThat(responseBody.getList("stations.name")).contains(강남역, 역삼역)
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공 한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        final ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청("신분당선", "bg-red-600");

        // when
        final String path = createResponse.header("Location");
        final String 구분당선 = "구분당선";
        final String color = "bg-blue-600";
        지하철_노선_수정_요청(path, 구분당선, color);

        // then
        final ExtractableResponse<Response> response = 지하철_노선_조회_요청(path);
        final JsonPath responseBody = response.jsonPath();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(responseBody.getString("name")).isEqualTo(구분당선),
                () -> assertThat(responseBody.getString("color")).isEqualTo(color)
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공 한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        final ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청("신분당선", "bg-red-600");

        // when
        final String path = createResponse.header("Location");
        final ExtractableResponse<Response> response = 지하철_노선_삭제_요청(path);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * When 노선에 등록된 하행 종점 역을 상행 역으로
     * When 새로 생성한 역을 하행 역으로
     * When 지하철 구간 등록을 요청 하면
     * Then 지하철 구간 등록이 성공 한다.
     */
    @DisplayName("지하철 구간 등록")
    @Test
    void addSection() {
        // given
        final ExtractableResponse<Response> createLineResponse = 지하철_노선_생성_요청();
        final String 세_번째역 = "잠실역";
        final ExtractableResponse<Response> createStationResponse1 = 지하철_역_생성_요청(세_번째역);
        final String 네_번째역 = "사당역";
        final ExtractableResponse<Response> createStationResponse2 = 지하철_역_생성_요청(네_번째역);
        final long lineId = createLineResponse.jsonPath().getLong("id");

        // when
        long upStationId = createLineResponse.jsonPath().getLong("stations[1].id");
        long downStationId = createStationResponse1.jsonPath().getLong("id");
        final ExtractableResponse<Response> response1 = 지하철_구간_등록_요청(lineId, upStationId, downStationId, 1);
        upStationId = downStationId;
        downStationId = createStationResponse2.jsonPath().getLong("id");
        final ExtractableResponse<Response> response2 = 지하철_구간_등록_요청(lineId, upStationId, downStationId, 1);

        // then
        final ExtractableResponse<Response> getLineResponse = 지하철_노선_조회_요청(String.format("/lines/%d", lineId));
        final JsonPath getLineResponseBody = getLineResponse.jsonPath();
        assertAll(
                () -> assertThat(response1.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(response2.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(getLineResponseBody.getString("stations[2].name")).isEqualTo(세_번째역),
                () -> assertThat(getLineResponseBody.getString("stations[3].name")).isEqualTo(네_번째역)
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * When 새로 생성한 역을 상행 역으로
     * When 새로 생성한 역을 하행 역으로
     * When 지하철 구간 등록을 요청 하면
     * Then 지하철 구간 등록이 실패 한다.
     */
    @DisplayName("노선에 등록된 하행 종점 역이 아닌 역을 상행 역 으로 지하철 구간 등록")
    @Test
    void addSectionWithUpStationNoContains() {
        // given
        final ExtractableResponse<Response> createLineResponse = 지하철_노선_생성_요청();
        final ExtractableResponse<Response> createUpStationResponse = 지하철_역_생성_요청("잠실역");
        final ExtractableResponse<Response> createDownStationResponse = 지하철_역_생성_요청("사당역");
        final long lineId = createLineResponse.jsonPath().getLong("id");

        // when
        final long upStationId = createUpStationResponse.jsonPath().getLong("id");
        final long downStationId = createDownStationResponse.jsonPath().getLong("id");
        final ExtractableResponse<Response> response = 지하철_구간_등록_요청(lineId, upStationId, downStationId, 1);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 노선에 등록된 하행 종점 역을 상행 역으로
     * When 이미 노선에 구간 으로 등록된 역을 하행 역으로
     * When 지하철 구간 등록을 요청 하면
     * Then 지하철 구간 등록이 실패 한다.
     */
    @DisplayName("이미 노선에 구간 으로 등록된 역을 하행 역으로 지하철 구간 등록")
    @Test
    void addSectionWithDownStationContains() {
        // given
        final ExtractableResponse<Response> createLineResponse = 지하철_노선_생성_요청();
        final long lineId = createLineResponse.jsonPath().getLong("id");

        // when
        final long upStationId = createLineResponse.jsonPath().getLong("stations[1].id");
        final long downStationId = createLineResponse.jsonPath().getLong("stations[0].id");
        final ExtractableResponse<Response> response = 지하철_구간_등록_요청(lineId, upStationId, downStationId, 1);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * Given 지하철 구간 등록을 요청 하고
     * When 지하철 구간 제거를 요청 하면
     * Then 지하철 구간 제거가 성공 한다.
     */
    @DisplayName("지하철 구간 제거")
    @Test
    void removeSection() {
        // given
        final String 잠실역 = "잠실역";
        final ExtractableResponse<Response> createLineResponse = 지하철_노선_생성_요청();
        final ExtractableResponse<Response> createStationResponse = 지하철_역_생성_요청(잠실역);

        final long lineId = createLineResponse.jsonPath().getLong("id");
        final long upStationId = createLineResponse.jsonPath().getLong("stations[1].id");
        final long downStationId = createStationResponse.jsonPath().getLong("id");
        지하철_구간_등록_요청(lineId, upStationId, downStationId, 1);

        // when
        final ExtractableResponse<Response> response = 지하철_구간_제거_요청(lineId, downStationId);

        // then
        final ExtractableResponse<Response> getLineResponse = 지하철_노선_조회_요청(String.format("/lines/%d", lineId));
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(getLineResponse.jsonPath().getList("stations.name")).doesNotContain(잠실역)
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 지하철 역 생성을 요청 하고
     * Given 지하철 구간 등록을 요청 하고
     * When 하행 종점 역이 아닌 지하철 구간 제거를 요청 하면
     * Then 지하철 구간 제거가 실패 한다.
     */
    @DisplayName("하행 종점 역이 아닌 지하철 구간 제거")
    @Test
    void removeSectionWithoutEndingStation() {
        // given
        final ExtractableResponse<Response> createLineResponse = 지하철_노선_생성_요청();
        final ExtractableResponse<Response> createStationResponse = 지하철_역_생성_요청("잠실역");

        final long lineId = createLineResponse.jsonPath().getLong("id");
        final long upStationId = createLineResponse.jsonPath().getLong("stations[1].id");
        final long downStationId = createStationResponse.jsonPath().getLong("id");
        지하철_구간_등록_요청(lineId, upStationId, downStationId, 1);

        // when
        final ExtractableResponse<Response> response = 지하철_구간_제거_요청(lineId, upStationId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 하행 종점 역으로 지하철 구간 제거를 요청 하면
     * Then 지하철 구간 제거가 실패 한다.
     */
    @DisplayName("구간이 한 개인 지하철 구간 제거")
    @Test
    void removeOnlyOneSection() {
        // given
        final ExtractableResponse<Response> createLineResponse = 지하철_노선_생성_요청();

        final long lineId = createLineResponse.jsonPath().getLong("id");
        final long EndingStationId = createLineResponse.jsonPath().getLong("stations[1].id");

        // when
        final ExtractableResponse<Response> response = 지하철_구간_제거_요청(lineId, EndingStationId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
