package subway.station;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static common.fixture.subway.StationFixture.*;
import static common.utils.CustomAssertions.상태코드_확인;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 인수테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(scripts = {"classpath:SQLScripts/00.clear-database.sql"})
public class StationAcceptanceTest {

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        Response response = 역_생성_요청(GN_STATION);

        // then
        상태코드_확인(response, HttpStatus.CREATED);

        // then
        List<String> stationNames = 역_이름_목록_반환(역_목록_조회_요청());
        assertThat(stationNames).containsAnyOf(GN_STATION);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void searchStation() {
        // given
        역_생성_요청(GN_STATION);
        역_생성_요청(YS_STATION);

        // when
        List<String> stationNames = 역_이름_목록_반환(역_목록_조회_요청());

        // then
        assertThat(stationNames).containsAnyOf(GN_STATION, YS_STATION);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 지하철역이 삭제된다
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        // given
        Long createdId = 역_생성_ID_반환(역_생성_요청(GN_STATION));

        // when
        Response response = 역_삭제_요청(createdId);

        // then
        상태코드_확인(response, HttpStatus.NO_CONTENT);

        // then
        List<String> stationNames = 역_이름_목록_반환(역_목록_조회_요청());
        assertThat(stationNames).isEmpty();
    }

}