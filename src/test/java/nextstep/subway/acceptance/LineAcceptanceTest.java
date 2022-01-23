package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.utils.DatabaseCleanup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.transaction.Transactional;

import java.util.List;

import static java.util.Objects.isNull;
import static nextstep.subway.acceptance.StationAcceptanceTest.강남역;
import static nextstep.subway.acceptance.StationAcceptanceTest.역삼역;

@DisplayName("지하철 노선 관리 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LineAcceptanceTest extends AcceptanceTest {
    public static final String _2호선 = "2호선";
    public static final String _2호선_COLOR = "bg-green-600";
    public static final String 분당선 = "분당선";
    public static final String 분당선_COLOR = "bg-yellow-600";
    public static final String 신분당선 = "신분당선";
    public static final String 신분당선_COLOR = "bg-red-600";
    public static final String LINE_CONTROLLER_COMMON_PATH = "/lines";

    @Autowired
    DatabaseCleanup databaseCleanup;

    @LocalServerPort
    int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        databaseCleanup.execute();
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(LineRequest lineRequest) {
        return RestAssured
                .given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(LINE_CONTROLLER_COMMON_PATH)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> ID로_지하철_노선_조회(long 노선_id) {
        return RestAssured
                .given().log().all()
                .when().get(LINE_CONTROLLER_COMMON_PATH + "/" + 노선_id)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회() {
        return RestAssured
                .given().log().all()
                .when().get(LINE_CONTROLLER_COMMON_PATH)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정(LineRequest params, int 수정할_노선_id) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(LINE_CONTROLLER_COMMON_PATH + "/" + 수정할_노선_id)
                .then().log().all().extract();
    }

    /**
     * Given 지하철역이 두개 주어지고
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공하고
     * Then 구간이 새로 만들어 진다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // Given
        long 강남역_id = StationAcceptanceTest.지하철역_생성(StationRequest.of(StationAcceptanceTest.강남역))
                .jsonPath().getLong("id");
        long 역삼역_id = StationAcceptanceTest.지하철역_생성(StationRequest.of(역삼역))
                .jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(LineRequest.of(
                _2호선, _2호선_COLOR, 역삼역_id, 강남역_id, 700));

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        Assertions.assertThat(response.jsonPath().getString("name")).isEqualTo(_2호선);
        Assertions.assertThat(isNull(response.jsonPath().get("id"))
                        || isNull(response.jsonPath().get("createdDate"))
                        || isNull(response.jsonPath().get("modifiedDate"))).isFalse();
        Assertions.assertThat(response.jsonPath().getString("color")).isEqualTo(_2호선_COLOR);
        List<Section> sections = response.jsonPath().getList("sections", Section.class);
        Assertions.assertThat(sections.size()).isEqualTo(1);
        Assertions.assertThat(sections.get(0)).isEqualTo(Section.of(
                sections.get(0).getId(),
                Line.of(response.jsonPath().get()),
                Station.of(역삼역_id, 역삼역),
                Station.of(강남역_id, 강남역),
                700
        ));
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
//        지하철_노선_생성_요청(신분당선_LineRequest);
//        지하철_노선_생성_요청(_2호선_LineRequest);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회();
        // then

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.jsonPath().getList(".").size()).isEqualTo(2);
        Assertions.assertThat(response.jsonPath().getList("name")).contains("신분당선", "2호선");
    }


    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
//        long 신분당선_노선_id = 지하철_노선_생성_요청(신분당선_LineRequest).jsonPath().getLong("id");
        long 신분당선_노선_id = 0;

        // when
        ExtractableResponse<Response> response = ID로_지하철_노선_조회(신분당선_노선_id);

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(response.jsonPath().getString("name")).isEqualTo("신분당선");
        Assertions.assertThat(response.jsonPath().getLong("id")).isEqualTo(신분당선_노선_id);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // when
//        Long 노선_id = 지하철_노선_생성_요청(신분당선_LineRequest).jsonPath().getLong("id");
        ExtractableResponse<Response> response = null;
//                지하철_노선_수정(노선_id);

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        ExtractableResponse<Response> 변경된_노선_response = ID로_지하철_노선_조회(0);
        Assertions.assertThat(변경된_노선_response.jsonPath().getString("name")).isEqualTo("구분당선");
        Assertions.assertThat(변경된_노선_response.jsonPath().getString("color")).isEqualTo("bg-blue-600");
    }


    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
//        long 노선_id = 지하철_노선_생성_요청(신분당선_LineRequest).jsonPath().getLong("id");
        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete(LINE_CONTROLLER_COMMON_PATH + "/" + 0)
                .then().log().all().extract();

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        ExtractableResponse<Response> 삭제된_지하철_노선_조회_response = ID로_지하철_노선_조회(0);
        Assertions.assertThat(삭제된_지하철_노선_조회_response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("중복이름으로 지하철 노선 생성")
    @Test
    void createDuplicatedLine() {
        // given
//        지하철_노선_생성_요청(신분당선_LineRequest);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(null);

        // then
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
        Assertions.assertThat(지하철_노선_목록_조회().jsonPath().getList("$").size()).isEqualTo(1);
    }

    @DisplayName("지하철 노선에 구간 제거")
    @Test
    void removeStation() {

    }

    @DisplayName("지하철 노선에 등록된 구간을 통해 역 목록을 조회")
    @Test
    void getStations() {

    }

}
