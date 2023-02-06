package subway.acceptance.section;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import subway.acceptance.station.fixture.StationFixture;
import subway.acceptance.util.DatabaseCleanup;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.acceptance.common.handler.HttpStatusValidationHandler.*;
import static subway.acceptance.common.handler.RequestHandler.*;
import static subway.acceptance.line.fixture.LineFixture.*;
import static subway.acceptance.station.fixture.StationFixture.*;
import static subway.acceptance.common.handler.BodyJsonPathHandler.*;


@DisplayName("지하철 노선의 구간 등록 관련 기능")
@ActiveProfiles("acceptance")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionCreateAcceptanceTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    Map<StationFixture, Long> 지하철역_아이디 = new HashMap<>();
    Long 연신내역_교대역_노선_아이디 = null;

    @BeforeEach
    void setUp() {
        지하철역_아이디.clear();

        지하철역_아이디.put(연신내, 아이디_추출(생성_지하철역(연신내)));
        지하철역_아이디.put(충무로, 아이디_추출(생성_지하철역(충무로)));
        지하철역_아이디.put(교대, 아이디_추출(생성_지하철역(교대)));
        지하철역_아이디.put(선릉, 아이디_추출(생성_지하철역(선릉)));
        지하철역_아이디.put(수서, 아이디_추출(생성_지하철역(수서)));

        var 연신내역_아이디 = 지하철역_아이디.get(연신내);
        var 교대역_아이디 = 지하철역_아이디.get(교대);

        연신내역_교대역_노선_아이디 = 아이디_추출(생성_지하철_노선(지하철_3_호선, 연신내역_아이디, 교대역_아이디, 19));
    }

    @AfterEach
    void cleanUp() {
        databaseCleanup.afterPropertiesSet();
        databaseCleanup.execute();
    }

    /**
     * When 지하철 노선에 구간 등록하면
     * Then 구간이 등록된다.
     * Then 지하철 노선 조회 시 등록한 구간이 포함되어 있다.
     */
    @DisplayName("지하철 노선에 구간 등록한다.")
    @Test
    void normal() {
        // when
        var 교대역_아이디 = 지하철역_아이디.get(교대);
        var 수서역_아이디 = 지하철역_아이디.get(수서);
        var 생성_지하철_노선에_구간_응답 = 생성_지하철_노선에_구간(
                연신내역_교대역_노선_아이디, 교대역_아이디, 수서역_아이디, 9
        );

        // then
        HTTP_상태_CREATED_검증(생성_지하철_노선에_구간_응답);

        // then
        var 연신내역_교대역_노선_응답 = 조회_지하철_노선(연신내역_교대역_노선_아이디);
        HTTP_상태_OK_검증(연신내역_교대역_노선_응답);

        var 지하철_3_호선_역_이름_목록 = 지하철역_이름_목록_추출(연신내역_교대역_노선_응답);

        assertThat(지하철_3_호선_역_이름_목록).hasSize(3);
        assertThat(지하철_3_호선_역_이름_목록).contains(연신내.name(), 교대.name(), 수서.name());
    }

    /**
     * When 지하철 노선에 [상행역으로 노선의 하행 종점역이 아닌 역] 조건으로 구간 등록하면
     * Then 구간이 등록 실패다.
     * Then 지하철 노선 조회 시 등록한 구간이 포함되어 있지 않다.
     */
    @DisplayName("상행역으로 노선의 하행 종점역이 아닌 역으로 등록을 시도하고 실패한다.")
    @Test
    void when_upStationIsNotDownStationOfLine() {
        // when
        var 충무로_아이디 = 지하철역_아이디.get(충무로);
        var 수서역_아이디 = 지하철역_아이디.get(수서);
        var 생성_지하철_노선에_구간_응답 = 생성_지하철_노선에_구간(
                연신내역_교대역_노선_아이디, 충무로_아이디, 수서역_아이디, 18
        );

        // then
        HTTP_상태_BAD_REQUEST_검증(생성_지하철_노선에_구간_응답);

        // then
        var 연신내역_교대역_노선_응답 = 조회_지하철_노선(연신내역_교대역_노선_아이디);
        HTTP_상태_OK_검증(연신내역_교대역_노선_응답);

        var 지하철_3_호선_역_이름_목록 = 지하철역_이름_목록_추출(연신내역_교대역_노선_응답);

        assertThat(지하철_3_호선_역_이름_목록).hasSize(2);
        assertThat(지하철_3_호선_역_이름_목록).contains(연신내.name(), 교대.name());
    }

    /**
     * When 지하철 노선에 [구간의 하행역을 해당 노선에 등록되어 있는 역] 조건으로 구간 등록하면
     * Then 구간이 등록 실패다.
     * Then 지하철 노선 조회 시 등록한 구간이 포함되어 있지 않다.
     */
    @DisplayName("구간의 하행역을 해당 노선에 등록되어 있는 역으로 등록을 시도하고 실패한다.")
    @Test
    void when_downStationIsStationOfLine() {
        // when
        var 교대_아이디 = 지하철역_아이디.get(교대);
        var 연신내_아이디 = 지하철역_아이디.get(연신내);
        var 생성_지하철_노선에_구간_응답 = 생성_지하철_노선에_구간(
                연신내역_교대역_노선_아이디, 교대_아이디, 연신내_아이디, 19
        );

        // then
        HTTP_상태_BAD_REQUEST_검증(생성_지하철_노선에_구간_응답);

        // then
        var 연신내역_교대역_노선_응답 = 조회_지하철_노선(연신내역_교대역_노선_아이디);
        HTTP_상태_OK_검증(연신내역_교대역_노선_응답);

        var 지하철_3_호선_역_이름_목록 = 지하철역_이름_목록_추출(연신내역_교대역_노선_응답);

        assertThat(지하철_3_호선_역_이름_목록).hasSize(2);
        assertThat(지하철_3_호선_역_이름_목록).contains(연신내.name(), 교대.name());
    }

}