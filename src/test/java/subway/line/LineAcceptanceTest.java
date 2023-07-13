package subway.line;

import common.AcceptanceTest;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.station.Station;
import subway.station.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static common.Constants.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.station.StationTestStepDefinition.*;
import static subway.line.LineTestStepDefinition.*;

@DisplayName("지하철 노선 관련 기능")
@AcceptanceTest
public class LineAcceptanceTest {

    // When 지하철 노선을 생성하면
    // Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        var response = 지하철_노선_생성_요청(신분당선, "bg-red-600", 지하철역, 새로운지하철역, 10);

        // then
        assertAll(
            () -> assertThat(response.getId()).isNotNull(),
            () -> assertThat(response.getName()).isEqualTo(신분당선),
            () -> assertThat(response.getColor()).isEqualTo("bg-red-600"),
            () -> assertThat(getStationNames(response)).containsExactly(지하철역, 새로운지하철역)
        );
    }

    // Given 2개의 지하철 노선을 생성하고
    // When 지하철 노선 목록을 조회하면
    // Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLineList() {
        // given
        StationResponse someStationResponse = 지하철_역_생성_요청(지하철역);
        StationResponse newStationResponse = 지하철_역_생성_요청(새로운지하철역);
        StationResponse anotherStationResponse = 지하철_역_생성_요청(또다른지하철역);

        var sinBundangLineResponse = 지하철_노선_생성_요청(신분당선, "bg-red-600",
            someStationResponse.getId(),
            newStationResponse.getId(), 10);
        var bundangLineResponse = 지하철_노선_생성_요청(분당선, "bg-green-600",
            someStationResponse.getId(),
            anotherStationResponse.getId(), 10);

        // when
        var response = 지하철_노선_목록_조회_요청();

        // then
        assertThat(response).containsExactly(sinBundangLineResponse, bundangLineResponse);
    }

    // Given 지하철 노선을 생성하고
    // When 생성한 지하철 노선을 조회하면
    // Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given & when
        var response = 지하철_노선_생성_요청(신분당선, "bg-red-600", 지하철역, 새로운지하철역, 10);

        // then
        assertAll(
            () -> assertThat(response.getId()).isNotNull(),
            () -> assertThat(response.getName()).isEqualTo(신분당선),
            () -> assertThat(response.getColor()).isEqualTo("bg-red-600"),
            () -> assertThat(getStationNames(response)).containsExactly(지하철역, 새로운지하철역)
        );
    }

    // TODO: PATCH인데 PUT으로 변경해야 함
    // Given 지하철 노선을 생성하고
    // When 생성한 지하철 노선을 수정하면
    // Then 해당 지하철 노선 정보는 수정된다
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        var lineResponse = 지하철_노선_생성_요청(신분당선, "bg-red-600", 지하철역, 새로운지하철역, 10);

        // when
        지하철_노선_수정_요청(lineResponse.getId(), "다른분당선", "bg-red-600");

        // then
        var updateResponse = 지하철_노선_조회_요청(lineResponse.getId());
        assertAll(
            () -> assertThat(updateResponse.getName()).isEqualTo("다른분당선"),
            () -> assertThat(updateResponse.getColor()).isEqualTo("bg-red-600"));
    }

    // Given 지하철 노선을 생성하고
    // When 생성한 지하철 노선을 삭제하면
    // Then 해당 지하철 노선 정보는 삭제된다
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        var lineResponse = 지하철_노선_생성_요청(신분당선, "bg-red-600", 지하철역, 새로운지하철역, 10);

        // when
        지하철_노선_삭제_요청(lineResponse.getId());

        // then
        없는_지하철_노선_조회_요청(lineResponse.getId());
    }

    private Stream<String> getStationNames(LineResponse response) {
        return response.getStations().stream().map(Station::getName);
    }
}
