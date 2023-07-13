package subway.line.section;

import static common.Constants.또다른지하철역;
import static common.Constants.새로운지하철역;
import static common.Constants.신분당선;
import static common.Constants.지하철역;
import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.LineTestStepDefinition.지하철_노선_생성_요청;
import static subway.line.LineTestStepDefinition.지하철_노선_조회_요청;
import static subway.line.section.SectionTestStepDefinition.지하철_구간_생성_요청;
import static subway.line.section.SectionTestStepDefinition.지하철_구간_생성_요청_상태_코드_반환;
import static subway.line.section.SectionTestStepDefinition.지하철_구간_제거_요청;
import static subway.line.section.SectionTestStepDefinition.지하철_구간_제거_요청_상태_코드_반환;
import static subway.station.StationTestStepDefinition.지하철_역_생성_요청;

import common.AcceptanceTest;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.line.LineResponse;
import subway.station.Station;

@DisplayName("지하철 구간 관련 기능")
@AcceptanceTest
public class SectionAcceptanceTest {
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
        var lineResponse = 지하철_노선_조회_요청(lineCreateResponse.getId());
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
        var statusCode = 지하철_구간_생성_요청_상태_코드_반환(lineCreateResponse.getId(), getUpEndStationId(lineCreateResponse),
            stationResponse.getId());

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value());
        var lineResponse = 지하철_노선_조회_요청(lineCreateResponse.getId());
        assertThat(getStationNames(lineResponse)).containsExactly(지하철역, 새로운지하철역);
    }

    // Given 지하철 노선을 생성하고
    // When 해당 노선에 이미 등록된 역을 하행선으로 가지는 구간을 추가하면
    // Then 응답에서 400 BAD_REQUEST 상태코드를 받는다
    @DisplayName("지하철 노선에 구간 추가시 해당 노선에 이미 등록된 역을 하행선으로 가지면 실패한다.")
    @Test
    void createSection_fail_anyStationOfSectionDoesNotMatchWithDownEndStationOfLine() {
        // given
        var lineCreateResponse = 지하철_노선_생성_요청(신분당선, "bg-red-600", 지하철역, 새로운지하철역, 10);

        // when
        var statusCode = 지하철_구간_생성_요청_상태_코드_반환(lineCreateResponse.getId(), getDownEndStationId(lineCreateResponse), getUpEndStationId(lineCreateResponse));

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value());
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
        var statusCode = 지하철_구간_제거_요청_상태_코드_반환(lineCreateResponse.getId(),
            getUpEndStationId(lineCreateResponse));

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value());
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
