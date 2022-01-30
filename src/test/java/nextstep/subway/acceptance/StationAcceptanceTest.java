package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.step.CommonSteps;
import nextstep.subway.acceptance.step.StationSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {
    final String 이름 = "name";

    final Map<String, String> 강남역 = Map.of(이름, "강남역");
    final Map<String, String> 역삼역 = Map.of(이름, "역삼역");

    /**
     * When 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 성공한다.
     */
    @DisplayName("지하철역 생성")
    @Test
    void createStation() {
        // given & when
        ExtractableResponse<Response> response = StationSteps.지하철_역_생성_요청(강남역);

        // then
        CommonSteps.생성_성공(response.statusCode());
        assertThat(response.header("Location")).isNotBlank();
    }

    /**
     * When 중복된 지하철 역 생성을 요청 하면
     * Then 지하철역 생성이 실패한다.
     */
    @DisplayName("중복된 지하철 역 생성")
    @Test
    void createDuplicateStation() {
        StationSteps.지하철_역_생성_요청(강남역);

        // given & when
        ExtractableResponse<Response> createResponse = StationSteps.지하철_역_생성_요청(강남역);

        // then
        CommonSteps.데이터_기존재로_실패(createResponse.statusCode());
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
        StationSteps.지하철_역_생성_요청(강남역);
        StationSteps.지하철_역_생성_요청(역삼역);

        // when
        ExtractableResponse<Response> searchResponse = StationSteps.지하철_역_조회_요청();

        CommonSteps.요청_성공(searchResponse.statusCode());
        List<String> stationNames = searchResponse.jsonPath().getList(이름);
        assertThat(stationNames).contains(강남역.get(이름), 역삼역.get(이름));
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
        ExtractableResponse<Response> createResponse = StationSteps.지하철_역_생성_요청(강남역);

        // when
        final String uri = createResponse.header("Location");
        ExtractableResponse<Response> deleteResponse = StationSteps.지하철_역_삭제_요청(uri);

        // then
        CommonSteps.요청_성공_컨텐츠_미제공(deleteResponse.statusCode());
    }
}
