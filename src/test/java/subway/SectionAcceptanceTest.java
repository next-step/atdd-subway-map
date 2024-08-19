package subway;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.line.LineRequest;

@DisplayName("노선 관련 기능")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SectionAcceptanceTest {

    private Long 사당;
    private Long 방배;
    private Long 서초;
    private Long 교대;
    private Long lineId;

    @BeforeEach
    void init() {
        사당 = getStationId(createStation("station1"));
        방배 = getStationId(createStation("station2"));
        서초 = getStationId(createStation("station3"));
        교대 = getStationId(createStation("station4"));
        lineId = getLineId(createLine(new LineRequest("사당", "red", 사당, 방배, 10L)));
    }

    /**
     * Given: 새로운 지하철 구간 정보를 입력하고,
     * When: 지하철 구간을 생성하면
     * Then: 해당 구간이 노선에 추가된다.
     */
    @DisplayName("지하철 구간 추가 성공")
    @Test
    void createSectionSuccess() {
        // Given
        Map<String, Object> createSectionInfoParam = givenParameterWithCorrectSectionCreationInfo();
        // When
        ExtractableResponse<Response> response = createSection(createSectionInfoParam);
        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    Map<String, Object> givenParameterWithCorrectSectionCreationInfo() {
        return Map.of("downStationId", 서초, "upStationId", 방배, "distance", 10);
    }

    /**
     * Given: 새로운 구간의 상행역이 해당 노선에 등록되어 있는 하행 종점역이 아닐 때,
     * When: 지하철 구간을 생성하면
     * Then: 예외가 발생한다.
     */
    @DisplayName("지하철 구간 추가 실패 - 상행역 불일치")
    @Test
    void createSectionFail1() {
        // Given
        Map<String, Object> createSectionInfoParam = givenParameterWithInconsistentUpStationInfo();
        // When
        ExtractableResponse<Response> response = createSection(createSectionInfoParam);
        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    Map<String, Object> givenParameterWithInconsistentUpStationInfo() {
        return Map.of("downStationId", 교대, "upStationId", 서초, "distance", 10);
    }

    /**
     * Given: 새로운 구간의 하행역이 해당 노선에 이미 등록되어 있을 때,
     * When: 지하철 구간을 생성하면
     * Then: 예외가 발생한다.
     */

    @DisplayName("지하철 구간 추가 실패 - 중복되는 하행역")
    @Test
    void createSectionFail2() {
        // Given
        Map<String, Object> createSectionInfoParam = givenParameterWithDuplicateDownStationInfo();
        // When
        ExtractableResponse<Response> response = createSection(createSectionInfoParam);
        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    Map<String, Object> givenParameterWithDuplicateDownStationInfo() {
        return Map.of("downStationId", 사당, "upStationId", 방배, "distance", 10);
    }

    /**
     * Given: 지하철 노선에 두 개 이상의 구간이 등록 되어있고, 제거하려는 구간(section)이 노선의 하행 종점역일 때,
     * When: 구간을 제거하면
     * Then: 구간이 정상적으로 제거된다.
     */

    @DisplayName("지하철 구간 제거 성공")
    @Test
    void deleteSectionSuccess() {
        // Given
        Map<String, Object> createSectionInfoParam = givenParameterWithSectionCreationInfo();
        createSection(createSectionInfoParam);
        Long stationToDelete = 서초;
        // When
        ExtractableResponse<Response> response = deleteSection(stationToDelete);
        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given: 지하철 노선에 한 개의 구간 만 갖고 있을 때,
     * When: 구간을 제거하면
     * Then: 오류가 발생한다.
     */

    @DisplayName("지하철 구간 제거 실패 - 구간이 1개 이하일 때")
    @Test
    void deleteSectionFail1() {
        // Given
        Long stationToDelete = 방배;
        // When
        ExtractableResponse<Response> response = deleteSection(stationToDelete);
        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given: 제거하려는 구간이 노선의 마지막 구간이 아닐 때,
     * When: 구간을 제거하면
     * Then: 오류가 발생한다.
     */

    @DisplayName("지하철 구간 제거 실패 - 하행 종점역이 아닐 때")
    @Test
    void deleteSectionFail2() {
        // Given
        Map<String, Object> createSectionInfoParam = givenParameterWithSectionCreationInfo();
        createSection(createSectionInfoParam);
        Long stationToDelete = 방배;
        // When
        ExtractableResponse<Response> response = deleteSection(stationToDelete);
        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    Map<String, Object> givenParameterWithSectionCreationInfo() {
        return Map.of("downStationId", 서초, "upStationId", 방배, "distance", 10);
    }

    ExtractableResponse<Response> createStation(String stationName) {
        Map<String, String> station = Map.of("name", stationName);
        return RestAssured.given().log().all()
                          .body(station)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().post("/stations")
                          .then().log().all()
                          .extract();
    }

    Long getStationId(ExtractableResponse<Response> stationCreationResponse) {
        Integer stationId = stationCreationResponse.body().jsonPath().get("id");
        return stationId.longValue();
    }

    ExtractableResponse<Response> createLine(LineRequest lineRequest) {
        return RestAssured.given().log().all()
                          .body(lineRequest)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().post("/lines")
                          .then().log().all()
                          .extract();
    }

    Long getLineId(ExtractableResponse<Response> lineCreationResponse) {
        Integer lineId = lineCreationResponse.body().jsonPath().get("id");
        return lineId.longValue();
    }

    ExtractableResponse<Response> createSection(Map<String, Object> createSectionInfoParam) {
        return RestAssured.given().log().all()
                          .body(createSectionInfoParam)
                          .contentType(MediaType.APPLICATION_JSON_VALUE)
                          .when().post("/lines/" + lineId.toString() + "/sections")
                          .then().log().all()
                          .extract();
    }

    ExtractableResponse<Response> deleteSection(Long stationId) {
        return RestAssured.given().log().all()
                          .when().delete("/lines/" + lineId.toString() + "/sections?stationId=" + stationId)
                          .then().log().all()
                          .extract();
    }
}
