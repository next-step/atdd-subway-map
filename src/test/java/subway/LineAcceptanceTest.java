package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.extracts.LineExtracts.*;
import static subway.extracts.ResponseExtracts.상태코드;
import static subway.extracts.StationExtracts.지하철역_생성_응답_ID_추출;
import static subway.requests.LineRequests.*;
import static subway.requests.StationRequests.지하철역_생성_요청하기;

@DisplayName("지하철노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {
        Long 강남역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기("강남역"));
        Long 양재역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기("양재역"));
        String lineName = "신분당선";

        // when
        ExtractableResponse<Response> 지하철노선_생성_응답 = 지하철노선_생성_요청하기(lineName, 강남역_ID, 양재역_ID);

        // then
        assertThat(상태코드(지하철노선_생성_응답)).isEqualTo(HttpStatus.CREATED);
        assertThat(지하철노선_목록_조회_응답_노선_이름_추출(지하철노선_목록_조회_요청하기())).contains(lineName);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void readLines() {
        // given
        Long 강남역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기("강남역"));
        Long 신논현역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기("신논현역"));
        Long 논현역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기("논현역"));

        지하철노선_생성_요청하기("신분당선", 강남역_ID, 신논현역_ID);
        지하철노선_생성_요청하기("신분당선", 신논현역_ID, 논현역_ID);

        // when
        ExtractableResponse<Response> 지하철노선_목록_조회_응답 = 지하철노선_목록_조회_요청하기();

        // then
        assertThat(지하철노선_목록_조회_응답_ID_추출(지하철노선_목록_조회_응답)).hasSize(2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답 받을 수 있다.
     */
    @DisplayName("지하철노선을 조회한다.")
    @Test
    void readLine() {
        // given
        String 노선_이름 = "신분당선";
        Long 강남역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기("강남역"));
        Long 신논현역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기("신논현역"));

        Long lineId = 지하철노선_생성_응답_ID_추출(지하철노선_생성_요청하기(노선_이름, 강남역_ID, 신논현역_ID));

        // when
        ExtractableResponse<Response> 지하철노선_조회_응답 = 지하철노선_조회_요청하기(lineId);

        // then
        assertThat(지하철노선_조회_응답_노선_이름_추출(지하철노선_조회_응답)).isEqualTo(노선_이름);
        assertThat(지하철노선_조회_역_ID_추출(지하철노선_조회_응답)).containsExactlyInAnyOrder(강남역_ID, 신논현역_ID);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선을 수정한다.")
    @Test
    void modifyLine() {
        // given
        Long 강남역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기("강남역"));
        Long 신논현역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기("신논현역"));

        Long 노선_ID = 지하철노선_생성_응답_ID_추출(지하철노선_생성_요청하기("신분당선", 강남역_ID, 신논현역_ID));

        String 새로운_노선_이름 = "다른분당선";

        // when
        ExtractableResponse<Response> 지하철노선_수정_응답 = 지하철노선_수정_요청하기(노선_ID, 새로운_노선_이름, "bg-blue-600");

        // then
        assertThat(상태코드(지하철노선_수정_응답)).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(지하철노선_조회_응답_노선_이름_추출(지하철노선_조회_요청하기(노선_ID))).isEqualTo(새로운_노선_이름);
    }


    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        Long 강남역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기("강남역"));
        Long 신논현역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기("신논현역"));

        Long 노선_ID = 지하철노선_생성_응답_ID_추출(지하철노선_생성_요청하기("신분당선", 강남역_ID, 신논현역_ID));

        // when
        ExtractableResponse<Response> 지하철역_삭제_응답 = 지하철노선_삭제_요청하기(노선_ID);

        // then
        assertThat(상태코드(지하철역_삭제_응답)).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(지하철노선_목록_조회_응답_ID_추출(지하철노선_목록_조회_요청하기()));
    }
}
