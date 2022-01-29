package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    // 자주 사용되는 문자열 상수로 분리
    private static final String PATH_PREFIX = "/lines";
    private static final String LINE_NAME_A = "신분당선";
    private static final String LINE_COLOR_A = "bg-red-600";
    private static final String LINE_NAME_B = "2호선";
    private static final String LINE_COLOR_B = "bg-green-600";
    private static final String NAME = "name";
    private static final String COLOR = "color";
    private static final String LOCATION = "Location";

    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = lineCreateRequest(LINE_NAME_A, LINE_COLOR_A);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(LOCATION)).isNotBlank();
    }

    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given

        ExtractableResponse<Response> responseA = lineCreateRequest(LINE_NAME_A, LINE_COLOR_A);
        ExtractableResponse<Response> responseB = lineCreateRequest(LINE_NAME_B, LINE_COLOR_B);

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
        ExtractableResponse<Response> createResponse = lineCreateRequest(LINE_NAME_A, LINE_COLOR_A);

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
        ExtractableResponse<Response> createResponse = lineCreateRequest(LINE_NAME_A, LINE_COLOR_A);

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
        ExtractableResponse<Response> createResponse = lineCreateRequest(LINE_NAME_A, LINE_COLOR_A);

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
        ExtractableResponse<Response> createResponse = lineCreateRequest(LINE_NAME_A, LINE_COLOR_A);

        // when
        ExtractableResponse<Response> duplicateCreationResponse =
                lineCreateRequest(LINE_NAME_A, LINE_COLOR_A);

        // then
        assertThat(duplicateCreationResponse.statusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("노선에 구간 추가")
    @Test
    void addNewStationSectionTest(){
        // given
        // 노선을 생성하고 해당 노선에 종점역을 추가한 후

        // when
        // 해당 노선에 상, 하선의 구간 추가를 요청하면

        // then
        // 구간 추가가 성공한다.
    }


    @DisplayName("노선에 구간 추가")
    @Test
    void addFirstSectionTest(){
        // Given
        // 노선을 생성하고

        // when
        // 해당 노선에 구간을 추가하면

        // then
        // 구간 추가가 성공한다.
    }

    @DisplayName("노선 구간 제거")
    @Test
    void deleteStationSectionTest(){
        // given
        // 노선을 생성하고 해당 노선에 구간을 추가한 후

        // when
        // 해당 구간을 삭제하면

        // then
        // 구간이 삭제된다.
    }

    @DisplayName("노선 구간 제거 실패")
    @Test
    void deleteIgnoredStationSectionTest(){
        // given
        // 노선을 생성하고 해당 노선에 구간을 추가한 후

        // when
        // 해당 구간을 삭제하면

        // then
        // 구간이 삭제된다.
    }

    /** 반복되는 생성 코드를 줄이기 위해 createRequest 를 따로 작성 */
    static ExtractableResponse<Response> lineCreateRequest(String name, String color) {

        Map<String, String> createRequest = new HashMap<>();
        createRequest.put(NAME, name);
        createRequest.put(COLOR, color);
        return RestAssured.given()
                .log()
                .all()
                .body(createRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(PATH_PREFIX)
                .then()
                .log()
                .all()
                .extract();
    }

    static ExtractableResponse<Response> specificLineReadRequest(String url) {
        return RestAssured.given().log().all().when().get(url).then().log().all().extract();
    }

    static ExtractableResponse<Response> lineUpdateRequest(String uri, String name, String color) {

        Map<String, String> updateRequest = new HashMap<>();
        updateRequest.put(NAME, name);
        updateRequest.put(COLOR, color);

        return RestAssured.given()
                .log()
                .all()
                .body(updateRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(uri)
                .then()
                .log()
                .all()
                .extract();
    }
}
