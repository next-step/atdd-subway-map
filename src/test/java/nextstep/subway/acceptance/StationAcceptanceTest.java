package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static nextstep.subway.utils.StationUtils.*;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 성공한다.
     */
    @DisplayName("지하철역 생성")
    @Test
    void createStation() {
        // given
        final Map<String, String> params = Station_데이터_생성("강남역");

        // when
        ExtractableResponse<Response> response = Station_생성_요청(params);

        // then
        Status가_CREATED면서_Location이_존재함(response);
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * Given 새로운 지하철역 생성을 요청 하고
     * When 지하철역 목록 조회를 요청 하면
     * Then 두 지하철역이 포함된 지하철역 목록을 응답받는다
     */
    @DisplayName("지하철역 목록 조회")
    @Test
    void getStations() {
        /// given
        final List<String> names = Arrays.asList("강남역", "역삼역");
        final List<Map<String, String>> params = Station_데이터_생성(names);

        for (Map<String, String> param : params ) {
            Station_생성_요청(param);
        }

        // when
        ExtractableResponse<Response> response = Station_목록_요청();

        // then
        Status가_OK면서_Station의_name_list가_일치함(response, names);
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 생성한 지하철역 삭제를 요청 하면
     * Then 생성한 지하철역 삭제가 성공한다.
     */
    @DisplayName("지하철역 삭제")
    @Test
    void deleteStation() {
        // given
        Map<String, String> params = Station_데이터_생성("강남역");
        ExtractableResponse<Response> createResponse = Station_생성_요청(params);

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> deleteResponse = Station_삭제_요청(uri);

        // then
        Status가_NO_CONTENT(deleteResponse);
    }
}
