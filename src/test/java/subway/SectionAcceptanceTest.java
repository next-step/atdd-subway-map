package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import subway.controller.dto.LineCreateRequest;
import subway.controller.dto.LineResponse;
import subway.controller.dto.SectionCreateRequest;
import subway.controller.dto.StationResponse;
import subway.exception.ExceptionResponse;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;
import static subway.fixture.LineFixture.SHINBUNDANG_LINE;
import static subway.fixture.StationFixture.*;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    private Long GANGNAM_STATION_ID;
    private Long SEOLLEUNG_STATION_ID;
    private Long YANGJAE_STATION_ID;

    private Long SHINBUNDANG_LINE_ID;

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
     * GIVEN 지하철 역을 생성하고
     * GIVEN 지하철 역에 노선을 등록하고
     * WHEN 새로운 지하철 구간 등록시 상행 지하철역과 하행 지하철역을 등록하지 않으면
     * Then 새로운 구간을 등록할 수 없다
     */
    @ParameterizedTest
    @MethodSource("provideBlankSectionCreateRequest")
    void 실패_새로운_지하철_구간_등록시_필수값을_모두_입력하지_않으면_예외가_발생한다(SectionCreateRequest request) {
        ExtractableResponse<Response> response = post("/lines/{lineId}/sections", request, BAD_REQUEST.value(), SHINBUNDANG_LINE_ID);
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
    }

    private static Stream<Arguments> provideBlankSectionCreateRequest() {
        return Stream.of(
                Arguments.of(
                        SectionCreateRequest
                                .builder()
                                .upStationId("1")
                                .build()),
                Arguments.of(SectionCreateRequest
                        .builder()
                        .downStationId("2")
                        .build())
        );
    }

    /**
     * GIVEN 지하철 역을 생성하고
     * GIVEN 지하철 역에 노선을 등록하고
     * WHEN 새로운 지하철 구간의 상행역을 노선의 하행 종점역이 아닌 곳에 등록하면
     * Then 새로운 구간을 등록할 수 없다
     */
    @Test
    void 실패_새로운_구간_등록시_상행역을_노선의_하행_종점역에_등록하지_않으면_예외가_발생한다() {
        SectionCreateRequest request = sectionCreateRequest(GANGNAM_STATION_ID, YANGJAE_STATION_ID, 10);
        String message = post("/lines/{lineId}/sections", request, OK.value(), SHINBUNDANG_LINE_ID)
                .as(ExceptionResponse.class).getMessage();
        assertThat(message).isEqualTo("새로운 구간의 상행역은 노선의 하행 종점역에만 등록할 수 있습니다.");
    }

    /**
     * GIVEN 지하철 역을 생성하고
     * GIVEN 지하철 역에 노선을 등록하고
     * GIVEN 구간을 등록하고
     * WHEN 새로운 지하철 구간 등록시 노선의 총 거리가 기존의 노선 거리랑 작거나 같다면
     * Then 새로운 구간을 등록할 수 없다
     */
    @Test
    void 실패_새로운_지하철_구간_등록시_노선의_총_거리가_기존의_노선_거리랑_작거나_같다면_예외가_발생한다() {

    }

    /**
     * GIVEN 지하철 역을 생성하고
     * GIVEN 지하철 역에 노선을 등록하고
     * GIVEN 구간을 등록하고
     * WHEN 새로운 지하철 구간 등록시 노선의 하행 종점역에 등록하면
     * Then 새로운 구간이 등록된다
     */
    @Test
    void 성공_새로운_지하철_구간_등록시_노선의_하행_종점역에_등록할_수_있다() {

    }

    private SectionCreateRequest sectionCreateRequest(Long upStationId, Long downStationId, int distance) {
        return SectionCreateRequest.builder()
                .upStationId(upStationId.toString())
                .downStationId(downStationId.toString())
                .distance(distance)
                .build();
    }

}
