package subway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.ARRAY;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.springframework.test.context.jdbc.Sql;

@Sql(value="/db/subwayLine.sql")
@DisplayName("지하철 노선 기능")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class SubwayLinesAcceptanceTest {
    private String 일호선 = "일호선";
    private String 이호선 = "이호선";

    private String 빨간색 = "bg-red-600";

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DirtiesContext
    @Test
    void 지하철노선_생성() {
        // When
        final ExtractableResponse<Response> response = createSubwayLine(일호선, 빨간색, 1L, 2L, 10);

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // Then
        final ExtractableResponse<Response> subwayLineResponse = findSubwayLine(response.as(SubwayLineResponse.class));
        final SubwayLineResponse subwayLine = subwayLineResponse.as(SubwayLineResponse.class);

        assertThat(subwayLine.getName()).containsAnyOf(일호선);
        assertThat(subwayLine.getStations().stream().map(StationResponse::getId)).contains(1L, 2L);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DirtiesContext
    @Test
    void 지하철노선_목록_조회() {
        // Given
        createSubwayLine(일호선, 빨간색, 1L, 2L, 10);
        createSubwayLine(이호선, 빨간색, 1L, 2L, 10);

        // When
        final SubwayLineResponse[] subwayLineList = findSubwayLines();
        final List<String> subwayLineNameList = Arrays.stream(subwayLineList).map(
            SubwayLineResponse::getName).collect(
            Collectors.toList());

        // Then
        assertThat(subwayLineNameList).contains(일호선, 이호선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DirtiesContext
    @Test
    void 지하철노선_조회() {
        // Given
        final ExtractableResponse<Response> createdLineResponse = createSubwayLine(일호선, 빨간색, 1L, 2L, 10);
        SubwayLineResponse createdLine = createdLineResponse.as(SubwayLineResponse.class);

        // When
        final ExtractableResponse<Response> subwayLineGetResponse = findSubwayLine(
            createdLine);
        SubwayLineResponse foundLine = subwayLineGetResponse.as(SubwayLineResponse.class);

        // Then
        assertThat(foundLine.getId()).isEqualTo(createdLine.getId());
        assertThat(foundLine.getName()).isEqualTo(createdLine.getName());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DirtiesContext
    @Test
    void 지하철노선_수정() {
        // Given
        final ExtractableResponse<Response> createdLineResponse = createSubwayLine(일호선, 빨간색, 1L, 2L, 10);
        final SubwayLineResponse createdLine = createdLineResponse.as(SubwayLineResponse.class);

        // When
        updateSubwayLine(createdLine, 이호선);

        // Then
        final ExtractableResponse<Response> updatedLineResponse = findSubwayLine(createdLine);
        final SubwayLineResponse updatedResponse = updatedLineResponse.as(SubwayLineResponse.class);

        assertThat(updatedResponse.getId()).isEqualTo(createdLine.getId());
        assertThat(updatedResponse.getName()).isEqualTo(이호선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DirtiesContext
    @Test
    void 지하철노선_삭제() {
        // Given
        final ExtractableResponse<Response> createdLineResponse = createSubwayLine(일호선, 빨간색, 1L, 2L, 10);
        final SubwayLineResponse createdLine = createdLineResponse.as(SubwayLineResponse.class);

        // When
        deleteSubwayLine(createdLine);

        // Then
        final SubwayLineResponse[] subwayLineList = findSubwayLines();
        final List<String> subwayLineNameList = Arrays.stream(subwayLineList).map(
            SubwayLineResponse::getName).collect(
            Collectors.toList());
        assertThat(subwayLineNameList).doesNotContain(일호선);
    }

    private static void deleteSubwayLine(SubwayLineResponse createdLine) {
        RestAssured
            .given().log().all()
            .when()
            .delete("/subwayLines/" + createdLine.getId())
            .then().log().all();
    }

    private void updateSubwayLine(SubwayLineResponse createdLine, String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        RestAssured
            .given().log().all()
            .when()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .put("/subwayLines/"+ createdLine.getId())
            .then().log().all()
            .extract();
    }

    private static ExtractableResponse<Response> findSubwayLine(SubwayLineResponse createdLine) {
        return RestAssured
            .given().log().all()
            .when()
            .get("/subwayLines/" + createdLine.getId())
            .then().log().all()
            .extract();
    }

    private static SubwayLineResponse[] findSubwayLines() {
        return RestAssured
            .given().log().all()
            .when()
                .get("/subwayLines")
            .then().log().all()
            .extract().as(SubwayLineResponse[].class);
    }

    private ExtractableResponse<Response> createSubwayLine(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId.toString());
        params.put("downStationId", downStationId.toString());
        params.put("distance", distance.toString());

        // when
        final ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/subwayLines")
            .then().log().all()
            .extract();
        return response;
    }
}
