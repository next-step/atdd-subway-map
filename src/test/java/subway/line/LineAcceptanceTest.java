package subway.line;

import common.AcceptanceTest;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import subway.station.Station;
import subway.station.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static common.Constants.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.line.SectionTestStepDefinition.지하철_구간_생성_요청;
import static subway.line.SectionTestStepDefinition.지하철_구간_생성_요청_상태_코드_반환;
import static subway.line.SectionTestStepDefinition.지하철_구간_제거_요청;
import static subway.line.SectionTestStepDefinition.지하철_구간_제거_요청_상태_코드_반환;
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

    // Given 지하철 노선을 생성하고
    // When 지하철 노선에 구간을 추가하면
    // Then 지하철 노선 조회시 추가한 구간이 포함되어있다.
    @DisplayName("지하철 노선에 구간을 추가한다.")
    @Test
    void createSection() {
        // given
        var lineCreateResponse = 지하철_노선_생성_요청(신분당선, "bg-red-600", 지하철역, 새로운지하철역, 10);
        var stationResponse = 지하철_역_생성_요청(또다른지하철역);

        // when
        지하철_구간_생성_요청(lineCreateResponse.getId(), getDownEndStationId(lineCreateResponse),
            stationResponse.getId());

        // then
        var lineResponse = 지하철_노선_조회_요청(stationResponse.getId());
        assertThat(getStationNames(lineResponse)).containsExactly(지하철역, 새로운지하철역, 또다른지하철역);
    }

    // Given 지하철 노선을 생성하고
    // When 해당 노선의 하행 종점역이 아닌 역을 상행선으로 가지는 구간을 추가하면
    // Then 지하철 노선 조회시 추가한 구간을 확인할 수 없다
    @DisplayName("지하철 노선에 구간 추가시 해당 노선의 하행 종점이 아닌 역을 상행선으로 가진다면 실패한다.")
    @Test
    void createSection_fail_upStationOfSectionDoesNotMatchWithDownEndStationOfLine() {
        // given
        var lineCreateResponse = 지하철_노선_생성_요청(신분당선, "bg-red-600", 지하철역, 새로운지하철역, 10);
        var stationResponse = 지하철_역_생성_요청(또다른지하철역);

        // when
        지하철_구간_생성_요청(lineCreateResponse.getId(), getUpEndStationId(lineCreateResponse),
            stationResponse.getId());

        // then
        var lineResponse = 지하철_노선_조회_요청(stationResponse.getId());
        assertThat(getStationNames(lineResponse)).containsExactly(지하철역, 새로운지하철역);
    }

    // Given 지하철 노선을 생성하고
    // When 해당 노선에 이미 등록된 역을 하행선으로 가지는 구간을 추가하면
    // Then 응답에서 409 Conflict 상태코드를 받는다
    @DisplayName("지하철 노선에 구간 추가시 해당 노선에 이미 등록된 역을 하행선으로 가지면 실패한다.")
    @Test
    void createSection_fail_anyStationOfSectionDoesNotMatchWithDownEndStationOfLine() {
        // given
        var lineCreateResponse = 지하철_노선_생성_요청(신분당선, "bg-red-600", 지하철역, 새로운지하철역, 10);

        // when
        var statusCode = 지하철_구간_생성_요청_상태_코드_반환(lineCreateResponse.getId(), getDownEndStationId(lineCreateResponse), getUpEndStationId(lineCreateResponse));

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.CONFLICT.value());
    }

    // Given 지하철 노선에 구간을 추가하고
    // When 추가한 구간을 제거 요청하면
    // Then 지하철 노선 조회시 추가한 구간이 제거된 것을 확인할 수 있다.
    @DisplayName("지하철 노선에 구간을 제거한다.")
    @Test
    void deleteSection() {
        // given
        var lineCreateResponse = 지하철_노선_생성_요청(신분당선, "bg-red-600", 지하철역, 새로운지하철역, 10);
        var stationResponse = 지하철_역_생성_요청(또다른지하철역);
        지하철_구간_생성_요청(lineCreateResponse.getId(), getDownEndStationId(lineCreateResponse),
            stationResponse.getId());

        // when
        지하철_구간_제거_요청(lineCreateResponse.getId(), stationResponse.getId());

        // then
        var lineResponse = 지하철_노선_조회_요청(lineCreateResponse.getId());
        assertThat(getStationNames(lineResponse)).containsExactly(지하철역, 새로운지하철역);
    }

    // Given 지하철 노선에 구간을 추가하고
    // When 하행 종점역이 아닌 역을 제거 요청하면
    // Then 지하철 노선 조회시 추가한 구역이 제거되지 않은 것을 확인할 수 있다.
    @DisplayName("지하철 노선에 구간 제거시 하행 종점역이 아닌 역을 제거 요청하면 실패한다.")
    @Test
    void deleteSection_fail_sectionDoesNotMatchWithDownEndStationOfLine() {
        // given
        var lineCreateResponse = 지하철_노선_생성_요청(신분당선, "bg-red-600", 지하철역, 새로운지하철역, 10);
        var stationResponse = 지하철_역_생성_요청(또다른지하철역);
        지하철_구간_생성_요청(lineCreateResponse.getId(), getDownEndStationId(lineCreateResponse),
            stationResponse.getId());

        // when
        지하철_구간_제거_요청(lineCreateResponse.getId(), getUpEndStationId(lineCreateResponse));

        // then
        var lineResponse = 지하철_노선_조회_요청(lineCreateResponse.getId());
        assertThat(getStationNames(lineResponse)).containsExactly(지하철역, 새로운지하철역, 또다른지하철역);
    }

    // Given 지하철 노선을 생성하고
    // When 상행 종점역과 하행 종점역만 있는 노선에 구간 삭제 요청하면
    // Then 지하철 노선 조회시 추가한 구역이 제거되지 않은 것을 확인할 수 있다.
    @DisplayName("지하철 노선에 구간 제거시 상행 종점역과 하행 종점역만 있는 노선에 구간이라면 실패한다.")
    @Test
    void deleteSection_fail_sectionOnlyExistsUpAndDownEndStationInLine() {
        // given
        var lineCreateResponse = 지하철_노선_생성_요청(신분당선, "bg-red-600", 지하철역, 새로운지하철역, 10);

        // when
        var statusCode = 지하철_구간_제거_요청_상태_코드_반환(lineCreateResponse.getId(), getUpEndStationId(lineCreateResponse));

        // then
        var lineResponse = 지하철_노선_조회_요청(lineCreateResponse.getId());
        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(getStationNames(lineResponse)).containsExactly(지하철역, 새로운지하철역);
    }

    private Stream<String> getStationNames(LineResponse response) {
        return response.getStations().stream().map(Station::getName);
    }

    private Long getDownEndStationId(LineResponse response) {
        List<Station> stations = response.getStations();
        return stations.get(stations.size() - 1).getId();
    }

    private Long getUpEndStationId(LineResponse response) {
        return response.getStations().get(0).getId();
    }
}
