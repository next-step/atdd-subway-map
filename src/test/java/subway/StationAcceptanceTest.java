package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static subway.StationAcceptanceTestHelper.*;

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
        // when
        final ExtractableResponse<Response> 지하철역_생성_응답 = 지하철역_생성_요청("강남역");

        // then
        지하철역이_정상적으로_생성되었는지_확인(지하철역_생성_응답, "강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void getStations() {
        // given
        지하철역_생성함("역삼역");
        지하철역_생성함("선릉역");

        // when
        final ExtractableResponse<Response> 지하철역_목록_조회_응답 = 지하철역_목록_조회_요청();

        // then
        지하철역들이_목록_안에_있는지_확인(지하철역_목록_조회_응답, "역삼역", "선릉역");
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
        final Long 강남역_ID = 지하철역_생성함("강남역");

        // when
        final ExtractableResponse<Response> 지하철역_삭제_응답 = 지하철역_삭제_요청(강남역_ID);

        // then
        지하철역이_정상적으로_삭제되었는지_확인(지하철역_삭제_응답, "강남역");
    }
}