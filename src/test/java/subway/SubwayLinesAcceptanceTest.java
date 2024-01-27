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
        final ExtractableResponse<Response> subwayLinesGetResponse =findSubwayLines();
        final List<String> subwayLinesNameList = subwayLinesGetResponse.jsonPath().getList("name", String.class);
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
        final ExtractableResponse<Response> subwayLinesGetResponse = findSubwayLines();

        // Then
        final List<String> subwayLinesNameList = subwayLinesGetResponse.jsonPath().getList("name", String.class);
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
        final ExtractableResponse<Response> subwayLineGetResponse = RestAssured
            .given().log().all()
            .when()
                .get("/subwayLines/" + createdLine.getId())
            .then().log().all()
            .extract();
        SubwayLineResponse foundLine = subwayLineGetResponse.as(SubwayLineResponse.class);

        // Then
        assertThat(foundLine.getId()).isEqualTo(createdLine.getId());
        assertThat(foundLine.getName()).isEqualTo(createdLine.getName());
    }



    private static ExtractableResponse<Response> findSubwayLines() {
        final ExtractableResponse<Response> subwayLinesGetResponse = RestAssured
            .given().log().all()
            .when()
                .get("/subwayLines")
            .then().log().all()
            .extract();
        return subwayLinesGetResponse;
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
