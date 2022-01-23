package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.LineSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @Test
    void 지하철_노선_생성() {
        // when
        ExtractableResponse<Response> 지하철_노선_생성_응답 = LineSteps.지하철_노선_생성_요청("9호선", "갈색");

        // then
        assertThat(지하철_노선_생성_응답.statusCode()).isEqualTo(HttpStatus.FOUND.value());
        assertThat(지하철_노선_생성_응답.header("Location")).isEqualTo("/lines/1");
    }


    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @Test
    void 지하철_노선_목록_조회() {
        // given
        LineSteps.지하철_노선_생성_요청("9호선", "갈색");
        LineSteps.지하철_노선_생성_요청("5호선", "보라색");

        // when
        ExtractableResponse<Response> 지하철_노선_목록_조회_응답 = LineSteps.지하철_노선_목록_조회_요청();

        // then
        assertThat(지하철_노선_목록_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> lineNames = 지하철_노선_목록_조회_응답.jsonPath().getList("name");
        assertThat(lineNames).containsAll(lineNames);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @Test
    void 지하철_노선_조회() {
        // given
        ExtractableResponse<Response> 지하철_노선_생성_응답 = LineSteps.지하철_노선_생성_요청("9호선", "갈색");

        // when
        String url = 지하철_노선_생성_응답.header("Location");
        ExtractableResponse<Response> 지하철_노선_조회_응답 = LineSteps.지하철_노선_조회_요청(url);

        // then
        assertThat(지하철_노선_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
        String lineName = 지하철_노선_조회_응답.jsonPath().getString("name");
        assertThat(lineName).isEqualTo("9호선");
    }

    /**
     * When 지하철 노선도를 조회하면
     * Then 지하철 노선 조회가 실패한다.
     */
    @Test
    void 존재하지_않는_노선_조회() {
        // given
        LineSteps.지하철_노선_생성_요청("9호선", "갈색");

        // when
        ExtractableResponse<Response> 지하철_노선_조회_응답 = LineSteps.지하철_노선_조회_요청("lines/2");

        // then
        assertThat(지하철_노선_조회_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @Test
    void 지하철_노선_수정() {
        // given
        ExtractableResponse<Response> 지하철_노선_생성_응답 = LineSteps.지하철_노선_생성_요청("9호선", "갈색");

        // when
        String url = 지하철_노선_생성_응답.header("Location");
        ExtractableResponse<Response> 지하철_노선_수정_응답 = LineSteps.지하철_노선_수정_요청(url, "5호선", "보라색");

        // then
        assertThat(지하철_노선_수정_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
        ExtractableResponse<Response> 지하철_노선_조회_응답 = LineSteps.지하철_노선_조회_요청(url);
        String lineName = 지하철_노선_조회_응답.jsonPath().getString("name");
        assertThat(lineName).isEqualTo("5호선");
    }

    /**
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보가 없어 신규 생성한다.
     */
    @Test
    void 존재하지_않는_노선_수정() {
        // when
        ExtractableResponse<Response> 지하철_노선_수정_응답 = LineSteps.지하철_노선_수정_요청("lines/2", "5호선", "보라색");

        // then
        assertThat(지하철_노선_수정_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        String lineName = 지하철_노선_수정_응답.jsonPath().getString("name");
        assertThat(lineName).isEqualTo("5호선");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @Test
    void 지하철_노선_삭제() {
        // given
        ExtractableResponse<Response> 지하철_노선_생성_응답 = LineSteps.지하철_노선_생성_요청("9호선", "갈색");

        // when
        String url = 지하철_노선_생성_응답.header("Location");
        ExtractableResponse<Response> 지하철_노선_삭제_응답 = LineSteps.지하철_노선_삭제_요청(url);

        // then
        assertThat(지하철_노선_삭제_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        ExtractableResponse<Response> 지하철_노선_조회_응답 = LineSteps.지하철_노선_조회_요청(url);
        assertThat(지하철_노선_조회_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청하고,
     * When 같은 이름으로 지하철 노선 생성을 요청하면,
     * Then 지하철 노선 생성이 실패한다.
     */
    @Test
    void 중복_이름으로_지하철_노선_생성() {
        // given
        LineSteps.지하철_노선_생성_요청("9호선", "갈색");

        // when
        ExtractableResponse<Response> 지하철_노선_중복_생성_응답 = LineSteps.지하철_노선_생성_요청("9호선", "갈색");

        // then
        assertThat(지하철_노선_중복_생성_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(지하철_노선_중복_생성_응답.jsonPath().getString("errorMessage")).isEqualTo("이미 등록된 노선입니다. 노선 이름 = " + "9호선");
    }
}
