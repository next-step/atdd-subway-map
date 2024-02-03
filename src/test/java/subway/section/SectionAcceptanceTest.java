package subway.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static subway.acceptance.AcceptanceTestBase.assertStatusCode;
import static subway.acceptance.ResponseParser.getStringIdFromResponse;
import static subway.line.LineAcceptanceTestHelper.*;
import static subway.section.SectionAcceptanceTestHelper.*;
import static subway.station.StationAcceptanceTestHelper.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.acceptance.AcceptanceTest;

@DisplayName("구간 관련 기능")
@AcceptanceTest
public class SectionAcceptanceTest {
    final String 상행역 = "강남역";
    private String 상행ID;
    final String 하행역 = "역삼역";
    private String 하행ID;
    final String 신규역 = "선릉역";

    @BeforeEach
    void setup() {
        상행ID = getStringIdFromResponse(지하철역_생성_요청(지하철_파라미터_생성(상행역)));
        하행ID = getStringIdFromResponse(지하철역_생성_요청(지하철_파라미터_생성(하행역)));

        노선_생성_요청(노선_파라미터_생성("2호선", "1", "2"));
    }

    /**
     Given 지하철 노선이 생성되고,
     When 새로운 구간의 상행역이 해당 노선에 등록되어 있는 하행 종점역이라면,
     Then 새로운 구간이 성공적으로 등록된다
     */
    @Test
    @DisplayName("상행역이 종점역일 때 구간 등록")
    void registerSectionWithTerminalStation() {
        //given
        String 신규하행ID = getStringIdFromResponse(지하철역_생성_요청(지하철_파라미터_생성(신규역)));

        //when
        ExtractableResponse<Response> response = 구간_등록_요청(구간_파라미터_생성(하행ID, 신규하행ID));

        //then
        assertStatusCode(response, CREATED);
        assertThat(노선_단건조회_요청(response).jsonPath().getString("stations[1].id")).isEqualTo(신규하행ID);
    }

    /**
     Given 지하철 노선이 생성되고,
     When 새로운 구간의 상행역이 해당 노선에 등록되어 있는 하행 종점역이 아니라면,
     Then 에러 메시지가 반환된다.
     */
    @Test
    @DisplayName("상행역이 종점역이 아닐 때 구간 등록 에러")
    void registerSectionWithNonTerminalStation() {
        //given
        String 신규하행ID = getStringIdFromResponse(지하철역_생성_요청(지하철_파라미터_생성(신규역)));

        //when
        ExtractableResponse<Response> response = 구간_등록_요청(구간_파라미터_생성(상행ID, 신규하행ID));

        //then
        assertStatusCode(response, BAD_REQUEST);
        assertThat(노선_단건조회_요청(response).jsonPath().getString("stations[1].id")).isEqualTo(하행ID);
    }

    /**
     Given 지하철 노선이 생성되고,
     When 이미 해당 노선에 등록되어 있는 역이 새로운 구간의 하행역이라면,
     Then 에러 메시지가 반환된다.
     */
    @Test
    @DisplayName("하행역이 이미 등록된 역일 때 구간 등록 에러")
    void registerSectionWithExistingStation() {
        //when
        ExtractableResponse<Response> response = 구간_등록_요청(구간_파라미터_생성(하행ID, 상행ID));

        //then
        assertStatusCode(response, BAD_REQUEST);
        assertThat(노선_단건조회_요청(response).jsonPath().getString("stations[1].id")).isEqualTo(하행ID);
    }

    /**
     Given 지하철 노선이 생성되고,
     When 해당 노선에 등록된 하행 종점역을 제거하려고 하면,
     Then 해당 구간이 성공적으로 제거된다.
     */
    @Test
    @DisplayName("하행 종점역 제거")
    void removeTerminalStation() {
        //given
        String 신규하행ID = getStringIdFromResponse(지하철역_생성_요청(지하철_파라미터_생성(신규역)));
        ExtractableResponse<Response> response = 구간_등록_요청(구간_파라미터_생성(하행ID, 신규하행ID));

        HashMap<String, String> params = 구간제거_파라미터_생성(신규하행ID);

        //when
        ExtractableResponse<Response> removeResponse = 구간_제거_요청(params);

        //then
        assertStatusCode(removeResponse, NO_CONTENT);
        assertThat(노선_단건조회_요청(response).jsonPath().getString("stations[1].id")).isEqualTo(하행ID);
    }

    /**
     Given 지하철 노선이 생성되고,
     When 해당 노선에 등록된 하행 종점역이 아닌 역을 제거하려고 하면,
     Then 에러 메시지가 반환된다.
     */
    @Test
    @DisplayName("하행 종점역이 아닌 역 제거 시 에러")
    void removeNonTerminalStation() {
        String 신규하행ID = getStringIdFromResponse(지하철역_생성_요청(지하철_파라미터_생성(신규역)));
        ExtractableResponse<Response> response = 구간_등록_요청(구간_파라미터_생성(하행ID, 신규하행ID));

        HashMap<String, String> params = 구간제거_파라미터_생성(하행ID);

        //when
        ExtractableResponse<Response> removeResponse = 구간_제거_요청(params);

        //then
        assertStatusCode(removeResponse, BAD_REQUEST);
        assertThat(노선_단건조회_요청(response).jsonPath().getString("stations[1].id")).isEqualTo(신규하행ID);
    }

    /**
     Given 지하철 노선이 생성되고, 상행 종점역과 하행 종점역만 있는 경우,
     When 역을 제거하려고 하면,
     Then 에러 메시지가 반환된다.
     */
    @Test
    @DisplayName("구간이 1개인 경우 역 제거 시 에러")
    void removeStationWhenSingleSection() {
        //given
        HashMap<String, String> params = 구간제거_파라미터_생성(하행ID);

        //when
        ExtractableResponse<Response> response = 구간_제거_요청(params);

        //then
        assertStatusCode(response, BAD_REQUEST);
        assertThat(노선_단건조회_요청(response).jsonPath().getString("stations[1].id")).isEqualTo(하행ID);
    }
}
