package subway.station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static subway.acceptance.AcceptanceTestBase.assertStatusCode;
import static subway.acceptance.ResponseParser.getIdFromResponse;
import static subway.acceptance.ResponseParser.getNamesFromResponse;
import static subway.station.StationAcceptanceTestHelper.지하철역_생성_요청;
import static subway.station.StationAcceptanceTestHelper.지하철_파라미터_생성;
import static subway.station.StationAcceptanceTestHelper.지하철역_제거_요청;
import static subway.station.StationAcceptanceTestHelper.지하철역_조회_요청;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        String stationName = "강남역";
        Map<String, String> params = 지하철_파라미터_생성(stationName);

        ExtractableResponse<Response> response = 지하철역_생성_요청(params);

        // then
        assertStatusCode(response, CREATED);
        assertThat(getNamesFromResponse(지하철역_조회_요청())).containsAnyOf(stationName);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성

    @DisplayName("지하철역을 조회한다.")
    @Test
    void findStation() {
        //given
        String stationName = "강남역";
        Map<String, String> params = 지하철_파라미터_생성(stationName);

        지하철역_생성_요청(params);


        params.put("name", "신사역");

        지하철역_생성_요청(params);

        //when
        ExtractableResponse<Response> response = 지하철역_조회_요청();

        // then
        assertThat(getNamesFromResponse(response).size()).isEqualTo(2);
        assertStatusCode(response, OK);

    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철역을 제거한다.")
    @Test
    void removeStation() {
        //given
        String stationName = "강남역";
        ExtractableResponse<Response> createResponse = 지하철역_생성_요청(지하철_파라미터_생성(stationName));
        Long id = getIdFromResponse(createResponse);

        //when
        ExtractableResponse<Response> response = 지하철역_제거_요청(id);

        // then
        assertStatusCode(response, NO_CONTENT);
    }

}