package subway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.ARRAY;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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

@DirtiesContext
@DisplayName("지하철 노선 기능")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class SubwayLinesAcceptanceTest {
    String 일호선 = "일호선";
    String 이호선 = "이호선";

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Test
    void createSubwayLineTest() {
        // When
        final ExtractableResponse<Response> response = createSubwayLine(일호선);

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // Then
        final List<String> subwayLinesNameList = findSubwayLines();
        assertThat(subwayLinesNameList).containsAnyOf(일호선);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @Test
    void 지하철노선_목록_조회() {
        // Given
        createSubwayLine(일호선);
        createSubwayLine(이호선);

        // When
        final List<String> subwayLinesNameList = findSubwayLines();

        // Then
        assertThat(subwayLinesNameList).contains(일호선, 이호선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    void 지하철노선_조회() {
        // Given
        final ExtractableResponse<Response> createdLineResponse = createSubwayLine(일호선);
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
    @Test
    void 지하철노선_수정() {
        // Given
        final ExtractableResponse<Response> createdLineResponse = createSubwayLine(일호선);
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
    @Test
    void 지하철노선_삭제() {
        // Given
        final ExtractableResponse<Response> createdLineResponse = createSubwayLine(일호선);
        final SubwayLineResponse createdLine = createdLineResponse.as(SubwayLineResponse.class);

        // When
        deleteSubwayLine(createdLine);

        // Then
        final List<String> subwayLinesNameList = findSubwayLines();
        assertThat(subwayLinesNameList).doesNotContain(일호선);
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

    private static List<String> findSubwayLines() {
        return RestAssured
            .given().log().all()
            .when()
                .get("/subwayLines")
            .then().log().all()
            .extract().jsonPath().getList("name", String.class);
    }

    private ExtractableResponse<Response> createSubwayLine(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

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
