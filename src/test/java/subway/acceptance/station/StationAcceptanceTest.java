package subway.acceptance.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import subway.acceptance.util.DatabaseCleanup;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.acceptance.common.handler.HttpStatusValidationHandler.*;
import static subway.acceptance.common.handler.RequestHandler.*;
import static subway.acceptance.station.fixture.StationFixture.*;
import static subway.acceptance.common.handler.BodyJsonPathHandler.*;

@DisplayName("지하철역 관련 기능")
@ActiveProfiles("acceptance")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @AfterEach
    void cleanUp() {
        databaseCleanup.afterPropertiesSet();
        databaseCleanup.execute();
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        var 생성_응답 = 생성_지하철역(선릉);

        // then
        HTTP_상태_CREATED_검증(생성_응답);

        // then
        var 등록된_지하철역_이름_목록 = 이름_목록_추출(조회_지하철역_목록());
        assertThat(등록된_지하철역_이름_목록).containsAnyOf(선릉.name());
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 생성 후 목록을 조회한다.")
    @Test
    void readStations() {
        // given
        생성_지하철역(연신내);
        생성_지하철역(충무로);

        // when
        var 등록된_지하철역_아이디_목록 = 아이디_목록_추출(조회_지하철역_목록());

        // then
        assertThat(등록된_지하철역_아이디_목록).hasSize(2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역 생성 후 삭제한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> response = 생성_지하철역(교대);
        Long 지하철역_아이디 = response.body().jsonPath().getLong("id");

        // when
        삭제_지하철역(지하철역_아이디);

        // then
        List<Long> 등록된_지하철역_아이디_목록 = 아이디_목록_추출(조회_지하철역_목록());
        assertThat(등록된_지하철역_아이디_목록).doesNotContain(지하철역_아이디);
    }

}