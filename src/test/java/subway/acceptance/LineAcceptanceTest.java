package subway.acceptance;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.line.LineRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.acceptance.StationAcceptanceTest.역_생성;
import static subway.fixture.LineFixture.*;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AbstractAcceptanceTest {

    @BeforeEach
    void setUp() {
        역_생성("강남역");
        역_생성("역삼역");
        역_생성("교대역");
        역_생성("양재역");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        노선_생성(신분당선_요청).statusCode(HttpStatus.CREATED.value());

        List<String> 노선_이름_목록 = 노선_이름_목록_조회();
        assertThat(노선_이름_목록).contains(신분당선);
    }

    /**
     * When 존재하지 않는 역을 포함해 생성 요청을 하면
     * Then 404 응답을 받는다.
     */
    @DisplayName("존재하지 않는 역을 포함시켜 지하철 노선을 생성하면 생성되지 않는다.")
    @Test
    void createLineException() {
        var response = 노선_생성(잘못된_노선_요청);

        assertThat(상태_코드_추출(response)).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void showLines() {
        노선_생성(신분당선_요청);
        노선_생성(분당선_요청);

        List<String> 노선_이름_목록 = 노선_이름_목록_조회();

        assertThat(노선_이름_목록)
                .hasSize(2)
                .contains(신분당선, 분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void showLine() {
        var createResponse = 노선_생성(신분당선_요청);
        String location = 리소스_경로_추출(createResponse);

        String 노선_이름 = 노선_이름_조회(location);

        assertThat(노선_이름).isEqualTo(신분당선);
    }

    /**
     * When 존재하지 않는 지하철 노선을 조회하면
     * Then 404 응답을 받는다.
     */
    @DisplayName("존재하지 않는 노선을 조회한다.")
    @Test
    void showLineException() {
        ValidatableResponse response = given()
                    .pathParam("id", 존재하지_않는_노선_ID).
                when()
                    .get("/lines/{id}").
                then().log().all();

        assertThat(상태_코드_추출(response)).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        var createResponse = 노선_생성(신분당선_요청);
        String location = 리소스_경로_추출(createResponse);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(지하철노선_수정_요청).
        when()
                .put(location).
        then().log().all()
                .statusCode(HttpStatus.OK.value());

        assertThat(노선_이름_조회(location)).isEqualTo("다른 분당선");
    }

    /**
     * When 존재하지 않는 지하철 노선을 수정하면
     * Then 404 응답을 받는다.
     */
    @DisplayName("존재하지 않는 노선을 수정하는 경우 수정되지 않는다.")
    @Test
    void updateLineException() {
        var response = given()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .pathParam("id", 존재하지_않는_노선_ID)
                        .body(지하철노선_수정_요청).
                    when()
                         .put("/lines/{id}").
                    then().log().all();

        assertThat(상태_코드_추출(response)).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다.
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        var createResponse = 노선_생성(신분당선_요청);
        String location = 리소스_경로_추출(createResponse);

        given().
        when()
                .delete(location).
        then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(노선_이름_목록_조회()).doesNotContain(신분당선);
    }

    /**
     * When 존재하지 않는 지하철 노선을 삭제하면
     * Then 404 응답을 받는다.
     */
    @DisplayName("존재하지 않는 노선을 삭제한다.")
    @Test
    void deleteLineException() {
        ValidatableResponse response = given()
                                        .pathParam("id", 존재하지_않는_노선_ID).
                                    when()
                                        .delete("/lines/{id}").
                                    then().log().all();

        assertThat(상태_코드_추출(response)).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    public static ValidatableResponse 노선_생성(LineRequest request) {
        return post("/lines", request);
    }

    private List<String> 노선_이름_목록_조회() {
        return get("/lines").extract()
                .jsonPath()
                .getList("name");
    }

    private String 노선_이름_조회(String location) {
        return get(location)
                .extract()
                .jsonPath()
                .getString("name");
    }
}
