package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.SectionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.LineAcceptanceUtil.*;
import static nextstep.subway.acceptance.SectionAcceptanceUtil.지하철_구간_등록_요청;
import static nextstep.subway.acceptance.StationAcceptanceUtil.지하철_역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */

    @BeforeEach
    void init() {
        지하철_역_생성_요청("동암역");
        지하철_역_생성_요청("강남역");
        지하철_역_생성_요청("부평역");
        지하철_역_생성_요청("신촌역");
    }

    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(new LineRequest("신분당선", "bg-red-600", 4L, 2L, 10));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
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
        // given
        지하철_노선_생성_요청(new LineRequest("신분당선", "bg-red-600", 4L, 2L, 10));

        // given
        지하철_노선_생성_요청(new LineRequest("2호선", "bg-red-600", 4L, 2L, 10));

        // when
        ExtractableResponse<Response> response = 지하철_모든_노선_조회_요청();

        // then
        assertThat(response.jsonPath()
                           .getList("name")).containsExactly("신분당선", "2호선");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(new LineRequest("신분당선", "bg-red-600", 4L, 2L, 10));
        String lineLocation = response.header("location");
        지하철_구간_등록_요청(lineLocation, new SectionRequest("3", "2", 10));

        // when
        ExtractableResponse<Response> response2 = 지하철_특정_노선_조회_요청(response);

        // then
        assertAll(
                () -> assertThat(response2.jsonPath()
                                          .getString("name")).isEqualTo("신분당선"),
                () -> assertThat(response2.jsonPath()
                                          .getList("stations.name")).containsExactly("신촌역", "강남역", "부평역")
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {

        // given
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(new LineRequest("2호선", "bg-red-600", 4L, 2L, 10));

        // given
        지하철_노선_수정_요청(response, new LineRequest("구분당선", "bg-blue-600", 4L, 2L, 10));


        // when
        ExtractableResponse<Response> response2 = 지하철_특정_노선_조회_요청(response);

        // then
        assertAll(
                () -> assertThat(response2.jsonPath()
                                          .getString("color")).isEqualTo("bg-blue-600"),
                () -> assertThat(response2.jsonPath()
                                          .getString("name")).isEqualTo("구분당선")
        );
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(new LineRequest("신분당선", "bg-red-600", 4L, 2L, 10));

        // when
        ExtractableResponse<Response> response2 = 지하철_노선_삭제_요청(response);

        // then
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


    /*
       Given 지하철 노선 생성을 요청 하고
       When 같은 이름으로 지하철 노선 생성을 요청 하면
       Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("중복이름으로 지하철 노선 생성")
    @Test
    void duplicatedLine() {

        // given
        지하철_노선_생성_요청(new LineRequest("신분당선", "bg-red-600", 4L, 2L, 10));

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(new LineRequest("신분당선", "bg-red-600", 4L, 2L, 10));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

}
