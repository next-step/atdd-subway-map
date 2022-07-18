package nextstep.subway.acceptance;

import io.restassured.response.ValidatableResponse;
import nextstep.SpringBootTestConfig;
import nextstep.subway.acceptance.client.SubwayRestAssured;
import nextstep.subway.acceptance.factory.LineFactory;
import nextstep.subway.domain.Line;
import nextstep.subway.acceptance.enums.SubwayRequestPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;


import static org.hamcrest.Matchers.*;

@DisplayName("지하철 노선 관련 기능")
@Sql("/truncate.sql")
class StationLineAcceptanceTest extends SpringBootTestConfig {
    protected final SubwayRestAssured<Line> lineRestAssured = new SubwayRestAssured<>();
    /**
     * given 노선을 생성하면
     * when 노선이 생성된다.
     * then 목록 조회시 생성된 노선을 확인할 수 있다.
     */
    @DisplayName("노선을 생성한다")
    @Test
    void createLine() {
        String lineRootPath = SubwayRequestPath.LINE.getValue();
        ValidatableResponse postResponse = lineRestAssured.postRequest(lineRootPath, LineFactory.신분당선());

        postResponse.statusCode(equalTo(HttpStatus.CREATED.value()));

        lineRestAssured.getRequest(lineRootPath).assertThat().body("name", contains("신분당선"));
    }


    /** given 2개의 지하철 노선을 생성하고
     *  when 지하철 노선 목록을 조회하면
     *  then 생성된 지하철 노선을 확인할 수 있다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        String lineRootPath = SubwayRequestPath.LINE.getValue();
        lineRestAssured.postRequest(lineRootPath, LineFactory.경의중앙선());
        lineRestAssured.postRequest(lineRootPath, LineFactory.분당선());

        lineRestAssured.getRequest(lineRootPath)
                       .assertThat().body("name", contains("경의중앙선", "분당선"));
    }

    /**
     * given 지하철 노선을 생성하고
     * when 생성된 지하철 노선을 조회하면
     * then 생성된 노선을 응답받을 수 있다
     */
    @DisplayName("지하철 노선 단일 조회")
    @Test
    void getLine() {
        ValidatableResponse 노선_등록결과 = lineRestAssured.postRequest(
                SubwayRequestPath.LINE.getValue(), LineFactory.우이신설());

        String nextLocation = 노선_등록결과.extract().header("Location");

        lineRestAssured.getRequest(nextLocation).assertThat().body("name", equalTo("우이신설"));
    }

    /**
     * given 지하철 노선을 생성하고
     * when 생성된 노선을 수정하면
     * then 변경된 노선을 확인할 수 있다
     */
    @DisplayName("지하철 노선 정보 수정")
    @Test
    void updateLine() {
        ValidatableResponse 노선_등록결과 =
                lineRestAssured.postRequest(SubwayRequestPath.LINE.getValue(), LineFactory.경춘선());

        String lineName = "춘경선";
        String color = "red";
        String nextLocation = 노선_등록결과.extract().header("Location");
        lineRestAssured.putRequest(nextLocation, LineFactory.mock(lineName, color));

        ValidatableResponse 변경된_노선_조회결과 = lineRestAssured.getRequest(nextLocation);
        변경된_노선_조회결과.body("name", equalTo(lineName));
        변경된_노선_조회결과.body("color", equalTo(color));
    }


    /**
     * given 지하철 노선을 생성하고
     * when 생성된 노선을 삭제하면
     * then 해당 노선을 조회할 수 없다
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        String lineRootPath = SubwayRequestPath.LINE.getValue();
        ValidatableResponse 노선_등록결과 =
                lineRestAssured.postRequest(lineRootPath, LineFactory.경춘선());

        String nextLocation = 노선_등록결과.extract().header("Location");
        lineRestAssured.deleteRequest(nextLocation).statusCode(HttpStatus.NO_CONTENT.value());

        lineRestAssured.getRequest(lineRootPath).assertThat().body("id", hasSize(0));
    }
}
