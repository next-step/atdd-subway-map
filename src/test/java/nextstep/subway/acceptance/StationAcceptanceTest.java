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

    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";


    /**
     * When 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 성공한다.
     */
    @DisplayName("지하철역 생성")
    @Test
    void createStation() {
        // given
        final Map<String, String> params = Station_데이터_생성(강남역);

        // when
        ExtractableResponse<Response> response = Station_생성_요청(params);

        // then
        생성요청한_지하철역이_생성됨(response);
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
        final List<Map<String, String>> params = Station_데이터_생성(Arrays.asList(강남역, 역삼역));
        final List<ExtractableResponse<Response>> requestList = Station_생성_요청(params);

        // when
        ExtractableResponse<Response> responseList = Station_목록_요청();

        // then
        생성요청한_지하철역들과_생성된_지하철역_목록이_동일함(requestList, responseList);
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
        Map<String, String> params = Station_데이터_생성(강남역);
        ExtractableResponse<Response> createResponse = Station_생성_요청(params);

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> deleteResponse = Station_삭제_요청(uri);

        // then
        삭제요청한_지하철_역이_존재하지_않음(deleteResponse);
    }


    /**
     * Given 지하철역 생성을 요청 하고
     * When 같은 이름으로 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 실패한다.
     */
    @DisplayName("중복이름으로 지하철역 생성")
    @Test
    void duplicateCheck() {
        // given
        Map<String, String> params = Station_데이터_생성(강남역);
        Station_생성_요청(params);

        // when
        ExtractableResponse<Response> duplicateResponse = Station_생성_요청(params);

        // then
        중복이름으로_지하철_역_생성_실패함(duplicateResponse);
    }
}
