package nextstep.subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static nextstep.subway.station.StationSteps.*;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    public static Map<String, String> 강남역 = makeStation("강남역");
    public static Map<String, String> 역삼역 = makeStation("역삼역");
    public static Map<String, String> 선릉역 = makeStation("선릉역");
    public static Map<String, String> 삼성역 = makeStation("삼성역");

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = 지하철_역_생성_요청(강남역);

        // then
        지하철_역_생성_성공(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicatedName() {
        // given
        지하철_역_생성_요청(강남역);

        // when
        ExtractableResponse<Response> response = 지하철_역_생성_요청(강남역);

        // then
        지하철_역_생성_실패(response);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        /// given
        ExtractableResponse<Response> createResponse1 = 지하철_역_생성_요청(강남역);
        ExtractableResponse<Response> createResponse2 = 지하철_역_생성_요청(역삼역);

        // when
        ExtractableResponse<Response> response = 지하철_역_조회();

        // then
        지하철_생성한_역_목록_조회_성공(Stream.of(createResponse1, createResponse2), response);
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_역_생성_요청(강남역);

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = 지하철_역_삭제(uri);

        // then
        지하철_역_삭제_성공(response);
    }

    public static Map<String, String> makeStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return params;
    }

}
