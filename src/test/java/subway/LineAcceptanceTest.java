package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.line.LineRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
    @BeforeEach
    public void makeStations() {
        StationAcceptanceTest.makeStation("gangnam");
        StationAcceptanceTest.makeStation("yeoksam");
        StationAcceptanceTest.makeStation("samseong");
    }

    /**
     * when 지하철 노선을 생성하면
     * then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> newLineResponse = RestAssured
                .given().log().all()
                .when()
                .body(new LineRequest(
                        "신분당선",
                        "bg-red-600",
                        1L,
                        2L,
                        10L))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        // then
        ExtractableResponse<Response> linesResponse = RestAssured
                .given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        List<Long> ids = linesResponse.jsonPath().getList("id", Long.class);
        assertThat(ids).contains(newLineResponse.jsonPath().getLong("id"));
    }
}
