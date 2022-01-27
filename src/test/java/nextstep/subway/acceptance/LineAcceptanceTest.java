package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> createResponse = 지하철노선_생성();

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
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
        String 신분당선 = "신분당선";
        String 빨간색 = "bg-red-600";

        String 경춘선 = "경춘선";
        String 초록색 = "bg-green-600";

        지하철노선_생성(신분당선, 빨간색);
        지하철노선_생성(경춘선, 초록색);

        // when
        Map<String, String> params = new HashMap<>();
        ExtractableResponse<Response> readResponse = 지하철노선_목록_조회(params);

        // then
        assertThat(readResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> lineNames = readResponse.jsonPath().getList("name");
        List<String> lineColors = readResponse.jsonPath().getList("color");
        assertThat(lineNames).contains(신분당선, 경춘선);
        assertThat(lineColors).contains(빨간색, 초록색);
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
        String _2호선 = "2호선";
        String 초록색 = "bg-green-600";

        ExtractableResponse<Response> createLineResponse = 지하철노선_생성(_2호선, 초록색);

        int stationId = createLineResponse.jsonPath().getInt("id");

        // when
        ExtractableResponse<Response> readResponse = 지하철노선_조회(stationId);

        // then
        assertThat(readResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        String name = readResponse.jsonPath().getString("name");
        String color = readResponse.jsonPath().getString("color");
        assertThat(name).isEqualTo(_2호선);
        assertThat(color).isEqualTo(초록색);

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
        ExtractableResponse<Response> createResponse = 지하철노선_생성();
        int stationId = createResponse.jsonPath().getInt("id");

        // when
        String _1호정_수정 = "1호선-수정";
        String 파란색_수정 = "bg-blue-600-수정";
        Map<String, String> params = new HashMap<>();
        params.put("name", _1호정_수정);
        params.put("color", 파란색_수정);

        ExtractableResponse<Response> modifyResponse = 지하철노선_수정(stationId, params);

        // then
        assertThat(modifyResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        String name = modifyResponse.jsonPath().getString("name");
        String color = modifyResponse.jsonPath().getString("color");
        assertThat(name).isEqualTo(_1호정_수정);
        assertThat(color).isEqualTo(파란색_수정);
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
        ExtractableResponse<Response> createResponse = 지하철노선_생성();
        int stationId = createResponse.jsonPath().getInt("id");

        // when
        ExtractableResponse<Response> deleteResponse = 지하철노선_삭제(stationId);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름의 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 요청이 실패한다.
     */
    @DisplayName("지하철 노선 등록 실패 - 이름 중복")
    @Test
    void createLineWithDuplicatedException() {
        // given
        지하철노선_생성("1호선", "blue");

        // when
        ExtractableResponse<Response> createResponse = 지하철노선_생성("1호선", "red");

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
