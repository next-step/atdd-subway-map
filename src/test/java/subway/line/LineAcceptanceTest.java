package subway.line;

import common.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.station.Station;
import subway.station.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static common.Constants.*;
import static subway.station.StationTestFixture.*;
import static subway.line.LineTestFixture.*;

@DisplayName("지하철 노선도 관련 기능")
@AcceptanceTest
public class LineAcceptanceTest {

    // When 지하철 노선을 생성하면
    // Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // TODO: 인수테스트 작성하면 stations 등 연관되어 있는 것 매번 만드는지?
        // TODO: 성공하는 케이스만 인수테스트를 작성하는지?
        // TODO: 비즈니스 실패 케이스는 작성할 수도 있겠지만 4XX 5XX 에러는 어떻게 테스트하는지?
        // TODO: 뭔가 코드가 많이 지저분해졌는데 시나리오에 방해되는 메서드들을 private 메서드로 빼는게 좋을지? -> ~TestFixture에 RestAssured 관련 메서드만 넣을 필요는 없으니까?
        // TODO: 매 역 생성마다 status code를 확인하는게 좋을지?
        // given
        StationResponse someStationResponse = 지하철_역_생성_요청_역_정보_반환(지하철역);
        StationResponse newStationResponse = 지하철_역_생성_요청_역_정보_반환(새로운지하철역);
        LineRequest lineRequest = new LineRequest(신분당선, "bg-red-600", someStationResponse.getId(),
            newStationResponse.getId(), 10);
        List<Station> stations = toStationList(someStationResponse, newStationResponse);

        // when
        var response = 지하철_노선_생성_요청(lineRequest);
        LineResponse lineResponse = toLineResponse(response);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(lineResponse).isEqualTo(
            new LineResponse(lineResponse.getId(), 신분당선, "bg-red-600", stations));
    }

    // Given 2개의 지하철 노선을 생성하고
    // When 지하철 노선 목록을 조회하면
    // Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLineList() {
        // given
        StationResponse someStationResponse = 지하철_역_생성_요청_역_정보_반환(지하철역);
        StationResponse newStationResponse = 지하철_역_생성_요청_역_정보_반환(새로운지하철역);
        StationResponse anotherStationResponse = 지하철_역_생성_요청_역_정보_반환(또다른지하철역);

        LineRequest sinBundangLineRequest = new LineRequest(신분당선, "bg-red-600",
            someStationResponse.getId(),
            newStationResponse.getId(), 10);
        LineRequest bundangLineRequest = new LineRequest(분당선, "bg-green-600",
            someStationResponse.getId(),
            anotherStationResponse.getId(), 10);
        LineResponse sinBundangLineResponse = 지하철_노선_생성_요청_노선_정보_반환(sinBundangLineRequest);
        LineResponse bundangLineResponse = 지하철_노선_생성_요청_노선_정보_반환(bundangLineRequest);

        // when
        var response = 지하철_노선_목록_조회_요청_노선_목록_반환();

        // then
        assertThat(response).containsExactly(sinBundangLineResponse, bundangLineResponse);
    }

    // Given 지하철 노선을 생성하고
    // When 생성한 지하철 노선을 조회하면
    // Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        StationResponse someStationResponse = 지하철_역_생성_요청_역_정보_반환(지하철역);
        StationResponse newStationResponse = 지하철_역_생성_요청_역_정보_반환(새로운지하철역);
        List<Station> stations = toStationList(someStationResponse, newStationResponse);
        LineRequest lineRequest = new LineRequest(신분당선, "bg-red-600", someStationResponse.getId(),
            newStationResponse.getId(), 10);

        // when
        LineResponse lineResponse = 지하철_노선_생성_요청_노선_정보_반환(lineRequest);

        // then
        assertThat(lineResponse).isEqualTo(new LineResponse(lineResponse.getId(), 신분당선, "bg-red-600", stations));
    }

    // Given 지하철 노선을 생성하고
    // When 생성한 지하철 노선을 수정하면
    // Then 해당 지하철 노선 정보는 수정된다
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        LineRequest lineRequest = new LineRequest(신분당선, "bg-red-600", 1L, 2L, 10);
        지하철_노선_생성_요청(lineRequest);

        // when
        LineUpdateRequest lineUpdateRequest = new LineUpdateRequest("다른분당선", "bg-red-600");
        int statusCode = 지하철_노선_수정_요청_상태_코드_반환(1L, lineUpdateRequest);

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.OK.value());
    }

    // Given 지하철 노선을 생성하고
    // When 생성한 지하철 노선을 삭제하면
    // Then 해당 지하철 노선 정보는 삭제된다
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        LineRequest lineRequest = new LineRequest(신분당선, "bg-red-600", 1L, 2L, 10);
        지하철_노선_생성_요청(lineRequest);

        // when
        int statusCode = 지하철_노선_삭제_요청_상태_코드_반환(1L);

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private List<Station> toStationList(StationResponse... stationResponse) {
        return Arrays.stream(stationResponse)
            .map(it -> new Station(it.getId(), it.getName()))
            .collect(Collectors.toList());
    }

    private LineResponse toLineResponse(ExtractableResponse<Response> response) {
        return response.as(LineResponse.class);
    }
}
