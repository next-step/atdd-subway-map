package nextstep.subway.acceptance;

import io.restassured.response.ValidatableResponse;
import nextstep.SpringBootTestConfig;
import nextstep.subway.acceptance.client.SubwayRestAssured;
import nextstep.subway.domain.Line;
import nextstep.subway.enums.SubwayRequestPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;


import static org.hamcrest.Matchers.*;

@DisplayName("지하철 노선 관련 기능")
@Sql("classpath:/truncate.sql")
class StationLineAcceptanceTest extends SpringBootTestConfig {

    private final SubwayRestAssured<Line> lineRestAssured = new SubwayRestAssured<>();

    /**
     * given 노선을 생성하면
     * when 노선이 생성된다.
     * then 목록 조회시 생성된 노선을 확인할 수 있다.
     */
    @DisplayName("노선을 생성한다")
    @Test
    void createLine() {
        String linePath = SubwayRequestPath.LINE.getValue();
        ValidatableResponse postResponse = lineRestAssured.postRequest(
                linePath, new Line("신분당선", "red", 1L, 3L, 10));

        postResponse.statusCode(equalTo(HttpStatus.CREATED.value()));

        lineRestAssured.getRequest(linePath).assertThat().body("name", contains("신분당선"));
    }


    /** given 2개의 지하철 노선을 생성하고
     *  when 지하철 노선 목록을 조회하면
     *  then 생성된 지하철 노선을 확인할 수 있다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        String linePath = SubwayRequestPath.LINE.getValue();
        lineRestAssured.postRequest(linePath, new Line("경의중앙선", "blue", 3L, 4L, 10));
        lineRestAssured.postRequest(linePath, new Line("분당선", "yellow", 5L, 6L, 10));

        lineRestAssured.getRequest(linePath).assertThat().body("name", contains("경의중앙선", "분당선"));
    }

    /**
     * given 지하철 노선을 생성하고
     * when 생성된 지하철 노선을 조회하면
     * then 생성된 노선을 응답받을 수 있다
     */
    @DisplayName("지하철 노선 단일 조회")
    @Test
    void getLine() {
        ValidatableResponse postResponse = lineRestAssured.postRequest(
                SubwayRequestPath.LINE.getValue(), new Line("우이신설", "gold", 10L, 20L, 10));

        long id = postResponse.extract().jsonPath().getLong("id");

        lineRestAssured.getRequest(SubwayRequestPath.LINE.addPathParam(), id).assertThat().body("name", equalTo("우이신설"));
    }

    /**
     * given 지하철 노선을 생성하고
     * when 생성된 노선을 수정하면
     * then 변경된 노선을 확인할 수 있다
     */
    @DisplayName("지하철 노선 정보 수정")
    @Test
    void updateLine() {
        Line 경춘선 = lineRestAssured.postRequest(
                SubwayRequestPath.LINE.getValue(), new Line("경춘선", "green", 1L, 21L, 11))
                                  .extract().as(Line.class);

        String pathParam = SubwayRequestPath.LINE.addPathParam();
        lineRestAssured.putRequest(pathParam,  경춘선.getId(), new Line("춘경선", "red"));

        ValidatableResponse 변경된_노선 = lineRestAssured.getRequest(pathParam, 경춘선.getId());
        변경된_노선.body("name", equalTo("춘경선"));
        변경된_노선.body("color", equalTo("red"));
    }


    /**
     * given 지하철 노선을 생성하고
     * when 생성된 노선을 삭제하면
     * then 해당 노선을 조회할 수 없다
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        String linePath = SubwayRequestPath.LINE.getValue();
        Line line = lineRestAssured.postRequest(
                                           linePath, new Line("신림선", "blue", 80L, 90L, 12))
                                   .extract().as(Line.class);

        lineRestAssured.deleteRequest(SubwayRequestPath.LINE.addPathParam(), line.getId())
                       .statusCode(HttpStatus.NO_CONTENT.value());

        lineRestAssured.getRequest(linePath).assertThat().body("id", hasSize(0));
    }
}
