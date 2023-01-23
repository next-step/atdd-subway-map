package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class StationAcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        final String stationName = "강남역";
        final ExtractableResponse<Response> response = 지하철역을_생성한다(stationName);
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        final List<String> stationNames = 지하철역_이름들을_조회한다();
        assertThat(stationNames).containsAnyOf(stationName);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철역 목록 조회")
    @Test
    void 지하철역_목록_조회() {
        final List<String> stationNames = List.of("강남역", "잠실역");
        stationNames.forEach(it -> 지하철역을_생성한다(it));

        final List<String> stationNameResponses = 지하철역_이름들을_조회한다();

        assertAll(
            () -> assertThat(stationNameResponses.size()).isEqualTo(stationNames.size()),
            () -> assertThat(stationNameResponses).containsAll(stationNames)
        );
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철역 제거")
    @Test
    void 지하철역_제거() {
        final String 강남역 = "강남역";
        final String 잠실역 = "잠실역";

        final List<String> stationNames = List.of(강남역, 잠실역);
        final List<StationResponse> stationResponses = stationNames.stream()
            .map(it -> 지하철역을_생성한다(it))
            .map(it -> it.body().as(StationResponse.class))
            .collect(Collectors.toList());

        final Long 강남역Id = stationResponses.stream()
            .filter(it -> 강남역.equals(it.getName()))
            .map(StationResponse::getId)
            .findFirst()
            .orElseThrow();

        final ExtractableResponse<Response> deleteResponse = 지하철역을_삭제한다(강남역Id);

        final List<String> stationNameResponses = 지하철역_이름들을_조회한다();

        assertAll(
            () -> assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
            () -> assertThat(stationNameResponses.size()).isNotEqualTo(stationNames.size()),
            () -> assertThat(stationNameResponses).doesNotContain(강남역),
            () -> assertThat(stationNameResponses).contains(잠실역)
        );
    }

    private ExtractableResponse<Response> 지하철역을_생성한다(String stationName) {
        return RestAssured.given().log().all()
            .body(Map.of("name", stationName))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/stations")
            .then().log().all()
            .extract();
    }

    private List<String> 지하철역_이름들을_조회한다() {
        return RestAssured.given().log().all()
            .when().get("/stations")
            .then().log().all()
            .extract().jsonPath().getList("name", String.class);
    }

    private ExtractableResponse<Response> 지하철역을_삭제한다(Long stationId) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/stations/" + stationId)
            .then().log().all()
            .extract();
    }

}
