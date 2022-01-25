package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.SectionRequest;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.Stations;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static nextstep.subway.acceptance.StationAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

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

    public static ExtractableResponse<Response> 지하철_노선_수정(LineRequest params, long 수정할_노선_id) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(LINE_CONTROLLER_COMMON_PATH + "/" + 수정할_노선_id)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_구간_등록(long upStationId, long 노선_id, long downStationId) {
        return RestAssured
                .given().log().all()
                .body(SectionRequest.of(upStationId, downStationId, 10))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(LINE_CONTROLLER_COMMON_PATH + "/" + 노선_id + "/sections")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_구간_삭제(long lineId, long 삭제할_역_id) {
        return RestAssured
                .given().log().all()
                .when().delete(LINE_CONTROLLER_COMMON_PATH + "/" + lineId + "/sections?stationId=" + 삭제할_역_id)
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
        long 강남역_id = 지하철역_생성(StationRequest.of(강남역))
                .jsonPath().getLong("id");
        long 역삼역_id = 지하철역_생성(StationRequest.of(역삼역))
                .jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(LineRequest.of(_2호선, _2호선_COLOR, 강남역_id, 역삼역_id, 7));

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
        long 강남역_id = 지하철역_생성(StationRequest.of(강남역))
                .jsonPath().getLong("id");
        long 역삼역_id = 지하철역_생성(StationRequest.of(역삼역))
                .jsonPath().getLong("id");

        ExtractableResponse<Response> response = 지하철_노선_생성_요청(LineRequest.of(
                _2호선, _2호선_COLOR, 강남역_id, 역삼역_id, 0));
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
        // Given
        long 강남역_id = 지하철역_생성(StationRequest.of(강남역))
                .jsonPath().getLong("id");

        ExtractableResponse<Response> response = 지하철_노선_생성_요청(LineRequest.of(
                _2호선, _2호선_COLOR, 강남역_id, 강남역_id, 7));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
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
        long 강남역_id = 지하철역_생성(StationRequest.of(강남역))
                .jsonPath().getLong("id");
        long 역삼역_id = 지하철역_생성(StationRequest.of(역삼역))
                .jsonPath().getLong("id");
        long 경기중앙역_id = 지하철역_생성(StationRequest.of(경기중앙역))
                .jsonPath().getLong("id");

        // when
        지하철_노선_생성_요청(LineRequest.of(신분당선, 신분당선_COLOR, 경기중앙역_id, 강남역_id, 30));
        // when
        지하철_노선_생성_요청(LineRequest.of(_2호선, _2호선_COLOR, 강남역_id, 역삼역_id, 7));

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
        long 강남역_id = 지하철역_생성(StationRequest.of(강남역))
                .jsonPath().getLong("id");
        long 경기중앙역_id = 지하철역_생성(StationRequest.of(경기중앙역))
                .jsonPath().getLong("id");

        // when
        long 신분당선_노선_id = 지하철_노선_생성_요청(LineRequest.of(신분당선, 신분당선_COLOR, 경기중앙역_id, 강남역_id, 30))
                .jsonPath().getLong("id");

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
        // Given
        long 강남역_id = 지하철역_생성(StationRequest.of(강남역))
                .jsonPath().getLong("id");
        long 경기중앙역_id = 지하철역_생성(StationRequest.of(경기중앙역))
                .jsonPath().getLong("id");

        Long 신분당선_노선_id = 지하철_노선_생성_요청(LineRequest.of(신분당선, 신분당선_COLOR, 경기중앙역_id, 강남역_id, 30))
                .jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(LineRequest.of(분당선, 분당선_COLOR))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(LINE_CONTROLLER_COMMON_PATH + "/" + 신분당선_노선_id)
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        ExtractableResponse<Response> 변경된_노선_response = ID로_지하철_노선_조회(신분당선_노선_id);
        assertThat(변경된_노선_response.jsonPath().getString("name")).isEqualTo(분당선);
        assertThat(변경된_노선_response.jsonPath().getString("color")).isEqualTo(분당선_COLOR);
    }


    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        long 강남역_id = 지하철역_생성(StationRequest.of(강남역))
                .jsonPath().getLong("id");
        long 경기중앙역_id = 지하철역_생성(StationRequest.of(경기중앙역))
                .jsonPath().getLong("id");
        long 신분당선_노선_id = 지하철_노선_생성_요청(LineRequest.of(신분당선, 신분당선_COLOR, 경기중앙역_id, 강남역_id, 30))
                .jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete(LINE_CONTROLLER_COMMON_PATH + "/" + 신분당선_노선_id)
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        ExtractableResponse<Response> 삭제된_지하철_노선_조회_response = ID로_지하철_노선_조회(신분당선_노선_id);
        assertThat(삭제된_지하철_노선_조회_response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("중복이름으로 지하철 노선 생성")
    @Test
    void createDuplicatedLine() {

        // Given
        long 강남역_id = 지하철역_생성(StationRequest.of(강남역))
                .jsonPath().getLong("id");
        long 경기중앙역_id = 지하철역_생성(StationRequest.of(경기중앙역))
                .jsonPath().getLong("id");
        지하철_노선_생성_요청(LineRequest.of(신분당선, 신분당선_COLOR, 경기중앙역_id, 강남역_id, 30))
                .jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(LineRequest.of(신분당선, 신분당선_COLOR, 경기중앙역_id, 강남역_id, 30));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(지하철_노선_목록_조회().jsonPath().getList("$").size()).isEqualTo(1);
    }

    /**
     * given 상행과 하행역이 있는 지하철의 노선이 주어지고
     * given 새로운 상행종점을 등록할 때, 등록할 구간의 하행역은, 기존 노선의 상행 종점이다
     * when 지하철 노선에 구간을 등록 요청을 하면
     * then 지하철 노선에 구간을 등록이 성공한다
     */
    @DisplayName("지하철 노선에 상행역 종점 구간 등록")
    @Test
    void addSectionToLineUpStation() {
        // given
        long 강남역_id = 지하철역_생성(StationRequest.of(강남역))
                .jsonPath().getLong("id");
        long 역삼역_id = 지하철역_생성(StationRequest.of(역삼역))
                .jsonPath().getLong("id");
        long _2호선_Line_id = 지하철_노선_생성_요청(LineRequest.of(_2호선, _2호선_COLOR, 강남역_id, 역삼역_id, 7))
                .jsonPath().getLong("id");

        // given
        long 교대역_id = 지하철역_생성(StationRequest.of(교대역))
                .jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 지하철_노선에_구간_등록(교대역_id, _2호선_Line_id, 강남역_id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(ID로_지하철_노선_조회(_2호선_Line_id).jsonPath().getList("stations.id").contains((int) 교대역_id)).isTrue();
    }

    /**
     * given 상행과 하행역이 있는 지하철의 노선이 주어지고
     * given 새로운 하행종점을 등록할 때, 구간의 상행역은, 기존 노선의 하행 종점이다
     * when 지하철 노선에 구간을 등록 요청을 하면
     * then 지하철 노선에 구간을 등록이 성공한다
     */
    @DisplayName("지하철 노선에 하행역 종점 구간 등록")
    @Test
    void addSectionToLineDownStation() {

        // given
        long 강남역_id = 지하철역_생성(StationRequest.of(강남역))
                .jsonPath().getLong("id");
        long 역삼역_id = 지하철역_생성(StationRequest.of(역삼역))
                .jsonPath().getLong("id");
        long _2호선_Line_id = 지하철_노선_생성_요청(LineRequest.of(_2호선, _2호선_COLOR, 강남역_id, 역삼역_id, 7))
                .jsonPath().getLong("id");

        // given
        long 선릉역_id = 지하철역_생성(StationRequest.of(선릉역))
                .jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 지하철_노선에_구간_등록(역삼역_id, _2호선_Line_id, 선릉역_id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(ID로_지하철_노선_조회(_2호선_Line_id).jsonPath().getList("stations.id")).contains((int) 선릉역_id);
    }

    /**
     * given 상행과 하행역이 있는 지하철의 노선이 주어지고
     * given 새로운 하행종점을 등록할 때, 현재 등록되어있는 역이면
     * when 지하철 노선에 구간을 등록 요청을 하면
     * then 지하철 노선에 구간을 등록이 실패한다
     */
    @DisplayName("지하철 노선에 하행역 종점 구간 등록시, 이미 등록된 역을 하행역으로 등록")
    @Test
    void addSectionToLineExistedDownStation() {
        // given
        long 강남역_id = 지하철역_생성(StationRequest.of(강남역))
                .jsonPath().getLong("id");
        long 역삼역_id = 지하철역_생성(StationRequest.of(역삼역))
                .jsonPath().getLong("id");
        long _2호선_Line_id = 지하철_노선_생성_요청(LineRequest.of(_2호선, _2호선_COLOR, 강남역_id, 역삼역_id, 7))
                .jsonPath().getLong("id");

        // given
        long 선릉역_id = 지하철역_생성(StationRequest.of(선릉역))
                .jsonPath().getLong("id");
        지하철_노선에_구간_등록(역삼역_id, _2호선_Line_id, 선릉역_id);

        // when
        ExtractableResponse<Response> response = 지하철_노선에_구간_등록(선릉역_id, _2호선_Line_id, 역삼역_id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * given 상행과 하행역이 있는 지하철의 노선이 주어지고
     * given 새로운 상행종점을 등록할 때, 현재 등록되어있는 역이면
     * when 지하철 노선에 구간을 등록 요청을 하면
     * then 지하철 노선에 구간을 등록이 실패한다
     */
    @DisplayName("지하철 노선에 상행역 종점 구간 등록, 이미 등록된 역을 상행역으로 등록")
    @Test
    void addSectionToLineExistedUpStation() {
        // given
        long 강남역_id = 지하철역_생성(StationRequest.of(강남역))
                .jsonPath().getLong("id");
        long 역삼역_id = 지하철역_생성(StationRequest.of(역삼역))
                .jsonPath().getLong("id");
        long _2호선_Line_id = 지하철_노선_생성_요청(LineRequest.of(_2호선, _2호선_COLOR, 강남역_id, 역삼역_id, 7))
                .jsonPath().getLong("id");

        // given
        long 교대역_id = 지하철역_생성(StationRequest.of(교대역))
                .jsonPath().getLong("id");
        지하철_노선에_구간_등록(교대역_id, _2호선_Line_id, 강남역_id);

        // when
        ExtractableResponse<Response> response = 지하철_노선에_구간_등록(역삼역_id, _2호선_Line_id, 교대역_id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * given 2개의 구간이 있는 지하철 노선이 존재하고
     * given 상행 종점이 주어질 때
     * when 구간 제거를 한다.
     * then 구간 제거가 성공한다.
     * then 노선에 역이 사라진다.
     */
    @DisplayName("지하철 노선에 구간 제거, 상행종점 구간 제거")
    @Test
    void removeUpStation() {

        // given
        long 강남역_id = 지하철역_생성(StationRequest.of(강남역)).jsonPath().getLong("id");
        long 역삼역_id = 지하철역_생성(StationRequest.of(역삼역)).jsonPath().getLong("id");
        long 교대역_id = 지하철역_생성(StationRequest.of(교대역)).jsonPath().getLong("id");
        long _2호선_Line_id = 지하철_노선_생성_요청(LineRequest.of(_2호선, _2호선_COLOR, 강남역_id, 역삼역_id, 7)).jsonPath().getLong("id");
        지하철_노선에_구간_등록(교대역_id, _2호선_Line_id, 강남역_id);

        // when
        ExtractableResponse<Response> response = 지하철_노선에_구간_삭제(_2호선_Line_id, 교대역_id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(ID로_지하철_노선_조회(_2호선_Line_id).jsonPath().getList("stations.id").contains((int) 교대역_id)).isFalse();
    }

    /**
     * given 2개의 구간이 있는 지하철 노선이 존재하고
     * given 하행 종점이 주어질 때
     * when 구간 제거를 한다.
     * then 구간 제거가 성공한다.
     * then 하행 종점이 바뀐다.
     */
    @DisplayName("지하철 노선에 구간 제거, 하행종점 구간 제거")
    @Test
    void removeDownStation() {
        // given
        long 강남역_id = 지하철역_생성(StationRequest.of(강남역)).jsonPath().getLong("id");
        long 역삼역_id = 지하철역_생성(StationRequest.of(역삼역)).jsonPath().getLong("id");
        long 교대역_id = 지하철역_생성(StationRequest.of(교대역)).jsonPath().getLong("id");
        long _2호선_Line_id = 지하철_노선_생성_요청(LineRequest.of(_2호선, _2호선_COLOR, 강남역_id, 역삼역_id, 7)).jsonPath().getLong("id");
        지하철_노선에_구간_등록(교대역_id, _2호선_Line_id, 강남역_id);

        // when
        ExtractableResponse<Response> response = 지하철_노선에_구간_삭제(_2호선_Line_id, 역삼역_id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(ID로_지하철_노선_조회(_2호선_Line_id).jsonPath().getList("stations.id").contains((int) 역삼역_id)).isFalse();
    }

    /**
     * given 1개의 구간이 있는 지하철 노선이 존재하고
     * when 구간 제거를 한다.
     * then 구간 제거가 실패한다.
     */
    @DisplayName("지하철 노선에 구간 제거, 구간이 1개 일 때")
    @Test
    void removeBiStation() {
        // given
        long 강남역_id = 지하철역_생성(StationRequest.of(강남역)).jsonPath().getLong("id");
        long 역삼역_id = 지하철역_생성(StationRequest.of(역삼역)).jsonPath().getLong("id");
        long _2호선_Line_id = 지하철_노선_생성_요청(LineRequest.of(_2호선, _2호선_COLOR, 강남역_id, 역삼역_id, 7)).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 지하철_노선에_구간_삭제(_2호선_Line_id, 역삼역_id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * given 2개의 구간이 있는 지하철 노선이 존재하고
     * given 존재하지 않는 역이 주어진다.
     * when 구간 제거를 한다.
     * then 구간 제거가 실패한다.
     */
    @DisplayName("지하철 노선에 구간 제거, 구간이 1개 일 때")
    @Test
    void removeNotExistedStation() {
        // given
        long 강남역_id = 지하철역_생성(StationRequest.of(강남역)).jsonPath().getLong("id");
        long 역삼역_id = 지하철역_생성(StationRequest.of(역삼역)).jsonPath().getLong("id");
        long 교대역_id = 지하철역_생성(StationRequest.of(교대역)).jsonPath().getLong("id");
        long _2호선_Line_id = 지하철_노선_생성_요청(LineRequest.of(_2호선, _2호선_COLOR, 강남역_id, 역삼역_id, 7)).jsonPath().getLong("id");
        지하철_노선에_구간_등록(교대역_id, _2호선_Line_id, 강남역_id);

        // when
        ExtractableResponse<Response> response = 지하철_노선에_구간_삭제(_2호선_Line_id, 9999);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 노선에 등록된 구간을 순서대로 정렬하여 상행 종점부터 하행 종점까지 목록을 응답하기
     */
    @DisplayName("등록된 구간을 통해 역 목록 조회")
    @Test
    void getLinesSortedStation() {
        long 강남역_id = 지하철역_생성(StationRequest.of(강남역)).jsonPath().getLong("id");
        long 역삼역_id = 지하철역_생성(StationRequest.of(역삼역)).jsonPath().getLong("id");
        long 경기중앙역_id = 지하철역_생성(StationRequest.of(경기중앙역)).jsonPath().getLong("id");
        long 선릉역_id = 지하철역_생성(StationRequest.of(선릉역)).jsonPath().getLong("id");

        long 신분당선_노선_id = 지하철_노선_생성_요청(LineRequest.of(신분당선, 신분당선_COLOR, 경기중앙역_id, 강남역_id, 30))
                .jsonPath().getLong("id");
        long _2호선_노선_id = 지하철_노선_생성_요청(LineRequest.of(_2호선, _2호선_COLOR, 강남역_id, 역삼역_id, 10))
                .jsonPath().getLong("id");

        지하철_노선에_구간_등록(역삼역_id, _2호선_노선_id, 선릉역_id);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<LineResponse> lines = response.jsonPath().getList(".", LineResponse.class);
        assertThat(lines.size()).isEqualTo(2);
        Map<Long, LineResponse> lineIdToLine = lines.stream()
                .collect(Collectors.toMap(LineResponse::getId, Function.identity()));

        assertThat(Stations.of(lineIdToLine.get(신분당선_노선_id).getStations()).getStationIds())
                .isEqualTo(Arrays.asList(경기중앙역_id, 강남역_id));
        assertThat(Stations.of(lineIdToLine.get(_2호선_노선_id).getStations()).getStationIds())
                .isEqualTo(Arrays.asList(강남역_id, 역삼역_id, 선릉역_id));
    }

}
