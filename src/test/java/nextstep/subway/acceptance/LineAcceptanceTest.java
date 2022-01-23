package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.Stations;
import nextstep.subway.exception.NotFoundStationException;
import nextstep.subway.exception.OutOfSectionDistanceException;
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

import java.util.Arrays;
import java.util.List;

import static java.util.Objects.isNull;
import static nextstep.subway.acceptance.StationAcceptanceTest.강남역;
import static nextstep.subway.acceptance.StationAcceptanceTest.역삼역;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

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
                _2호선, _2호선_COLOR, 역삼역_id, 강남역_id, 7));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo(_2호선);
        assertThat(isNull(response.jsonPath().get("id"))
                        || isNull(response.jsonPath().get("createdDate"))
                        || isNull(response.jsonPath().get("modifiedDate"))).isFalse();
        assertThat(response.jsonPath().getString("color")).isEqualTo(_2호선_COLOR);
        List<Station> stations = response.jsonPath().get("stations");
        assertThat(stations.size()).isEqualTo(2);
    }

    /**
     * Given 존재하지 않는 지하철역으로
     * When 지하철 노선 생성 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("지하철 노선 생성 시, 존재하지 않는 지하철역을 등록")
    @Test
    void createLineAsInvalidStation() {
        // Given
        long invalidUpStationId = 1;
        long invalidDownStationId = 2;

        // When
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(LineRequest.of(_2호선, _2호선_COLOR, invalidUpStationId, invalidDownStationId, 7));

        // Then
        // NotFoundStationException 이라는 커스텀 에러를 catch 할 수 있으면 좋을텐데 RestAssured 로는 해당 에러를 분별하려면
        // 에러 코드를 정의해야 할까요? ...
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Given 지하철역이 두개 주어지고
     * Given 노선의 distance 를 1 미만 으로 주어질 때
     * When 지하철 노선 생성 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("지하철 노선 생성 시, 노선의 distance 를 1 미만 으로 입력")
    @Test
    void createLineAsInvalidDistance() {
        // Given
        long 강남역_id = StationAcceptanceTest.지하철역_생성(StationRequest.of(StationAcceptanceTest.강남역))
                .jsonPath().getLong("id");
        long 역삼역_id = StationAcceptanceTest.지하철역_생성(StationRequest.of(역삼역))
                .jsonPath().getLong("id");

        ExtractableResponse<Response> response = 지하철_노선_생성_요청(LineRequest.of(
                _2호선, _2호선_COLOR, 역삼역_id, 강남역_id, 0));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Given 동일한 상행/하행 지하철역의 Id 가 주어질 때
     * When 지하철 노선 생성 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("지하철 노선 생성 시, 동일한 상행/하행 지하철역의 Id 입력")
    @Test
    void createLineAsSameUpAndDownStation() {

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

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".").size()).isEqualTo(2);
        assertThat(response.jsonPath().getList("name")).contains("신분당선", "2호선");
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
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo("신분당선");
        assertThat(response.jsonPath().getLong("id")).isEqualTo(신분당선_노선_id);
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
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        ExtractableResponse<Response> 변경된_노선_response = ID로_지하철_노선_조회(0);
        assertThat(변경된_노선_response.jsonPath().getString("name")).isEqualTo("구분당선");
        assertThat(변경된_노선_response.jsonPath().getString("color")).isEqualTo("bg-blue-600");
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
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        ExtractableResponse<Response> 삭제된_지하철_노선_조회_response = ID로_지하철_노선_조회(0);
        assertThat(삭제된_지하철_노선_조회_response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
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
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(지하철_노선_목록_조회().jsonPath().getList("$").size()).isEqualTo(1);
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
