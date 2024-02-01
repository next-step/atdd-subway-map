package subway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.controller.dto.LineCreateRequest;
import subway.controller.dto.StationResponse;

import static org.springframework.http.HttpStatus.CREATED;
import static subway.fixture.LineFixture.SHINBUNDANG_LINE;
import static subway.fixture.StationFixture.GANGNAM_STATION;
import static subway.fixture.StationFixture.SEOLLEUNG_STATION;



@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest  extends AcceptanceTest {

    private static Long GANGNAM_STATION_ID;
    private static Long SEOLLEUNG_STATION_ID;
    private static Long SHINBUNDANG_LINE_ID;

    @BeforeEach
    void setFixture() {
        GANGNAM_STATION_ID = createStation(GANGNAM_STATION.toCreateRequest(), CREATED.value())
                .as(StationResponse.class).getId();

        SEOLLEUNG_STATION_ID = createStation(SEOLLEUNG_STATION.toCreateRequest(), CREATED.value())
                .as(StationResponse.class).getId();

        LineCreateRequest request = SHINBUNDANG_LINE.toCreateRequest(GANGNAM_STATION_ID, SEOLLEUNG_STATION_ID);
        Long SHINBUNDANG_LINE_ID = createLine(request, CREATED.value())
                .as(StationResponse.class).getId();
    }

    /**
     * GIVEN 지하철역을 생성하고
     * GIVEN 지하철 역에 노선을 등록하고
     * WHEN 새로운 지하철 구간 등록시 상행 지하철역, 하행 지하철역, 노선의 총 거리를 모두 등록하지 않으면
     * Then 새로운 구간을 등록할 수 없다
     */
    @Test
    void 실패_새로운_지하철_구간_등록시_필수값을_모두_입력하지_않으면_예외가_발생한다() {

    }

    /**
     * GIVEN 지하철역을 생성하고
     * GIVEN 지하철 역에 노선을 등록하고
     * GIVEN 구간을 등록하고
     * WHEN 새로운 지하철 구간을 노선의 상행 종점역에 등록하면
     * Then 새로운 구간을 등록할 수 없다
     */
    @Test
    void 실패_새로운_지하철_구간_등록시_노선의_상행_종점역에_등록하면_예외가_발생한다() {

    }

    /**
     * GIVEN 지하철역을 생성하고
     * GIVEN 지하철 역에 노선을 등록하고
     * GIVEN 구간을 등록하고
     * WHEN 새로운 지하철 구간을 노선의 중간에 있는 역에 등록하면
     * Then 새로운 구간을 등록할 수 없다
     */
    @Test
    void 실패_새로운_지하철_구간_등록시_노선의_중간역에_등록하면_예외가_발생한다() {

    }

    /**
     * GIVEN 지하철역을 생성하고
     * GIVEN 지하철 역에 노선을 등록하고
     * GIVEN 구간을 등록하고
     * WHEN 새로운 지하철 구간 등록시 노선의 총 거리가 기존의 노선 거리랑 작거나 같다면
     * Then 새로운 구간을 등록할 수 없다
     */
    @Test
    void 실패_새로운_지하철_구간_등록시_노선의_총_거리가_기존의_노선_거리랑_작거나_같다면_예외가_발생한다() {

    }

    /**
     * GIVEN 지하철역을 생성하고
     * GIVEN 지하철 역에 노선을 등록하고
     * WHEN 지하철 구간 등록시 기존의 구간이 없다면
     * Then 노선의 첫 구간이 등록된다
     */
    @Test
    void 성공_새로운_지하철_구간_등록시_기존의_구간이_없다면_노선의_첫_구간이_등록된다() {

    }

    /**
     * GIVEN 지하철역을 생성하고
     * GIVEN 지하철 역에 노선을 등록하고
     * GIVEN 구간을 등록하고
     * WHEN 새로운 지하철 구간 등록시 노선의 하행 종점역에 등록하면
     * Then 새로운 구간이 등록된다
     */
    @Test
    void 성공_새로운_지하철_구간_등록시_노선의_하행_종점역에_등록할_수_있다() {

    }

}
