package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.linestep.LineRequestStep.*;
import static nextstep.subway.acceptance.linestep.LineValidateStep.*;
import static nextstep.subway.acceptance.testenum.TestLine.*;

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
        ExtractableResponse<Response> response = 노선_생성(LINE_NEW_BOONDANG);

        // then
        응답_상태코드_검증(response, HttpStatus.CREATED);
        응답_바디_각_요소_검증(response, LINE_NEW_BOONDANG.getName(), LINE_NEW_BOONDANG.getColor());
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
        노선_생성(LINE_NEW_BOONDANG);
        // given
        노선_생성(LINE_TWO);

        // when
        ExtractableResponse<Response> response = 노선_목록_조회();

        // then
        응답_상태코드_검증(response, HttpStatus.OK);
        응답_바디_여러_요소_검증(response);
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
        Long lineId = extractId(노선_생성(LINE_NEW_BOONDANG));

        // when
        ExtractableResponse<Response> response = 노선_조회(lineId);

        // then
        응답_상태코드_검증(response, HttpStatus.OK);
        응답_바디_각_요소_검증(response, LINE_NEW_BOONDANG.getName(), LINE_NEW_BOONDANG.getColor());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        String modifyName = "구분당선";
        String modifyColor = "bg-blue-600";

        // given
        Long modifiedId = extractId(노선_생성(LINE_NEW_BOONDANG));

        // when
        ExtractableResponse<Response> response = 노선_변경(modifyName, modifyColor, modifiedId);

        // then
        응답_상태코드_검증(response, HttpStatus.OK);
        응답_바디_각_요소_검증(노선_조회(modifiedId), modifyName, modifyColor);
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
        Long deletedId = extractId(노선_생성(LINE_NEW_BOONDANG));

        // when
        ExtractableResponse<Response> response = 노선_삭제(deletedId);

        // then
        응답_상태코드_검증(response, HttpStatus.NO_CONTENT);
    }

    /**
     *  Scenario: 지하철 노선의 이름 중복을 검증한다.
     *  given   : 지하철 노선 생성을 요청하고
     *  when    : 같은 이름의 노선 생성을 다시 요청하면,
     *  then    : 두번째 노선은 생성되지 않는다. (409)
     */
    @DisplayName("지하철 노선 중복 검증")
    @Test
    void validateLineName() {
        // given
        노선_생성(LINE_NEW_BOONDANG);

        // when
        ExtractableResponse<Response> duplicatedLineResponse = 노선_생성(LINE_NEW_BOONDANG);

        // then
        응답_상태코드_검증(duplicatedLineResponse, HttpStatus.CONFLICT);
    }
}
