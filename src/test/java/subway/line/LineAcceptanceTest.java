package subway.line;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import subway.line.dto.LineResponse;

/**
 * 프로그래밍 요구사항
 * - 아래의 순서로 기능을 구현한다
 *   1. 인수 조건을 검증하는 인수 테스트를 작성한다
 *   2. 인수 테스트를 충족하는 기능을 구현한다
 * - 인수 테스트의 결과가 서로 영향을 끼치지 않도록 인수테스트를 서로 격리시킨다
 * - 인수 테스트의 재사용성과 가독성, 그리고 빠른 테스트 의도 파악을 위해 인수 테스트를 리팩터링한다
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
    /**
     * When: 지하철 노선을 생성하면
     * Then: 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        String lineName = "신분당선";
        ExtractableResponse<Response> responseOfCreate = LineRequest.지하철_노선을_생성한다("강남역", "역삼역", lineName);

        // then
        assertThat(responseOfCreate.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        long createdLineId = 지하철_노선_Id를_추출한다(responseOfCreate);
        ExtractableResponse<Response> responseOfRead = LineRequest.지하철_노선을_조회한다(createdLineId);

        long findLineId = 지하철_노선_Id를_추출한다(responseOfRead);
        String findLineName = 지하철_노선_이름을_추출한다(responseOfRead);

        assertThat(findLineId).isEqualTo(createdLineId);
        assertThat(findLineName).isEqualTo(lineName);
    }

    private long 지하철_노선_Id를_추출한다(ExtractableResponse<Response> responseOfCreateStation) {
        return responseOfCreateStation.jsonPath().getLong("id");
    }

    private String 지하철_노선_이름을_추출한다(ExtractableResponse<Response> responseOfRead) {
        return responseOfRead.jsonPath().getString("name");
    }

    /**
     * Given: 2개의 지하철 노선을 생성하고
     * When: 지하철 노선 목록을 조회하면
     * Then: 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void findAllLines() {
        // given
        LineRequest.지하철_노선을_생성한다("강남역", "양재역", "신분당선");
        LineRequest.지하철_노선을_생성한다("가양역", "여의도역", "9호선");

        // when
        ExtractableResponse<Response> response = LineRequest.지하철_노선_목록을_조회한다();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("", LineResponse.class)).hasSize(2);
    }

    /**
     * Given: 지하철 노선을 생성하고
     * When: 생성한 지하철 노선을 조회하면
     * Then: 생성한 지하철 노선의 정보를 응답받을 수 있다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void findLine() {
        // given
        ExtractableResponse<Response> responseOfCreateLine = LineRequest.지하철_노선을_생성한다("강남역", "양재역", "신분당선");

        // when
        ExtractableResponse<Response> responseOfFindLine = LineRequest.지하철_노선을_조회한다(지하철_노선_Id를_추출한다(responseOfCreateLine));

        // then
        assertThat(responseOfFindLine.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(지하철_노선_Id를_추출한다(responseOfFindLine)).isEqualTo(1L);
    }

    /**
     * Given: 지하철 노선을 생성하고
     * When: 생성한 지하철 노선을 수정하면
     * Then: 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> responseOfCreateLine = LineRequest.지하철_노선을_생성한다("강남역", "양재역", "신분당선");

        // when
        long lineId = 지하철_노선_Id를_추출한다(responseOfCreateLine);
        String lineNameForUpdate = "구분당선";
        String lineColorForUpdate = "bg-sky-500";
        ExtractableResponse<Response> responseOfUpdateLine = LineRequest.지하철_노선을_수정한다(lineId, lineNameForUpdate, lineColorForUpdate);

        // then
        assertThat(responseOfUpdateLine.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> responseOfShowLine = LineRequest.지하철_노선을_조회한다(lineId);
        assertThat(지하철_노선_이름을_추출한다(responseOfShowLine)).isEqualTo(lineNameForUpdate);
        assertThat(지하철_노선_색상을_추출한다(responseOfShowLine)).isEqualTo(lineColorForUpdate);
    }

    private String 지하철_노선_색상을_추출한다(ExtractableResponse<Response> responseOfShowLine) {
        return responseOfShowLine.jsonPath().getString("color");
    }

    /**
     * Given: 지하철 노선을 생성하고
     * When: 생성한 지하철 노선을 삭제하면
     * Then: 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> responseOfCreateLine = LineRequest.지하철_노선을_생성한다("강남역", "양재역", "신분당선");

        // when
        long lineId = 지하철_노선_Id를_추출한다(responseOfCreateLine);
        ExtractableResponse<Response> responseOfDelete = LineRequest.지하철_노선을_삭제한다(lineId);

        // then
        assertThat(responseOfDelete.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        ExtractableResponse<Response> responseOfShowLine = LineRequest.지하철_노선을_조회한다(lineId);
        assertThat(responseOfShowLine.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());  //TODO 없는 지하철 노선을 조회했으므로 예외가 발생할 것. 하지만 내가 ExceptionHandler를 따로 구현해주지 않았기 때문에 정해진 형식으로 내려오지 않는 문제가 있다. 어떻게 할지 고민 !
    }
}
