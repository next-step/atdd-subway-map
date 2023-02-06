package subway.acceptance.section;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import subway.acceptance.common.handler.RequestHandler;
import subway.acceptance.station.fixture.StationFixture;
import subway.acceptance.util.DatabaseCleanup;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.acceptance.common.handler.BodyJsonPathHandler.아이디_추출;
import static subway.acceptance.common.handler.BodyJsonPathHandler.지하철역_이름_목록_추출;
import static subway.acceptance.common.handler.HttpStatusValidationHandler.*;
import static subway.acceptance.common.handler.RequestHandler.*;
import static subway.acceptance.line.fixture.LineFixture.지하철_3_호선;
import static subway.acceptance.station.fixture.StationFixture.*;
import static subway.acceptance.station.fixture.StationFixture.교대;


@DisplayName("지하철 노선의 구간 삭제 관련 기능")
@ActiveProfiles("acceptance")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionDeleteAcceptanceTest {

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
     * When 지하철 노선에서 구간을 삭제하면
     * Then 구간이 삭제된다.
     * Then 지하철 노선 조회 시 삭제한 구간이 없다.
     */
    @DisplayName("지하철 노선에 구간을 제거한다.")
    @Test
    void normal() {
        var 교대역_아이디 = 지하철역_아이디.get(교대);
        var 수서역_아이디 = 지하철역_아이디.get(수서);

        생성_지하철_노선에_구간(연신내역_교대역_노선_아이디, 교대역_아이디, 수서역_아이디, 9);

        // when
        var 삭제_지하철_노선의_구간_응답 = RequestHandler.삭제_지하철_노선의_구간(연신내역_교대역_노선_아이디, 수서역_아이디);

        // then
        HTTP_상태_NO_CONTENT_검증(삭제_지하철_노선의_구간_응답);

        // then
        var 연신내역_교대역_노선_응답 = 조회_지하철_노선(연신내역_교대역_노선_아이디);
        HTTP_상태_OK_검증(연신내역_교대역_노선_응답);

        var 지하철_3_호선_역_이름_목록 = 지하철역_이름_목록_추출(연신내역_교대역_노선_응답);

        assertThat(지하철_3_호선_역_이름_목록).hasSize(2);
        assertThat(지하철_3_호선_역_이름_목록).contains(연신내.name(), 교대.name());
    }

    /**
     * When 지하철 노선에 [하나의 구간만 존재] 조건으로 구간 삭제하면
     * Then 구간이 삭제 실패다.
     * Then 지하철 노선 조회 시 삭제가 반영되어 있지 않다.
     */
    @DisplayName("하나의 구간만 존재할 때, 구간 삭제를 시도하고 실패한다.")
    @Test
    void when_onlyOneSectionException() {
        var 교대역_아이디 = 지하철역_아이디.get(교대);

        // when
        var 삭제_지하철_노선의_구간_응답 = RequestHandler.삭제_지하철_노선의_구간(연신내역_교대역_노선_아이디, 교대역_아이디);

        // then
        HTTP_상태_BAD_REQUEST_검증(삭제_지하철_노선의_구간_응답);

        // then
        var 연신내역_교대역_노선_응답 = 조회_지하철_노선(연신내역_교대역_노선_아이디);
        HTTP_상태_OK_검증(연신내역_교대역_노선_응답);

        var 지하철_3_호선_역_이름_목록 = 지하철역_이름_목록_추출(연신내역_교대역_노선_응답);

        assertThat(지하철_3_호선_역_이름_목록).hasSize(2);
        assertThat(지하철_3_호선_역_이름_목록).contains(연신내.name(), 교대.name());
    }

    /**
     * When 지하철 노선에 [구간의 하행 종점역이 아닌 역에 대해 삭제] 조건으로 구간 삭제하면
     * Then 구간이 삭제 실패다.
     * Then 지하철 노선 조회 시 삭제가 반영되어 있지 않다.
     */
    @DisplayName("구간의 하행 종점역이 아닌 역에 대해 삭제를 시도하고 실패한다.")
    @Test
    void when_notDownEndStationRegisteredOnLine() {
        var 교대역_아이디 = 지하철역_아이디.get(교대);
        var 수서역_아이디 = 지하철역_아이디.get(수서);
        var 충무로역_아이디 = 지하철역_아이디.get(충무로);

        생성_지하철_노선에_구간(연신내역_교대역_노선_아이디, 교대역_아이디, 수서역_아이디, 9);

        // when
        var 삭제_지하철_노선의_구간_응답 = RequestHandler.삭제_지하철_노선의_구간(연신내역_교대역_노선_아이디, 충무로역_아이디);

        // then
        HTTP_상태_BAD_REQUEST_검증(삭제_지하철_노선의_구간_응답);

        // then
        var 연신내역_교대역_노선_응답 = 조회_지하철_노선(연신내역_교대역_노선_아이디);
        HTTP_상태_OK_검증(연신내역_교대역_노선_응답);

        var 지하철_3_호선_역_이름_목록 = 지하철역_이름_목록_추출(연신내역_교대역_노선_응답);

        assertThat(지하철_3_호선_역_이름_목록).hasSize(3);
        assertThat(지하철_3_호선_역_이름_목록).contains(연신내.name(), 교대.name(), 수서.name());
    }

}