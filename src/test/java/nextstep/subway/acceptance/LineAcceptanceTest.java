package nextstep.subway.acceptance;

import static nextstep.subway.step.LineStep.구간_삭제;
import static nextstep.subway.step.LineStep.구간_생성;
import static nextstep.subway.step.LineStep.노선_목록_조회;
import static nextstep.subway.step.LineStep.노선_변경;
import static nextstep.subway.step.LineStep.노선_삭제;
import static nextstep.subway.step.LineStep.노선_생성;
import static nextstep.subway.step.StationStep.역_생성;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.ChangeLineRequest;
import nextstep.subway.domain.SectionRequest;
import nextstep.subway.utils.RequestMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    private static final String DEFAULT_PATH = "/lines";
    private static final String JSON_PATH_ID = "id";

    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";
    private static final String 신촌역 = "신촌역";
    private static final String 교대역 = "교대역";

    private static final String 신분당선 = "신분당선";
    private static final String 수인분당선 = "수인분당선";

    private static final String SINBUNDANGLINE_COLOR = "bg-red-600";
    private static final String SUINBUNDANGLINE_COLOR = "bg-blue-700";

    private static final int DEFAULT_DISTANCE = 5;

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLineTest() {
        // given

        Long 강남역_id = 역_생성(강남역).jsonPath().getLong(JSON_PATH_ID);
        Long 역삼역_id = 역_생성(역삼역).jsonPath().getLong(JSON_PATH_ID);

        // when
        ExtractableResponse<Response> response = 노선_생성(신분당선, SINBUNDANGLINE_COLOR, 강남역_id, 역삼역_id,
            DEFAULT_DISTANCE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given
     * When 노선 생성 시 두 종점역을 모두 입력하지 않으면
     * Then 생성에 실패한다
     */
    @Test
    @DisplayName("지하철 노선 생성 실패")
    void createLineExceptionTest() {
        //give
        String failParam = "fail";

        // when
        ExtractableResponse<Response> response = RequestMethod.post(DEFAULT_PATH, failParam);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * scenario: 지하철 노선에 구간 등록 성공 (새로 등록할 구간의 상행역은 기존 구간의 하행이된다)
     * Given 지하철 노선에 구간이 등록되어 있고
     * When  기존 노선의 하행역이 새로운 구간의 상행역 & 새로운 노선의 하행역은 등록되어 있지 않은 역이면
     * Then  구간 등록에 성공 한다
     */
    @Test
    @DisplayName("지하철 노선 구간 등록 성공 테스트")
    void addSectionTest() {
        // given
        Long 강남역_id = 역_생성(강남역).jsonPath().getLong(JSON_PATH_ID);
        Long 역삼역_id = 역_생성(역삼역).jsonPath().getLong(JSON_PATH_ID);

        ExtractableResponse<Response> response = 노선_생성(신분당선, SINBUNDANGLINE_COLOR, 강남역_id, 역삼역_id,
            DEFAULT_DISTANCE);

        String 신분당선_id = response.jsonPath().getString(JSON_PATH_ID);

        // when
        Long 교대역_id = 역_생성(교대역).jsonPath().getLong(JSON_PATH_ID);

        SectionRequest sectionRequest = new SectionRequest(역삼역_id, 교대역_id, DEFAULT_DISTANCE);

        ExtractableResponse<Response> sectionRegisterResponse = RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(sectionRequest)
            .when()
            .post(DEFAULT_PATH + "/" + 신분당선_id + "/sections")
            .then().log().all()
            .extract();

        // then
        assertThat(sectionRegisterResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철
     * 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLinesTest() {
        // given
        Long 강남역_id = 역_생성(강남역).jsonPath().getLong(JSON_PATH_ID);
        Long 역삼역_id = 역_생성(역삼역).jsonPath().getLong(JSON_PATH_ID);

        노선_생성(신분당선, SINBUNDANGLINE_COLOR, 강남역_id, 역삼역_id, DEFAULT_DISTANCE);

        Long 신촌역_id = 역_생성(신촌역).jsonPath().getLong(JSON_PATH_ID);
        Long 교대역_id = 역_생성(교대역).jsonPath().getLong(JSON_PATH_ID);

        노선_생성(수인분당선, SUINBUNDANGLINE_COLOR, 신촌역_id, 교대역_id, DEFAULT_DISTANCE);

        // when
        ExtractableResponse<Response> response = 노선_목록_조회(DEFAULT_PATH);

        // then
        assertThat(response.jsonPath().getList("name")).containsExactly(신분당선, 수인분당선);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 단일 노선 조회")
    @Test
    void getLineTest() {
        // given
        Long 강남역_id = 역_생성(강남역).jsonPath().getLong(JSON_PATH_ID);
        Long 역삼역_id = 역_생성(역삼역).jsonPath().getLong(JSON_PATH_ID);

        String 신분당선_id = 노선_생성(신분당선, SINBUNDANGLINE_COLOR, 강남역_id, 역삼역_id,
            DEFAULT_DISTANCE).jsonPath().getString(JSON_PATH_ID);

        // when
        ExtractableResponse<Response> response = 노선_목록_조회(DEFAULT_PATH + "/" + 신분당선_id);

        assertThat(response.jsonPath().getString("name")).isEqualTo(신분당선);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLineTest() {
        // given
        Long 강남역_id = 역_생성(강남역).jsonPath().getLong(JSON_PATH_ID);
        Long 역삼역_id = 역_생성(역삼역).jsonPath().getLong(JSON_PATH_ID);

        String 신분당선_id = 노선_생성(신분당선, SINBUNDANGLINE_COLOR, 강남역_id, 역삼역_id,
            DEFAULT_DISTANCE).jsonPath().getString(JSON_PATH_ID);
        // when
        ChangeLineRequest request = new ChangeLineRequest("뉴 신분당선", "bg-red-700");

        ExtractableResponse<Response> response = 노선_변경(DEFAULT_PATH + "/" + 신분당선_id, request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo("뉴 신분당선");
        assertThat(response.jsonPath().getString("color")).isEqualTo("bg-red-700");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLineTest() {
        // given
        Long 강남역_id = 역_생성(강남역).jsonPath().getLong(JSON_PATH_ID);
        Long 역삼역_id = 역_생성(역삼역).jsonPath().getLong(JSON_PATH_ID);

        String 신분당선_id = 노선_생성(신분당선, SINBUNDANGLINE_COLOR, 강남역_id, 역삼역_id,
            DEFAULT_DISTANCE).jsonPath().getString(JSON_PATH_ID);

        // when
        ExtractableResponse<Response> response = 노선_삭제(DEFAULT_PATH + "/" + 신분당선_id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @Test
    @DisplayName("중복트 이름으로 지하철 노선 생성 실패")
    void duplicationLineNameExceptionTest() {
        // given
        Long 강남역_id = 역_생성(강남역).jsonPath().getLong(JSON_PATH_ID);
        Long 역삼역_id = 역_생성(역삼역).jsonPath().getLong(JSON_PATH_ID);

        노선_생성(신분당선, SINBUNDANGLINE_COLOR, 강남역_id, 역삼역_id, DEFAULT_DISTANCE);

        // when
        ExtractableResponse<Response> 홍대역 = 역_생성("홍대역");
        ExtractableResponse<Response> 신촌역 = 역_생성("신촌역");

        Long 홍대역_id = 홍대역.jsonPath().getLong("id");
        Long 신촌역_id = 신촌역.jsonPath().getLong("id");

        ExtractableResponse<Response> response = 노선_생성(신분당선, SINBUNDANGLINE_COLOR, 강남역_id, 역삼역_id,
            DEFAULT_DISTANCE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }


    /**
     * scenario: 지하철 노선에 구간 등록 성공 (새로 등록할 구간의 상행역은 기존 구간의 하행이된다)
     * given 지하철 노선에 구간이 등록되어 있고
     * when  기존 노선의 하행역이 새로운 구간의 상행역 & 새로운 노선의 하행역은 등록되어 있지 않은 역이면
     * then  구간 등록에 성공 한다
     */
    @Test
    @DisplayName("지하철 노선 구간 등록 성공")
    void registerSectionTest() {
        // given 지하철 노선에 구간이 등록되어 있고
        Long 강남역_id = 역_생성(강남역).jsonPath().getLong(JSON_PATH_ID);
        Long 역삼역_id = 역_생성(역삼역).jsonPath().getLong(JSON_PATH_ID);
        Long 신촌역_id = 역_생성(신촌역).jsonPath().getLong(JSON_PATH_ID);

        String 신분당선_id = 노선_생성(신분당선, SINBUNDANGLINE_COLOR, 강남역_id, 역삼역_id, DEFAULT_DISTANCE)
            .jsonPath().getString(JSON_PATH_ID);

        // when
        SectionRequest request = new SectionRequest(역삼역_id, 신촌역_id, DEFAULT_DISTANCE);
        ExtractableResponse<Response> response = 구간_생성(DEFAULT_PATH + "/" + 신분당선_id + "/sections",
            request);

        // then
        assertThat(response.jsonPath().getList("stations.name")).containsExactly(강남역, 역삼역, 신촌역);
    }

    /**
     * scenario: 지하철 노선에 구간 등록 실패 (기존 노선이 없는 경우)
     * given
     * when  추가 할 기존 노선이 없다면
     * then  구간 등록에 실패 한다
     */
    @Test
    @DisplayName("지하철 노선 구간 등록 실패 (기존 노선이 없는 경우)")
    void failRegisterNotFoundLineTest() {
        //given
        //when
        Long 강남역_id = 역_생성(강남역).jsonPath().getLong(JSON_PATH_ID);
        Long 역삼역_id = 역_생성(역삼역).jsonPath().getLong(JSON_PATH_ID);

        SectionRequest request = new SectionRequest(강남역_id, 역삼역_id, DEFAULT_DISTANCE);
        ExtractableResponse<Response> response = 구간_생성(DEFAULT_PATH + "/" + 1 + "/sections",
            request);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /**
     * scenario: 지하철 노선에 구간 등록 실패 (새로운 구간의 상행역이 기존 노선의 하행역이 아닌 경우)
     * given 지하철 노선에 구간이 등록되어 있고
     * when 기존 노선의 하행역이 새로운 구간의 상행역이 아니라면
     * then  구간 등록에 실패 한다
     */
    @Test
    @DisplayName("지하철 노선에 구간 등록 실패 (새로운 구간의 상행역이 기존 노선의 하행역이 아닌 경우)")
    void failRegisterSectionNotLastStationTest() {
        // given 지하철 노선에 구간이 등록 되어 있고
        Long 강남역_id = 역_생성(강남역).jsonPath().getLong(JSON_PATH_ID);
        Long 역삼역_id = 역_생성(역삼역).jsonPath().getLong(JSON_PATH_ID);

        String 신분당선_id = 노선_생성(신분당선, SINBUNDANGLINE_COLOR, 강남역_id, 역삼역_id,
            DEFAULT_DISTANCE).jsonPath().getString(JSON_PATH_ID);

        // when 기존 노선의 하행역이 아닌 구간을 등록하면
        SectionRequest request = new SectionRequest(강남역_id, 역삼역_id, DEFAULT_DISTANCE);

        ExtractableResponse<Response> response = 구간_생성(DEFAULT_PATH + "/" + 신분당선_id + "/sections",
            request);

        // then 구간 등록에 실패 한다
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * scenario: 지하철 노선에 구간 등록 실패 (새로 등록할 구간의 하행역이 기존 구간에 존재하는 경우)
     * given 지하철 노선에 구간이 등록되어 있고
     * when
     * 새로운 노선의 하행역이 기존 노선에 등록되어 있다면
     * then  구간 등록에 실패 한다
     */
    @Test
    @DisplayName("지하철 노선에 구간 등록 실패 (새로 등록할 구간의 하행역이 기존 구간에 존재하는 경우)")
    void failRegisterSectionAlredyRegistedStationTest() {
        // given 지하철 노선에 구간이 등록 되어 있고
        Long 강남역_id = 역_생성(강남역).jsonPath().getLong(JSON_PATH_ID);
        Long 역삼역_id = 역_생성(역삼역).jsonPath().getLong(JSON_PATH_ID);

        String 신분당선_id = 노선_생성(신분당선, SINBUNDANGLINE_COLOR, 강남역_id, 역삼역_id,
            DEFAULT_DISTANCE).jsonPath().getString(JSON_PATH_ID);

        // when 등록할 구간의 하행역이 이미 노선에 등록된 역이라면
        SectionRequest request = new SectionRequest(역삼역_id, 강남역_id, DEFAULT_DISTANCE);

        ExtractableResponse<Response> response = 구간_생성(DEFAULT_PATH + "/" + 신분당선_id + "/sections",
            request);

        // then 구간 등록에 실패 한다
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * scenario: 지하철 노선에 구간을 제거 성공 (지하철 노선에 등록된 마지막 역(하행 종점역)만 제거)
     * given 역이 3개 이상인 지하철 노선이 등록되어 있고
     * when  해당 지하철 노선의 하행 종점역을 제거 요청하면
     * then  노선 구간이 제거 된다
     */
    @Test
    @DisplayName("지하철 노선에 구간을 제거 성공 (지하철 노선에 등록된 마지막 역(하행 종점역)만 제거)")
    void deleteSectionTest() {
        // given
        Long 강남역_id = 역_생성(강남역).jsonPath().getLong(JSON_PATH_ID);
        Long 역삼역_id = 역_생성(역삼역).jsonPath().getLong(JSON_PATH_ID);
        Long 신촌역_id = 역_생성(신촌역).jsonPath().getLong(JSON_PATH_ID);

        String 신분당선_id = 노선_생성(신분당선, SINBUNDANGLINE_COLOR, 강남역_id, 역삼역_id,
            DEFAULT_DISTANCE).jsonPath().getString(JSON_PATH_ID);

        SectionRequest request = new SectionRequest(역삼역_id, 신촌역_id, DEFAULT_DISTANCE);

        구간_생성(DEFAULT_PATH + "/" + 신분당선_id + "/sections", request);

        // when
        ExtractableResponse<Response> response = 구간_삭제(DEFAULT_PATH + "/" + 신분당선_id + "/sections",
            "stationId", 신촌역_id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * scenario: 지하철 노선에 구간을 제거 실패 (지하철 노선의 구간이 2개인 경우)
     *      given 역이 2개인 지하철 노선이 등록되어 있고
     *      when  해당 지하철노선의 하행 종점역을 제거 요청하면
     *      then  노선 구간이 제거가 실패 한다
     */
    @Test
    @DisplayName("지하철 노선에 구간을 제거 실패 (지하철 노선의 구간이 2개인 경우)")
    void failDeleteSectionTest() {
        // given
        Long 강남역_id = 역_생성(강남역).jsonPath().getLong(JSON_PATH_ID);
        Long 역삼역_id = 역_생성(역삼역).jsonPath().getLong(JSON_PATH_ID);

        String 신분당선_id = 노선_생성(신분당선, SINBUNDANGLINE_COLOR, 강남역_id, 역삼역_id,
            DEFAULT_DISTANCE).jsonPath().getString(JSON_PATH_ID);

        // when
        ExtractableResponse<Response> response = 구간_삭제(DEFAULT_PATH + "/" + 신분당선_id + "/sections",
            "stationId", 역삼역_id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * scenario: 지하철 노선에 구간을 제거 실패 (하행 종점역이 아닌 역을 제거)
     *      given 역이 3개 이상인 지하철 노선이 등록되어 있고
     *      when  해당 지하철 노선의 하행 종점역이 아닌 역을 제거 요청하면
     *      then  노선 구간이 제거가 실패 한다
     */
    @Test
    @DisplayName("지하철 노선에 구간을 제거 실패 (하행 종점역이 아닌 역을 제거)")
    void failDeleteSectionNotLastStationTest() {
        // given
        Long 강남역_id = 역_생성(강남역).jsonPath().getLong(JSON_PATH_ID);
        Long 역삼역_id = 역_생성(역삼역).jsonPath().getLong(JSON_PATH_ID);
        Long 신촌역_id = 역_생성(신촌역).jsonPath().getLong(JSON_PATH_ID);

        String 신분당선_id = 노선_생성(신분당선, SINBUNDANGLINE_COLOR, 강남역_id, 역삼역_id,
            DEFAULT_DISTANCE).jsonPath().getString(JSON_PATH_ID);

        SectionRequest request = new SectionRequest(역삼역_id, 신촌역_id, DEFAULT_DISTANCE);

        구간_생성(DEFAULT_PATH + "/" + 신분당선_id + "/sections", request);

        // when
        ExtractableResponse<Response> response = 구간_삭제(DEFAULT_PATH + "/" + 신분당선_id + "/sections",
            "stationId", 역삼역_id);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * scenario: 등록된 구간을 통해 역 목록 조회 기능 given 역이 2개 이상인 지하철 노선이 등록되어 있고 when  해당 노선의 구간을 요청하면 then 해당
     * 노선과 노선에 등록된 구간을 순서대로 정렬하여 상행 종점부터 하행 종점까지 목록을 응답 한다
     */
    @Test
    @DisplayName("등록된 구간을 통해 역 목록 조회 기능")
    void showStationsTest() {
        // given
        Long 강남역_id = 역_생성(강남역).jsonPath().getLong(JSON_PATH_ID);
        Long 역삼역_id = 역_생성(역삼역).jsonPath().getLong(JSON_PATH_ID);
        Long 신촌역_id = 역_생성(신촌역).jsonPath().getLong(JSON_PATH_ID);

        String 신분당선_id = 노선_생성(신분당선, SINBUNDANGLINE_COLOR, 강남역_id, 역삼역_id,
            DEFAULT_DISTANCE).jsonPath().getString(JSON_PATH_ID);

        SectionRequest request = new SectionRequest(역삼역_id, 신촌역_id, DEFAULT_DISTANCE);

        구간_생성(DEFAULT_PATH + "/" + 신분당선_id + "/sections", request);

        // when
        ExtractableResponse<Response> response = 노선_목록_조회(DEFAULT_PATH);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

}
