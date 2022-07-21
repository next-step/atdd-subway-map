package nextstep.subway.acceptance.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static nextstep.subway.acceptance.common.CommonSteps.삭제_성공_응답;
import static nextstep.subway.acceptance.line.LineSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    /**
     * When 노선 생성 요청
     * Then 지하철 노선이 생성된다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void 지하철_노선_생성_테스트() {
        // when 노선_생성_요청
        ExtractableResponse<Response> 노선_생성_응답 = 노선_생성_요청(SHIN_BUNDANG_LINE_NAME, SHIN_BUNDANG_LINE_COLOR, SHIN_BUNDANG_UP_STATION_NAME, SHIN_BUNDANG_DOWN_STATION_NAME, DISTANCE);
        // then 생성 노선 확인
        생성된_노선_확인(노선_생성_응답, SHIN_BUNDANG_LINE_NAME, SHIN_BUNDANG_LINE_COLOR);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 2개의 지하철 노선을 응답 받는다
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void 지하철_노선_목록_조회_테스트() {
        // given 2개의 지하철역을 생성하고
        노선_생성_요청(SHIN_BUNDANG_LINE_NAME, SHIN_BUNDANG_LINE_COLOR, SHIN_BUNDANG_UP_STATION_NAME, SHIN_BUNDANG_DOWN_STATION_NAME, DISTANCE);
        노선_생성_요청(BUNDANG_LINE_NAME, BUNDANG_LINE_COLOR, BUNDANG_UP_STATION_NAME, BUNDANG_DOWN_STATION_NAME, DISTANCE);
        // when 지하철역 목록을 조회하면
        List<String> 노선명_목록 = 지하철_노선_목록_조회().jsonPath().getList("name");
        // then 2개의 지하철역을 응답 받는다
        생성된_노선_목록_확인(노선명_목록, SHIN_BUNDANG_LINE_NAME, BUNDANG_LINE_NAME);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void 지하철_노선_조회() {
        // given 지하철 노선을 생성하고
        ExtractableResponse<Response> 노선 = 노선_생성_요청(SHIN_BUNDANG_LINE_NAME, SHIN_BUNDANG_LINE_COLOR, SHIN_BUNDANG_UP_STATION_NAME, SHIN_BUNDANG_DOWN_STATION_NAME, DISTANCE);
        // when 생성한 지하철 노선을 조회하면
        String lineName = 노선.jsonPath().getString("name");
        // then 생성한 지하철 노선의 정보를 응답받을 수 있다.
        assertThat(lineName).isEqualTo(SHIN_BUNDANG_LINE_NAME);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다")
    @Test
    void 지하철_노선_수정() {
        // given 지하철 노선 생성
        ExtractableResponse<Response> 노선 = 노선_생성_요청(SHIN_BUNDANG_LINE_NAME, SHIN_BUNDANG_LINE_COLOR, SHIN_BUNDANG_UP_STATION_NAME, SHIN_BUNDANG_DOWN_STATION_NAME, DISTANCE);
        Long 노선_ID = 노선.jsonPath().getLong("id");
        // When 지하철 노선 수정
        LineSteps.지하철_노선_수정(노선_ID, FIRST_LINE_NAME, FIRST_LINE_COLOR);
        ExtractableResponse<Response> updatedLine = LineSteps.지하철_노선_조회(노선_ID);
        // Then 해당 지하철 노선 정보는 수정된다
        assertAll(
                () -> assertThat(updatedLine.jsonPath().getString("name")).isEqualTo(FIRST_LINE_NAME),
                () -> assertThat(updatedLine.jsonPath().getString("color")).isEqualTo(FIRST_LINE_COLOR)
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다")
    @Test
    void 지하철_노선_삭제() {
        // given 지하철_노선_생성
        ExtractableResponse<Response> 노선 = 노선_생성_요청(SHIN_BUNDANG_LINE_NAME, SHIN_BUNDANG_LINE_COLOR, SHIN_BUNDANG_UP_STATION_NAME, SHIN_BUNDANG_DOWN_STATION_NAME, DISTANCE);
        // When 지하철_노선_삭제
        ExtractableResponse<Response> deleteLineResponse = LineSteps.지하철_노선_삭제(노선.jsonPath().getLong("id"));
        // Then 해당 지하철 노선 정보를 찾을 수 없다
        삭제_성공_응답(deleteLineResponse);
    }

}