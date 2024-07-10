package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.test.AcceptanceTestBase;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철노선 관련 기능")
public class SubwayLineAcceptanceTest extends AcceptanceTestBase {
    public static final String LINE_SINBUNDANG = "신분당선";
    public static final String LINE_ONE = "1호선";
    public static final String LINE_TWO = "2호선";
    public static final String COLOR_RED = "bg-red-001";
    public static final String COLOR_BLUE = "bg-blue-001";

    /*
     * given 지하철 정보를 입력하고
     * when 지하철역 노선을 생성하면
     * /then 지하철역 노선이 생성된다
     */
    @DisplayName("지하철 노선을 생성한다")
    @Test
    void createSubwayLine() {
        //when
        var createdResponse = requestCreateSubwayLine(LINE_SINBUNDANG, COLOR_RED);

        //then
        assertThat(createdResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(extractName(createdResponse)).isEqualTo(LINE_SINBUNDANG);
    }

    /**
     * given 지하철노선이 3개일때
     * when 지하철역 노선목록을 조회하면
     * then 지하철역 노선3개가 조회된다
     */

    @DisplayName("지하철 노선 목록을 조회한다")
    @Test
    void getAllSubwayLine() {
        //given
        requestCreateSubwayLine(LINE_SINBUNDANG, COLOR_RED);
        requestCreateSubwayLine(LINE_ONE, COLOR_RED);
        requestCreateSubwayLine(LINE_TWO, COLOR_RED);

        //when
        var getAllResponse = requestGetAllSubwayLine();

        //then
        assertThat(getAllResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(extractIds(getAllResponse).size()).isEqualTo(3);
    }

    /**
     * given 지하철역 노선이 등록되어있는 경우
     * when 해당 지하철역 노선을 조회한다
     * then: 지하철역 노선이 조회된다
     */

    @DisplayName("지하철 노선을 조회한다")
    @Test
    void getSubwayLine() {
        //given
        var createdResponse = requestCreateSubwayLine(LINE_SINBUNDANG, COLOR_RED);
        var createdId = createdResponse.jsonPath().getLong("id");

        //when
        var getResponse = requestGetSubwayLine(createdId);

        //then
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(extractName(getResponse)).isEqualTo(LINE_SINBUNDANG);
    }

    /**
     * given: 노선이 등록된 경우
     * when: 노선의 이름과 색을 수정한다
     * then: 이름과 색이 수정된다
     */
    @DisplayName("지하철 노선의 이름과 색을 수정한다")
    @Test
    void updateSubwayLine() {
        //given
        var createdResponse = requestCreateSubwayLine(LINE_SINBUNDANG, COLOR_RED);
        var createdId = createdResponse.jsonPath().getLong("id");

        //when
        var updatedResponse = requestUpdateSubwayLine(createdId, LINE_ONE, COLOR_BLUE);

        //then
        assertThat(updatedResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        var getResponse = requestGetSubwayLine(createdId);
        assertThat(extractName(getResponse)).isEqualTo(LINE_ONE);
        assertThat(extractColor(getResponse)).isEqualTo(COLOR_BLUE);
    }

    /**
     * given: 노선이 등록된 경우
     * when: 노선을 삭제한다
     * then: 노선이 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다")
    @Test
    void deleteSubwayLine() {
        //given
        var createdResponse = requestCreateSubwayLine(LINE_SINBUNDANG, COLOR_RED);
        var createdId = createdResponse.jsonPath().getLong("id");

        //when
        var deletedResponse = requestDeleteSubwayLine(createdId);

        //then
        assertThat(deletedResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        var getAllResponse = requestGetAllSubwayLine();
        assertThat(extractIds(getAllResponse)).doesNotContain(createdId);
    }

    private ExtractableResponse<Response> requestCreateSubwayLine(String name, String color) {
        var body = Map.of("name", name,
                "color", color,
                "upStationId", 10L,
                "downStationId", 1L,
                "distance", 10
        );
        var contentType = MediaType.APPLICATION_JSON_VALUE;
        var path = "/lines";

        return RestAssured.given().log().all()
                .body(body)
                .contentType(contentType)
                .when().post(path)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> requestUpdateSubwayLine(Long id, String name, String color) {
        var body = Map.of(
                "name", name,
                "color", color
        );
        var contentType = MediaType.APPLICATION_JSON_VALUE;

        return RestAssured
                .given()
                .pathParam("id", id)
                .body(body)
                .contentType(contentType)
                .when()
                .put("/lines/{id}")
                .then()
                .extract();
    }

    private ExtractableResponse<Response> requestGetAllSubwayLine() {
        return RestAssured
                .when().get("/lines")
                .then()
                .extract();
    }

    private ExtractableResponse<Response> requestGetSubwayLine(Long id) {
        return RestAssured
                .given()
                .pathParam("id", id)
                .when()
                .get("/lines/{id}")
                .then()
                .extract();
    }

    private ExtractableResponse<Response> requestDeleteSubwayLine(Long id) {
        return RestAssured
                .given()
                .pathParam("id", id)
                .when()
                .delete("/lines/{id}")
                .then()
                .extract();
    }

    private String extractName(ExtractableResponse<Response> response) {
        return response.jsonPath().getString("name");
    }

    private String extractColor(ExtractableResponse<Response> response) {
        return response.jsonPath().getString("color");
    }

    private List<Long> extractIds(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("id", Long.class);
    }
}
