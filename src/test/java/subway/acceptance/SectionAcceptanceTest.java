package subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import subway.utils.DatabaseCleanup;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static subway.steps.LineSteps.지하철_노선_생성_요청;
import static subway.steps.SectionSteps.지하철_구간_삭제_요청;
import static subway.steps.SectionSteps.지하철_구간_생성_요청;
import static subway.steps.StationSteps.지하철_역_생성_요청;

@DisplayName("지하철 구간 관련 기능")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SectionAcceptanceTest {

    private static ExtractableResponse<Response> line;
    private static Long GANG_NAM_STATION;
    private static Long YANG_JAE_STATION;
    private static Long BOON_DANG_STATION;
    private static Long SEO_HYEON_STATION;


    @Autowired
    private DatabaseCleanup databaseCleanup;

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
        GANG_NAM_STATION = 지하철_역_생성_요청("강남역"); // 상행 종점역
        YANG_JAE_STATION = 지하철_역_생성_요청("양재역"); // 하행 종점역
        BOON_DANG_STATION = 지하철_역_생성_요청("분당역");
        SEO_HYEON_STATION = 지하철_역_생성_요청("서현역");
        line = 지하철_노선_생성_요청("신분당선", "bg-red-600", GANG_NAM_STATION, YANG_JAE_STATION, 10);
    }


    /* 지하철 구간 생성
    * when 새로운 구간을 생성하면
    * then 새로운 구간의 상행역은 해당 노선의 하행 종점역이 된다.
    * */

    @DisplayName("새로운 구간 생성시 새로운 구간의 상행역은 해당 노선의 하행 종점역이 된다.")
    @Test
    void 지하철_구간_생성_인수_테스트() {
        // when
        var lineId = line.jsonPath().getLong("id");
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(BOON_DANG_STATION, YANG_JAE_STATION, 10, lineId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.jsonPath().getString("upStation.name")).isEqualTo(line.jsonPath().getString("stations[1].name"));
    }


    /* 지하철 구간 생성 예외 테스트(1)
    * when 새로운 구간 생성시 새로운 구간의 상행역이 해당 노선의 하행 종점역이 아닐 경우
    * then 에러가 발생한다.
    * */

    @DisplayName("새로운 구간의 상행역이 해당 노선의 하행 종점역이 아닐 경우 에러가 발생하는 테스트")
    @Test
    void 지하철_구간_생성_예외_테스트_1() {
        // when
        var lineId = line.jsonPath().getLong("id");

        ExtractableResponse<Response> response = 지하철_구간_생성_요청(BOON_DANG_STATION, SEO_HYEON_STATION, 10, lineId);
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
    }

    /* 지하철 구간 생성 예외 테스트(2)
    *  when 새로운 구간 생성시 새로운 구간의 하행역이 해당 노선에 등록되어 있을 경우
    *  then 에러가 발생한다.
   * */
    @DisplayName("새로운 구간의 하행역이 해당 노선에 등록되어 있을 경우 에러가 발생하는 테스트")
    @Test
    void 지하철_구간_생성_예외_테스트_2() {
        // when
        var lineId = line.jsonPath().getLong("id");
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(GANG_NAM_STATION, YANG_JAE_STATION, 10, lineId);
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
    }

    /* 지하철 구간 제거
    * when 제거 하고 싶은 구간이 노선에 등록된 역(하행 종점역) 일 경우 , 마지막 구간일 경우
    * then 구간을 제거할 수 있다.
    * */

    @DisplayName("지하철 구간 제거는 마지막 구간일 경우에 가능하다.")
    @Test
    void 지하철_구간_삭제_인수_테스트() {
        var lineId = line.jsonPath().getLong("id");
        지하철_구간_생성_요청(BOON_DANG_STATION, YANG_JAE_STATION, 10, lineId);
        ExtractableResponse<Response> response = 지하철_구간_삭제_요청(lineId, BOON_DANG_STATION);
        assertThat(response.statusCode()).isEqualTo(OK.value());
    }


    /*
     * 지하철 구간 제거 예외 테스트(1)
     * when 제거하려는 구간이 마지막 구간(해당 지하철 노선의 하행 종점역)이 아닐 경우
     * then 에러가 발생한다.
     * */
    @DisplayName("제거하려는 구간이 마지막 구간(해당 지하철 노선의 하행 종점역)이 아닐 경우 에러가 발생하는 테스트")
    @Test
    void 지하철_구간_삭제_예외_테스트_1() {
        var lineId = line.jsonPath().getLong("id");
        지하철_구간_생성_요청(BOON_DANG_STATION, YANG_JAE_STATION, 10, lineId);
        ExtractableResponse<Response> response = 지하철_구간_삭제_요청(lineId, SEO_HYEON_STATION);
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
    }

    /*
     * 지하철 구간 제거 예외 테스트(2)
     * when 구간이 1개일 경우 제거하면
     * then 에러가 발생한다.
     * */
    @DisplayName("지하철 노선에 상행 종점역과 하행 종점역만 있을 경우 구간을 삭제하면 에러가 발생하는 테스트")
    @Test
    void 지하철_구간_삭제_예외_테스트_2() {
        var lineId = line.jsonPath().getLong("id");
        ExtractableResponse<Response> response = 지하철_구간_삭제_요청(lineId, YANG_JAE_STATION);
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
    }

}
