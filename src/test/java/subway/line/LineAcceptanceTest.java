package subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static subway.acceptance.AcceptanceTestBase.assertStatusCode;
import static subway.acceptance.ResponseParser.getIdFromResponse;
import static subway.acceptance.ResponseParser.getNameFromResponse;
import static subway.acceptance.ResponseParser.getNamesFromResponse;
import static subway.line.LineAcceptanceTestHelper.노선_단건조회_요청;
import static subway.line.LineAcceptanceTestHelper.노선_삭제_요청;
import static subway.line.LineAcceptanceTestHelper.노선_생성_요청;
import static subway.line.LineAcceptanceTestHelper.노선_수정_요청;
import static subway.line.LineAcceptanceTestHelper.노선_파라미터_생성;
import static subway.line.LineAcceptanceTestHelper.노선목록_조회_요청;
import static subway.line.LineAcceptanceTestHelper.노선수정_파라미터_생성;
import static subway.station.StationAcceptanceTestHelper.지하철_파라미터_생성;
import static subway.station.StationAcceptanceTestHelper.지하철역_생성_요청;

import java.util.HashMap;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.acceptance.AcceptanceTest;

@DisplayName("노선 관련 기능")
@AcceptanceTest
public class LineAcceptanceTest {
    final String 상행역 = "강남";
    final String 하행역 = "역삼역";
    @BeforeEach
    void setUp() {
        지하철역_생성_요청(지하철_파라미터_생성(상행역));
        지하철역_생성_요청(지하철_파라미터_생성(하행역));
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Test
    @DisplayName("지하철 노선 생성")
    void createLine() {
        // given
        HashMap<String, String> params = 노선_파라미터_생성("신분당선", "1", "2");

        // when
        var response = 노선_생성_요청(params);

        // then
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @Test
    @DisplayName("노선 목록 조회")
    void fineLine() {
        // given
        노선_생성_요청(노선_파라미터_생성("신분당선", "1", "2"));
        노선_생성_요청(노선_파라미터_생성("2호선", "1", "10"));

        // when
        var response = 노선목록_조회_요청();

        // then
        assertStatusCode(response, OK);
        assertThat(getNamesFromResponse(response).size()).isEqualTo(2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    @DisplayName("노선 단건 조회")
    void findLineDetail() {
        // given
        HashMap<String, String> params = 노선_파라미터_생성("신분당선", "1", "2");
        var createResponse = 노선_생성_요청(params);

        // when
        var response = 노선_단건조회_요청(createResponse);

        // then
        assertStatusCode(response, OK);
        assertThat(getNameFromResponse(response)).isEqualTo(params.get("name"));
    }


    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @Test
    @DisplayName("노선 수정")
    void modifyLine() {
        // given
        var createResponse = 노선_생성_요청(노선_파라미터_생성("신분당선", "1", "2"));
        Long id = getIdFromResponse(createResponse);
        HashMap<String, String> updateParam = 노선수정_파라미터_생성();

        // when
        var response = 노선_수정_요청(updateParam, id);

        // then
        assertStatusCode(response, OK);
        assertThat(getNameFromResponse(노선_단건조회_요청(createResponse))).isEqualTo(updateParam.get("name"));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @Test
    @DisplayName("노선 삭제")
    void removeLine() {
        // given
        var createResponse = 노선_생성_요청(노선_파라미터_생성("신분당선", "1", "2"));
        Long id = getIdFromResponse(createResponse);

        // when
        var response = 노선_삭제_요청(id);

        // then
        assertStatusCode(response, NO_CONTENT);
    }

}
