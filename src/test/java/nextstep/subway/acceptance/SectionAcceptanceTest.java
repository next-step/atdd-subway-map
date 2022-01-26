package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.StationSteps.*;
import static nextstep.subway.acceptance.SectionSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 관리 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    private static final String 신분당선 = "신분당선";
    private static final String BG_RED_600 = "bg-red-600";
    private static final String 미금역 = "미금역";
    private static final String 양재역 = "양재역";
    private static final String 강남역 = "강남역";
    private static final String 동천역 = "동천역";
    private static final int 미금역_양재역_거리 = 2;
    private static final int 양재역_강남역_거리 = 1;
    private static final int 동천역_강남역_거리 = 3;
    private static final int 동천역_양재역_거리 = 5;

    /**
     * When 구간 생성을 요청 하면
     * Then 구간 생성이 성공한다.
     */
    @DisplayName("구간 생성")
    @Test
    void createSection() {
        // given
        Long 양재역_아이디 = 지하철역_생성_요청(양재역).jsonPath().getLong("id");
        Long 강남역_아이디 = 지하철역_생성_요청(강남역).jsonPath().getLong("id");
        ExtractableResponse<Response> lineResponse = 지하철_노선_생성_요청(신분당선, BG_RED_600
                , 지하철역_생성_요청(미금역).jsonPath().getLong("id")
                , 지하철역_생성_요청(양재역).jsonPath().getLong("id")
                , 미금역_양재역_거리);

        // when
        ExtractableResponse<Response> createResponse =
                구간_생성_요청(lineResponse.jsonPath().getLong("id"), 양재역_아이디, 강남역_아이디, 양재역_강남역_거리);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createResponse.header("Location")).isNotBlank();
    }

    /**
     * When 해당 노선의 하행 종점역이 아닌 새로운 구간 생성을 요청 하면
     * Then 구간 생성이 실패한다.
     */
    @DisplayName("해당 노선의 하행 종점역이 아닌 새로운 구간 생성")
    @Test
    void notSubSection() {
        ExtractableResponse<Response> lineResponse = 지하철_노선_생성_요청(신분당선, BG_RED_600
                , 지하철역_생성_요청(미금역).jsonPath().getLong("id")
                , 지하철역_생성_요청(양재역).jsonPath().getLong("id")
                , 미금역_양재역_거리);

        Long 동천역_아이디 = 지하철역_생성_요청(동천역).jsonPath().getLong("id");
        Long 강남역_아이디 = 지하철역_생성_요청(강남역).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> createResponse =
                구간_생성_요청(lineResponse.jsonPath().getLong("id"), 동천역_아이디, 강남역_아이디, 동천역_강남역_거리);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * When 해당 노선에 이미 등록된 역을 하행역으로 새로운 구간 생성 요청을 하면
     * Then 구간 생성이 실패한다.
     */
    @DisplayName("이미 등록된 역을 하행역으로 새로운 구간 생성")
    @Test
    void duplicateSection() {
        ExtractableResponse<Response> lineResponse = 지하철_노선_생성_요청(신분당선, BG_RED_600
                , 지하철역_생성_요청(미금역).jsonPath().getLong("id")
                , 지하철역_생성_요청(양재역).jsonPath().getLong("id")
                , 미금역_양재역_거리);

        Long 동천역_아이디 = 지하철역_생성_요청(동천역).jsonPath().getLong("id");
        Long 양재역_아이디 = 지하철역_생성_요청(양재역).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> createResponse =
                구간_생성_요청(lineResponse.jsonPath().getLong("id"), 동천역_아이디, 양재역_아이디, 동천역_양재역_거리);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
