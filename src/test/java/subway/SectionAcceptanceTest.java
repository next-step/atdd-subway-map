package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.controller.dto.LineCreateRequest;
import subway.controller.dto.LineResponse;
import subway.controller.dto.SectionCreateRequest;
import subway.controller.dto.StationResponse;
import subway.exception.ExceptionResponse;

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.http.HttpStatus.*;
import static subway.fixture.LineFixture.SHINBUNDANG_LINE;
import static subway.fixture.StationFixture.*;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    private Long GANGNAM_STATION_ID;
    private Long SEOLLEUNG_STATION_ID;
    private Long YANGJAE_STATION_ID;

    private Long SHINBUNDANG_LINE_ID;

    /**
     * GIVEN 지하철 역을 생성하고
     * GIVEN 노선을 생성한다
     */
    @BeforeEach
    void setFixture() {
        GANGNAM_STATION_ID = createStation(GANGNAM_STATION.toCreateRequest(), CREATED.value())
                .as(StationResponse.class).getId();

        SEOLLEUNG_STATION_ID = createStation(SEOLLEUNG_STATION.toCreateRequest(), CREATED.value())
                .as(StationResponse.class).getId();

        YANGJAE_STATION_ID = createStation(YANGJAE_STATION.toCreateRequest(), CREATED.value())
                .as(StationResponse.class).getId();

        LineCreateRequest request = SHINBUNDANG_LINE.toCreateRequest(GANGNAM_STATION_ID, SEOLLEUNG_STATION_ID);
        SHINBUNDANG_LINE_ID = createLine(request, CREATED.value())
                .as(LineResponse.class).getId();
    }

    /**
     * WHEN 새로운 지하철 구간 생성시 상행 지하철역과 하행 지하철역을 생성하지 않으면
     * Then 새로운 구간을 생성할 수 없다
     */
    @ParameterizedTest
    @MethodSource("provideBlankSectionCreateRequest")
    void 실패_새로운_지하철_구간_생성시_필수값을_모두_입력하지_않으면_예외가_발생한다(SectionCreateRequest request) {
        // when
        ExtractableResponse<Response> response = post("/lines/{lineId}/sections", request, BAD_REQUEST.value(), SHINBUNDANG_LINE_ID);

        // then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
    }

    private static Stream<Arguments> provideBlankSectionCreateRequest() {
        return Stream.of(
                Arguments.of(
                        SectionCreateRequest
                                .builder()
                                .upStationId(1L)
                                .build()),
                Arguments.of(SectionCreateRequest
                        .builder()
                        .downStationId(2L)
                        .build())
        );
    }

    /**
     * WHEN 새로운 지하철 구간의 상행역을 노선의 하행 종점역이 아닌 곳에 생성하면
     * Then 새로운 구간을 생성할 수 없다
     */
    @Test
    void 실패_새로운_구간_생성시_상행역을_노선의_하행_종점역에_생성하지_않으면_예외가_발생한다() {
        // given
        SectionCreateRequest request = sectionCreateRequest(GANGNAM_STATION_ID, YANGJAE_STATION_ID, 10);

        // when
        String message = post("/lines/{lineId}/sections", request, OK.value(), SHINBUNDANG_LINE_ID)
                .as(ExceptionResponse.class).getMessage();

        // then
        assertThat(message).isEqualTo("새로운 구간의 상행역은 노선의 하행 종점역에만 생성할 수 있습니다.");
    }

    /**
     * WHEN 새로운 지하철 구간의 하행역을 노선의 존재하는 역에 생성하면
     * Then 새로운 구간을 생성할 수 없다
     */
    @Test
    void 실패_새로운_구간_생성시_하행역을_노선의_존재하는_역에_생성하면_예외가_발생한다() {
        // given
        SectionCreateRequest request = sectionCreateRequest(SEOLLEUNG_STATION_ID, GANGNAM_STATION_ID, 10);

        // when
        String message = post("/lines/{lineId}/sections", request, OK.value(), SHINBUNDANG_LINE_ID)
                .as(ExceptionResponse.class).getMessage();

        // then
        assertThat(message).isEqualTo("새로운 구간의 하행역은 노선에 존재하는 역에 생성할 수 없습니다.");
    }

    /**
     * WHEN 새로운 지하철 구간의 상행역을 노선의 하행 종점역이 아닌 곳에 생성하면
     * Then 새로운 구간을 생성할 수 없다
     */
    @Test
    void 실패_새로운_구간_생성시_하행역을_노선의_하행역에_생성하면_예외가_발생한다() {
        // given
        SectionCreateRequest request = sectionCreateRequest(YANGJAE_STATION_ID, SEOLLEUNG_STATION_ID, 10);

        // when
        String message = post("/lines/{lineId}/sections", request, OK.value(), SHINBUNDANG_LINE_ID)
                .as(ExceptionResponse.class).getMessage();

        // then
        assertThat(message).isEqualTo("새로운 구간의 하행역은 노선에 존재하는 역에 생성할 수 없습니다.");
    }

    /**
     * WHEN 새로운 지하철 구간 생성시 노선의 길이 보다 작거나 같다면
     * Then 새로운 구간을 생성할 수 없다
     */
    @ParameterizedTest
    @ValueSource(ints = {10, 9})
    void 실패_새로운_지하철_구간_생성시_노선_길이보다_작거나_같다면_예외가_발생한다(int distance) {
        // given
        SectionCreateRequest request = sectionCreateRequest(SEOLLEUNG_STATION_ID, YANGJAE_STATION_ID, distance);

        // when
        String message = post("/lines/{lineId}/sections", request, OK.value(), SHINBUNDANG_LINE_ID)
                .as(ExceptionResponse.class).getMessage();

        // then
        assertThat(message).isEqualTo("새로운 구간의 길이는 노선의 길이 보다 작거나 같을 수 없습니다.");
    }

    /**
     * WHEN 새로운 지하철 구간 생성시 노선의 하행 종점역에 생성하면
     * Then 새로운 구간이 생성된다
     */
    @Test
    void 성공_새로운_지하철_구간_생성시_노선의_하행_종점역에_생성할_수_있다() {
        // given
        SectionCreateRequest request = sectionCreateRequest(SEOLLEUNG_STATION_ID, YANGJAE_STATION_ID, 13);

        // when
        post("/lines/{lineId}/sections", request, CREATED.value(), SHINBUNDANG_LINE_ID);

        // then
        LineResponse response = findLine(SHINBUNDANG_LINE_ID, OK.value()).as(LineResponse.class);
        assertThat(response.getStations()).hasSize(3)
                .extracting("id", "name")
                .containsExactly(
                        tuple(1L, "강남역"),
                        tuple(2L, "선릉역"),
                        tuple(3L, "양재역")
                );
    }

    private SectionCreateRequest sectionCreateRequest(long upStationId, long downStationId, int distance) {
        return SectionCreateRequest.builder()
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(distance)
                .build();
    }

    /**
     * GIVEN 구간을 생성하고
     * WHEN 지하철 구간 제거시 마지막 구간이 아닐 경우
     * Then 구간을 제거할 수 없다
     */
    @Test
    void 실패_지하철_구간_제거시_마지막_구간이_아닐경우_예외가_발생한다() {
        // given
        SectionCreateRequest request = sectionCreateRequest(SEOLLEUNG_STATION_ID, YANGJAE_STATION_ID, 13);
        post("/lines/{lineId}/sections", request, CREATED.value(), SHINBUNDANG_LINE_ID);

        // when
        String message = delete("/lines/{lineId}/sections", OK.value(), Map.of("stationId", "1"), SHINBUNDANG_LINE_ID)
                .as(ExceptionResponse.class).getMessage();

        // then
        assertThat(message).isEqualTo("마지막 구간이 아닐 경우 구간을 제거할 수 없습니다.");
    }

    /**
     * WHEN 지하철 구간 제거시 구간이 한개만 있는 경우
     * Then 구간을 제거할 수 없다
     */
    @Test
    void 실패_지하철_구간_제거시_구간이_한개만_있는_경우_예외가_발생한다() {
        // when
        String message = delete("/lines/{lineId}/sections", OK.value(), Map.of("stationId", "1"), SHINBUNDANG_LINE_ID)
                .as(ExceptionResponse.class).getMessage();

        // then
        assertThat(message).isEqualTo("구간이 한개만 있을 경우 구간을 제거할 수 없습니다.");
    }

    /**
     * GIVEN 구간을 생성하고
     * WHEN 지하철 구간 제거시
     * Then 구간을 제거한다
     */
    @Test
    void 성공_지하철_구간_제거시_구간의_제거에_성공한다() {
        // given
        SectionCreateRequest request = sectionCreateRequest(SEOLLEUNG_STATION_ID, YANGJAE_STATION_ID, 13);
        post("/lines/{lineId}/sections", request, CREATED.value(), SHINBUNDANG_LINE_ID);

        // when
        delete("/lines/{lineId}/sections", NO_CONTENT.value(), Map.of("stationId", "2"), SHINBUNDANG_LINE_ID);

        // then
        LineResponse response = findLine(SHINBUNDANG_LINE_ID, OK.value()).as(LineResponse.class);
        assertThat(response.getStations()).hasSize(2)
                .extracting("id", "name")
                .containsExactly(
                        tuple(1L, "강남역"),
                        tuple(2L, "선릉역")
                );
    }

}
