package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.step.LineStep;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    private final static Integer NUMBER_ONE = 1;

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {

        // 요청 후, 노선을 생성하다
        ExtractableResponse<Response> extract = LineStep.saveLine("color_1", "name_1");

        // 상태 코드
        assertThat(extract.response().statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(extract.header("Location")).isNotBlank();
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

        // 요청 후, 노선을 생성하다
        LineStep.saveLine("color_1", "name_1");
        LineStep.saveLine("color_2", "name_2");

        ExtractableResponse<Response> response = LineStep.showLines();

        // 조회 포함 확인
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> resultResponseData = response.jsonPath().getList("color");
        assertThat(resultResponseData).contains("color_1", "color_2");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {

        // 요청 후, 노선을 생성하다
        LineStep.saveLine("color_1", "name_1");

        // 조회 결과
        ExtractableResponse<Response> response = LineStep.showLine(NUMBER_ONE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        String responseResultData = response.jsonPath().get("color");
        assertThat(responseResultData).isEqualTo("color_1");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {

        // 요청 후, 노선을 생성하다
        LineStep.saveLine("color_1", "name_1");

        // 수정 요청
        ExtractableResponse<Response> response = LineStep.updateLine("color_2", "name_2", 1);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {

        // 요청 후, 노선을 생성하다
        LineStep.saveLine("color_1", "name_1");

        // 노선을 삭제하다
        ExtractableResponse<Response> response = LineStep.deleteLine(NUMBER_ONE);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
