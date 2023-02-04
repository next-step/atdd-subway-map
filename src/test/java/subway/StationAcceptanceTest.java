package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.extracts.ResponseExtracts.상태코드;
import static subway.extracts.StationExtracts.지하철역_목록_조회_응답_역_이름_추출;
import static subway.extracts.StationExtracts.지하철역_생성_응답_ID_추출;
import static subway.requests.StationRequests.*;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given
        String stationName = "강남역";

        // when
        ExtractableResponse<Response> 지하철역_생성_응답 = 지하철역_생성_요청하기(stationName);

        // then
        assertThat(상태코드(지하철역_생성_응답)).isEqualTo(HttpStatus.CREATED);
        assertThat(지하철역_목록_조회_응답_역_이름_추출(지하철역_목록_조회_요청하기())).containsAnyOf(stationName);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void fetchStations() {
        // given
        var stationNames = List.of("강남역", "신사역");
        stationNames.forEach(it -> 지하철역_생성_요청하기(it));

        // when
        ExtractableResponse<Response> 지하철역_목록_조회_응답 = 지하철역_목록_조회_요청하기();

        // then
        assertThat(상태코드(지하철역_목록_조회_응답)).isEqualTo(HttpStatus.OK);
        assertThat(지하철역_목록_조회_응답_역_이름_추출(지하철역_목록_조회_응답))
                .hasSize(2)
                .containsExactlyElementsOf(stationNames);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        // given
        String 지하철역_이름 = "강남역";
        Long 지하철역_ID = 지하철역_생성_응답_ID_추출(지하철역_생성_요청하기(지하철역_이름));

        // when
        ExtractableResponse<Response> 지하철역_삭제_응답 = 지하철역_삭제_요청하기(지하철역_ID);

        // then
        assertThat(상태코드(지하철역_삭제_응답)).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(지하철역_목록_조회_응답_역_이름_추출(지하철역_목록_조회_요청하기())).doesNotContain(지하철역_이름);
    }
}
