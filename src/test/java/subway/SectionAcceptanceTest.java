package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.extracts.LineExtracts.지하철노선_생성_응답_ID_추출;
import static subway.extracts.LineExtracts.지하철노선_조회_역_ID_추출;
import static subway.extracts.ResponseExtracts.상태코드;
import static subway.extracts.StationExtracts.지하철역_생성_응답_ID_추출;
import static subway.requests.LineRequests.지하철노선_생성_요청하기;
import static subway.requests.LineRequests.지하철노선_조회_요청하기;
import static subway.requests.SectionRequests.지하철구간_삭제_요청하기;
import static subway.requests.SectionRequests.지하철구간_추가_요청하기;
import static subway.requests.StationRequests.지하철역_생성_요청하기;

@DisplayName("지하철구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    /**
     * Given 지하철 노선이 존재할 때
     * When 해당 노선에 지하철 구간을 추가하면
     * Then 지하철 노선 목록 조회 시 추가한 구간의 역을 찾을 수 있다.
     */
    @DisplayName("지하철구간을 추가한다.")
    @Test
    void addSection() {
        // given
        Long 강남역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기("강남역"));
        Long 양재역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기("양재역"));
        Long 양재시민의숲역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기("양재시민의숲역"));
        String lineName = "신분당선";

        Long 신분당선_ID = 지하철노선_생성_응답_ID_추출(지하철노선_생성_요청하기(lineName, 강남역_ID, 양재역_ID));

        // when
        ExtractableResponse<Response> 지하철구간_추가_응답 = 지하철구간_추가_요청하기(신분당선_ID, 양재역_ID, 양재시민의숲역_ID, 10);

        // then
        assertThat(상태코드(지하철구간_추가_응답)).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(지하철노선_조회_역_ID_추출(지하철노선_조회_요청하기(신분당선_ID))).contains(양재시민의숲역_ID);
    }

    /**
     * Given 지하철 노선이 존재할 때
     * When 지하철 구간 추가 시 해당 구간의 상행선이 노선의 하행 종점역이 아닌 경우
     * Then 구간 추가할 수 없다.
     */
    @DisplayName("지하철 구간 추가 시 해당 구간의 상행선이 노선의 하행 종점역이 아닌 경우")
    @Test
    void addSectionMismatchStation() {
        // given
        Long 강남역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기("강남역"));
        Long 양재역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기("양재역"));
        Long 양재시민의숲역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기("양재시민의숲역"));
        String lineName = "신분당선";

        Long 신분당선_ID = 지하철노선_생성_응답_ID_추출(지하철노선_생성_요청하기(lineName, 강남역_ID, 양재역_ID));

        // when
        ExtractableResponse<Response> 지하철구간_추가_응답 = 지하철구간_추가_요청하기(신분당선_ID, 강남역_ID, 양재시민의숲역_ID, 10);

        // then
        assertThat(상태코드(지하철구간_추가_응답)).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Given 지하철 노선이 존재할 때
     * When 지하철 구간 추가 시 해당 구간의 하행선이 노선에 이미 존재하는 역인 경우
     * Then 구간 추가할 수 없다.
     */
    @DisplayName("지하철 구간 추가 시 해당 구간의 하행선이 노선에 이미 존재하는 역인 경우")
    @Test
    void addSectionAlreadyExistsStation() {
        // given
        Long 강남역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기("강남역"));
        Long 양재역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기("양재역"));
        String lineName = "신분당선";

        Long 신분당선_ID = 지하철노선_생성_응답_ID_추출(지하철노선_생성_요청하기(lineName, 강남역_ID, 양재역_ID));

        // when
        ExtractableResponse<Response> 지하철구간_추가_응답 = 지하철구간_추가_요청하기(신분당선_ID, 양재역_ID, 강남역_ID, 10);

        // then
        assertThat(상태코드(지하철구간_추가_응답)).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }


    /**
     * Given 지하철 노선이 존재할 때
     * When 해당 노선에 지하철 구간을 삭제하면
     * Then 지하철 노선 목록 조회 시 삭제한 구간의 하행선을 찾을 수 없다.
     */
    @DisplayName("지하철구간을 삭제한다.")
    @Test
    void deleteSection() {
        // given
        Long 강남역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기("강남역"));
        Long 양재역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기("양재역"));
        Long 양재시민의숲역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기("양재시민의숲역"));
        String lineName = "신분당선";

        Long 신분당선_ID = 지하철노선_생성_응답_ID_추출(지하철노선_생성_요청하기(lineName, 강남역_ID, 양재역_ID));
        지하철구간_추가_요청하기(신분당선_ID, 양재역_ID, 양재시민의숲역_ID, 10);

        // when
        ExtractableResponse<Response> 지하철구간_삭제_응답 = 지하철구간_삭제_요청하기(신분당선_ID, 양재시민의숲역_ID);

        // then
        assertThat(상태코드(지하철구간_삭제_응답)).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(지하철노선_조회_역_ID_추출(지하철노선_조회_요청하기(신분당선_ID)))
                .contains(강남역_ID, 양재역_ID)
                .doesNotContain(양재시민의숲역_ID);
    }


    /**
     * Given 지하철 노선이 존재할 때
     * When 지하철 구간 삭제 시 해당 역이 하행 종점역이 아닌 경우
     * Then 구간 삭제할 수 없다.
     */
    @DisplayName("지하철 구간 삭제 시 해당 역이 하행 종점역이 아닌 경우")
    @Test
    void deleteSectionNotLastStation() {
        // given
        Long 강남역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기("강남역"));
        Long 양재역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기("양재역"));
        Long 양재시민의숲역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기("양재시민의숲역"));
        String lineName = "신분당선";

        Long 신분당선_ID = 지하철노선_생성_응답_ID_추출(지하철노선_생성_요청하기(lineName, 강남역_ID, 양재역_ID));
        지하철구간_추가_요청하기(신분당선_ID, 양재역_ID, 양재시민의숲역_ID, 10);

        // when
        ExtractableResponse<Response> 지하철구간_삭제_응답 = 지하철구간_삭제_요청하기(신분당선_ID, 양재역_ID);

        // then
        assertThat(상태코드(지하철구간_삭제_응답)).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Given 지하철 노선이 존재할 때
     * When 지하철 구간 삭제 시 해당 노선에 구간이 1개인 경우
     * Then 구간 삭제할 수 없다.
     */
    @DisplayName("지하철 구간 삭제 시 해당 역이 하행 종점역이 아닌 경우")
    @Test
    void deleteSectionOnlyOneSection() {
        // given
        Long 강남역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기("강남역"));
        Long 양재역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기("양재역"));
        String lineName = "신분당선";

        Long 신분당선_ID = 지하철노선_생성_응답_ID_추출(지하철노선_생성_요청하기(lineName, 강남역_ID, 양재역_ID));

        // when
        ExtractableResponse<Response> 지하철구간_삭제_응답 = 지하철구간_삭제_요청하기(신분당선_ID, 양재역_ID);

        // then
        assertThat(상태코드(지하철구간_삭제_응답)).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
