package subway.acceptance.line;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import subway.acceptance.util.DatabaseCleanup;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.acceptance.common.handler.HttpStatusValidationHandler.*;
import static subway.acceptance.common.handler.RequestHandler.*;
import static subway.acceptance.line.fixture.LineFixture.*;
import static subway.acceptance.station.fixture.StationFixture.*;
import static subway.acceptance.common.handler.BodyJsonPathHandler.*;


@DisplayName("지하철 노선 관련 기능")
@ActiveProfiles("acceptance")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @AfterEach
    void cleanUp() {
        databaseCleanup.afterPropertiesSet();
        databaseCleanup.execute();
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        var 생성_지하철_노선_응답 = 생성_지하철_노선(지하철_3_호선, 연신내, 교대, 19);

        // then
        HTTP_상태_CREATED_검증(생성_지하철_노선_응답);

        var 지하철_노선_이름 = 이름_추출(생성_지하철_노선_응답);
        assertThat(지하철_노선_이름.equals(지하철_3_호선.이름()));
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void readLines() {
        // given
        생성_지하철_노선(지하철_2_호선, 교대, 선릉, 3);
        생성_지하철_노선(지하철_3_호선, 연신내, 교대, 19);

        // when
        var 조회_지하철_노선_목록_응답 = 조회_지하철_노선_목록();

        // then
        HTTP_상태_OK_검증(조회_지하철_노선_목록_응답);

        var 지하철_노선_이름_목록 = 이름_목록_추출(조회_지하철_노선_목록_응답);
        assertThat(지하철_노선_이름_목록).hasSize(2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void readLine() {
        // given
        var 지하철_노선_생성_응답 = 생성_지하철_노선(지하철_2_호선, 연신내, 교대, 19);
        var 생성된_지하철_노선_아이디 = 아이디_추출(지하철_노선_생성_응답);

        // when
        var 조회한_지하철_노선_응답 = 조회_지하철_노선(생성된_지하철_노선_아이디);

        // then
        HTTP_상태_OK_검증(조회한_지하철_노선_응답);

        var 지하철_노선_이름 = 이름_추출(조회한_지하철_노선_응답);
        assertThat(지하철_노선_이름).isEqualTo(지하철_2_호선.이름());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        //given
        var 지하철_3_호선_생성_응답 = 생성_지하철_노선(지하철_3_호선, 연신내, 교대, 19);
        var 지하철_3_호선_아이디 = 지하철_3_호선_생성_응답.jsonPath().getLong("id");

        // when
        var 수정_지하철_노선_응답 = 수정_지하철_노선(지하철_3_호선_아이디, 지하철_2_호선);

        // then
        HTTP_상태_OK_검증(수정_지하철_노선_응답);

        var 조회_지하철_노선_응답 = 조회_지하철_노선(지하철_3_호선_아이디);
        var 지하철_노선_이름 = 이름_추출(조회_지하철_노선_응답);
        var 지하철_노선_색상 = 색상_추출(조회_지하철_노선_응답);

        assertThat(지하철_노선_이름).isEqualTo(지하철_2_호선.이름());
        assertThat(지하철_노선_색상).isEqualTo(지하철_2_호선.색상());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        var 지하철_노선_생성_응답 = 생성_지하철_노선(지하철_3_호선, 연신내, 교대, 19);
        var 생성된_지하철_노선_아이디 = 아이디_추출(지하철_노선_생성_응답);

        // when
        var 삭제_지하철_노선_응답 = 삭제_지하철_노선(생성된_지하철_노선_아이디);

        // then
        HTTP_상태_NO_CONTENT_검증(삭제_지하철_노선_응답);
    }

}