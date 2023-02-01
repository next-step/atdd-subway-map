package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.line.fixture.LineFixture;
import subway.station.fixture.StationFixture;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 노선 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    private final static String lineUrlPath = "/lines";
    private final static String stationUrlPath = "/stations";

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        var 지하철_3_호선 = LineFixture.지하철_3_호선;
        var 생성_지하철_노선_응답 = 생성_지하철_노선(지하철_3_호선, StationFixture.연신내, StationFixture.교대, 19);

        // then
        assertThat(생성_지하철_노선_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        var 지하철_노선_이름 = 생성_지하철_노선_응답.body().jsonPath().get("name");
        assertThat(지하철_노선_이름.equals(지하철_3_호선.이름()));
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void readLines() {
        // given
        생성_지하철_노선(LineFixture.지하철_2_호선, StationFixture.교대, StationFixture.선릉, 3);
        생성_지하철_노선(LineFixture.지하철_3_호선, StationFixture.연신내, StationFixture.교대, 19);

        // when
        var 조회_지하철_노선_목록_응답 = 조회_지하철_노선_목록();

        // then
        assertThat(조회_지하철_노선_목록_응답.statusCode()).isEqualTo(HttpStatus.OK.value());

        var 지하철_노선_이름_목록 = 조회_지하철_노선_목록_응답.jsonPath().getList("name", String.class);
        assertThat(지하철_노선_이름_목록).hasSize(2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void readLine() {
        // given
        var 지하철_2_호선 = LineFixture.지하철_2_호선;
        var 지하철_노선_생성_응답 = 생성_지하철_노선(지하철_2_호선, StationFixture.연신내, StationFixture.교대, 19);
        var 생성된_지하철_노선_아이디 = 지하철_노선_생성_응답.jsonPath().getLong("id");

        // when
        var 조회한_지하철_노선_응답 = 조회_지하철_노선(생성된_지하철_노선_아이디);

        // then
        assertThat(조회한_지하철_노선_응답.statusCode()).isEqualTo(HttpStatus.OK.value());

        var 지하철_노선_이름 = 조회한_지하철_노선_응답.jsonPath().get("name");
        assertThat(지하철_노선_이름).isEqualTo(지하철_2_호선.이름());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        //given
        var 지하철_2_호선 = LineFixture.지하철_2_호선;
        var 지하철_3_호선 = LineFixture.지하철_3_호선;

        var 지하철_3_호선_생성_응답 = 생성_지하철_노선(지하철_3_호선, StationFixture.연신내, StationFixture.교대, 19);
        var 지하철_3_호선_아이디 = 지하철_3_호선_생성_응답.jsonPath().getLong("id");

        // when
        var 수정_지하철_노선_응답 = 수정_지하철_노선(지하철_3_호선_아이디, 지하철_2_호선);

        // then
        assertThat(수정_지하철_노선_응답.statusCode()).isEqualTo(HttpStatus.OK.value());

        var 조회_지하철_노선_응답 = 조회_지하철_노선(지하철_3_호선_아이디);
        var 지하철_노선_이름 = 조회_지하철_노선_응답.jsonPath().get("name");
        var 지하철_노선_색상 = 조회_지하철_노선_응답.jsonPath().get("color");

        assertThat(지하철_노선_이름).isEqualTo(지하철_2_호선.이름());
        assertThat(지하철_노선_색상).isEqualTo(지하철_2_호선.색상());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        var 지하철_3_호선 = LineFixture.지하철_3_호선;
        var 지하철_노선_생성_응답 = 생성_지하철_노선(지하철_3_호선, StationFixture.연신내, StationFixture.교대, 19);
        var 생성된_지하철_노선_아이디 = 지하철_노선_생성_응답.jsonPath().getLong("id");

        // when
        var 삭제_지하철_노선_응답 = 삭제_지하철_노선(생성된_지하철_노선_아이디);

        // then
        assertThat(삭제_지하철_노선_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 생성_지하철_노선(LineFixture lineFixture, StationFixture upStation, StationFixture downStation, int distance) {
        Long upStationId = 조회_생성된_지하철역_아이디(StationFixture.연신내);
        Long downStationId = 조회_생성된_지하철역_아이디(StationFixture.교대);

        return RestAssured.given().log().all()
                .body(lineFixture.toMap(upStationId, downStationId, distance))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(lineUrlPath)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 수정_지하철_노선(Long id, LineFixture lineFixture) {
        return RestAssured.given().log().all()
                .body(lineFixture.toMap())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/" + id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 삭제_지하철_노선(Long id) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(lineUrlPath + "/{id}", id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 조회_지하철_노선_목록() {
        return RestAssured.given().log().all()
                .when().get(lineUrlPath)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 조회_지하철_노선(Long id) {
        return RestAssured.given().log().all()
                .when().get(lineUrlPath + "/{id}", id)
                .then().log().all()
                .extract();
    }

    private Long 조회_생성된_지하철역_아이디(StationFixture station) {
        return 생성_지하철역(station)
                .body()
                .jsonPath().getLong("id");
    }

    private ExtractableResponse<Response> 생성_지하철역(StationFixture station) {
        return RestAssured.given().log().all()
                .body(station.toMap())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(stationUrlPath)
                .then().log().all()
                .extract();
    }

}