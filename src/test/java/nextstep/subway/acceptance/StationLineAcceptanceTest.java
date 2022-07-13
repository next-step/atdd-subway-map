package nextstep.subway.acceptance;


import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import nextstep.subway.acceptance.client.LineRestAssured;
import nextstep.subway.domain.Line;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;


import static org.hamcrest.Matchers.*;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("classpath:/truncate.sql")
public class StationLineAcceptanceTest {
    @LocalServerPort
    private int port;

    private final LineRestAssured lineRestAssured = new LineRestAssured();

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     * given 노선을 생성하면
     * when 노선이 생성된다.
     * then 목록 조회시 생성된 노선을 확인할 수 있다.
     */
    @DisplayName("노선을 생성한다")
    @Test
    void createLine() {
        //given
        ValidatableResponse postResponse = lineRestAssured.postRequest(new Line("신분당선", "red", 1L, 3L, 10));
        //when
        postResponse.statusCode(equalTo(HttpStatus.CREATED.value()));
        //then
        lineRestAssured.getRequest().assertThat().body("name", contains("신분당선"));
    }


    /** given 2개의 지하철 노선을 생성하고
     *  when 지하철 노선 목록을 조회하면
     *  then 생성된 지하철 노선을 확인할 수 있다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        //given
        lineRestAssured.postRequest(new Line("경의중앙선", "blue", 3L, 4L, 10));
        lineRestAssured.postRequest(new Line("분당선", "yellow", 5L, 6L, 10));

        //when, then
        lineRestAssured.getRequest().assertThat().body("name", contains("경의중앙선", "분당선"));
    }

    /**
     * given 지하철 노선을 생성하고
     * when 생성된 지하철 노선을 조회하면
     * then 생성된 노선을 응답받을 수 있다
     */
    @DisplayName("지하철 노선 단일 조회")
    @Test
    void getLine() {
        // given
        ValidatableResponse postResponse =
                lineRestAssured.postRequest(new Line("우이신설", "gold", 10L, 20L, 10));

        // when
        long id = postResponse.extract().jsonPath().getLong("id");

        // then 생성한 지하철 노선 확인
        lineRestAssured.getRequest(id).assertThat().body("name", equalTo("우이신설"));
    }

    /**
     * given 지하철 노선을 생성하고
     * when 생성된 노선을 수정하면
     * then 변경된 노선을 확인할 수 있다
     */
    @DisplayName("지하철 노선 정보 수정")
    @Test
    void updateLine() {
        //given
        Line 경춘선 = lineRestAssured.postRequest(
                new Line("경춘선", "green", 1L, 21L, 11)).extract().as(Line.class);

        //when
        ValidatableResponse putResponse = lineRestAssured.putRequest(new Line("춘경선", "red"), 경춘선.getId());

        //then
        putResponse.body("name", equalTo("춘경선"));
        putResponse.body("color", equalTo("red"));
    }

    /**
     * given 지하철 노선을 생성하고
     * when 생성된 노선을 삭제하면
     * then 해당 노선을 조회할 수 없다
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        //given
        Line line = lineRestAssured.postRequest(
                new Line("신림선", "blue", 80L, 90L, 12)).extract().as(Line.class);

        //when
        lineRestAssured.deleteRequest(line).statusCode(HttpStatus.NO_CONTENT.value());

        //then
        lineRestAssured.getRequest().assertThat().body("id", hasSize(0));
    }


}
