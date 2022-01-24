package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.step.LineStep;
import nextstep.subway.step.SectionStep;
import nextstep.subway.step.StationStep;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

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
        ExtractableResponse<Response> extract = LineStep.saveLine("하늘색", "4호선", 1L,2L,3);

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
        LineStep.saveLine("하늘색", "4호선", 1L,2L,3);
        LineStep.saveLine("파란색", "1호선", 1L,2L,3);

        ExtractableResponse<Response> response = LineStep.showLines();

        // 조회 포함 확인
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> resultResponseData = response.jsonPath().getList("color");
        assertThat(resultResponseData).contains("하늘색", "파란색");
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
        LineStep.saveLine("하늘색", "4호선", 1L,2L,3);

        // 조회 결과
        ExtractableResponse<Response> response = LineStep.showLine(NUMBER_ONE);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        String responseResultData = response.jsonPath().get("color");
        assertThat(responseResultData).isEqualTo("하늘색");
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
        LineStep.saveLine("하늘색", "4호선", 1L,2L,3);

        // 수정 요청
        ExtractableResponse<Response> response = LineStep.updateLine("파란색", "1호선", 1, 1L,2L,3);

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
        LineStep.saveLine("하늘색", "4호선", 1L,2L,3);

        // 노선을 삭제하다
        ExtractableResponse<Response> response = LineStep.deleteLine(NUMBER_ONE);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선을 생성 요청 한다.
     * When 같은 이름으로 지하철 역을 생성 요청한다.
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("중복 지하철 노선 생성 실패")
    @Test
    void createLine_duplication() {

        // 노선을 생성한다.
        LineStep.saveLine("하늘색", "4호선", 1L,2L,3);

        // 중복으로 생성할 때
        ExtractableResponse<Response> response = LineStep.saveLine("파란색", "4호선", 1L,2L,3);

        // 실패를 한다.
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    // 섹션에 대한 코드
    @DisplayName("구간을 생성")
    @Test
    void createSection() {
        // 역 2개 생성 : 1L, 2L
        ExtractableResponse<Response> 수원역 = StationStep.saveStation("수원역"); // up
        ExtractableResponse<Response> 사당역 = StationStep.saveStation("사당역"); // down

        // 노선 생성
        LineStep.saveLine("파란색", "1호선", 1L, 2L, 10);

        // 구간 생성
        ExtractableResponse<Response> extract = SectionStep.saveSection(1L, 2L, 10, 1L);

        // 상태 코드
        assertThat(extract.response().statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(extract.header("Location")).isNotBlank();
    }

    @DisplayName("새로운 구간의 하행역은 현재 등록되어있는 역일 수 없다.")
    @Test
    void upStationDownStationRelation() {
        // 역 2개 생성 : 1L, 2L
        ExtractableResponse<Response> 수원역 = StationStep.saveStation("수원역"); // up
        ExtractableResponse<Response> 사당역 = StationStep.saveStation("사당역"); // down

        // 노선 생성
        LineStep.saveLine("파란색", "1호선", 1L, 2L, 10);

        // 구간 생성
        ExtractableResponse<Response> extract = SectionStep.saveSection(1L, 2L, 2, 1L);

        // 새로운 역 추가
        ExtractableResponse<Response> 성균관대역 = StationStep.saveStation("성균관대역"); // down

        // 새로운 구간 생성
        ExtractableResponse<Response> extract_2 = SectionStep.saveSection(1L, 3L, 1, 1L);

        assertThat(extract_2.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @DisplayName("새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야한다.")
    @Test
    void newUpStationMustBeDownStation() {
        // 역 2개 생성 : 1L, 2L
        ExtractableResponse<Response> 수원역 = StationStep.saveStation("수원역"); // up
        ExtractableResponse<Response> 사당역 = StationStep.saveStation("사당역"); // down

        // 노선 생성
        LineStep.saveLine("파란색", "1호선", 1L, 2L, 10);

        // 구간 생성
        ExtractableResponse<Response> extract = SectionStep.saveSection(1L, 2L, 2, 1L);

        // 새로운 상행역 2개
        ExtractableResponse<Response> 성균관대역 = StationStep.saveStation("성균관대역");
        ExtractableResponse<Response> 돼지역 = StationStep.saveStation("돼지역");

        // 새로운 구간 생성
        ExtractableResponse<Response> extract_2 = SectionStep.saveSection(3L, 4L, 1, 1L);

        assertThat(extract_2.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
