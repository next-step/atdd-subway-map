package subway.section;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static common.fixture.subway.LineFixture.*;
import static common.fixture.subway.SectionFixture.구간_삭제_요청;
import static common.fixture.subway.SectionFixture.구간_생성_요청;
import static common.fixture.subway.StationFixture.*;
import static common.utils.CustomAssertions.상태코드_확인;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(scripts = {"classpath:SQLScripts/00.clear-database.sql"})
public class SectionAcceptanceTest {
    Long LINE_ID_1;
    Long STATION_ID_1;
    Long STATION_ID_2;
    Long STATION_ID_3;
    Long STATION_ID_4;

    @BeforeEach
    void setup() {
        STATION_ID_1 = 역_생성_ID_반환(역_생성_요청(GN_STATION));
        STATION_ID_2 = 역_생성_ID_반환(역_생성_요청(YS_STATION));
        STATION_ID_3 = 역_생성_ID_반환(역_생성_요청(SL_STATION));
        STATION_ID_4 = 역_생성_ID_반환(역_생성_요청(SS_STATION));

        LINE_ID_1 = 노선_생성_ID_반환(노선_생성_요청(SBD_LINE_NAME, RED_LINE_COLOR, STATION_ID_1, STATION_ID_2, DISTANCE_10));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 하행 종점역을 상행역으로 구간을 등록한다
     * Then 지하철 노선등록에 성공한다
     */
    @DisplayName("구간을 생성한다.")
    @Test
    void createSection() {
        // when
        Response response = 구간_생성_요청(LINE_ID_1, STATION_ID_2, STATION_ID_3, DISTANCE_10);

        // then
        상태코드_확인(response, HttpStatus.CREATED);

        // then
        assertThat(response.jsonPath().getLong("id")).isEqualTo(LINE_ID_1);
    }


    /**
     * Given 지하철 노선을 생성하고
     * When 등록이 된 역을 다시 등록한다
     * Then 지하철 노선등록에 실패한다
     */
    @DisplayName("등록이 된 역으로 구간을 생성하면 실패한다.")
    @Test
    void createSectionExistsStation() {
        // when
        Response response = 구간_생성_요청(LINE_ID_1, STATION_ID_1, STATION_ID_2, DISTANCE_10);

        // then
        상태코드_확인(response, HttpStatus.BAD_REQUEST);
    }


    /**
     * Given 지하철 노선을 생성하고
     * When 하행 종점역이 아닌 역으로 구간을 등록한다
     * Then 지하철 노선등록에 실패한다
     */
    @DisplayName("하행 종점역이 아닌 역으로 구간을 생성하면 실패한다.")
    @Test
    void createSectionNotEndStation() {
        // when
        Response response = 구간_생성_요청(LINE_ID_1, STATION_ID_3, STATION_ID_4, DISTANCE_10);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * Given 구간을 등록한다
     * When 마지막 구간을 삭제한다
     * Then 구간이 삭제된다.
     */
    @DisplayName("구간을 삭제한다.")
    @Test
    void deleteSection() {
        // given
        구간_생성_요청(LINE_ID_1, STATION_ID_2, STATION_ID_3, DISTANCE_10);

        // when
        Response response = 구간_삭제_요청(LINE_ID_1, STATION_ID_3);

        // then
        상태코드_확인(response, HttpStatus.NO_CONTENT);

        // then
        List<Long> stationIds = 노선_단일_조회_요청(LINE_ID_1).jsonPath().getList("stations.id", Long.class);
        assertThat(stationIds).containsAnyOf(STATION_ID_1, STATION_ID_2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 구간이 한개인 노선의 구간을 삭제한다
     * Then 구간 삭제에 실패한다.
     */
    @DisplayName("구간이 한개일 때 구간을 삭제하면 실패한다.")
    @Test
    void deleteSectionOneSection() {
        // when
        Response response = 구간_삭제_요청(LINE_ID_1, STATION_ID_2);

        // then
        상태코드_확인(response, HttpStatus.BAD_REQUEST);

        // then
        List<Long> stationIds = 노선_단일_조회_요청(LINE_ID_1).jsonPath().getList("stations.id", Long.class);
        assertThat(stationIds).containsAnyOf(STATION_ID_1, STATION_ID_2);
    }

    /**
     * Given 지하철 노선을 생성하고
     * Given 구간을 등록한다
     * When 하행 종점역이 아닌 구간을 삭제한다
     * Then 구간 삭제에 실패한다
     */
    @DisplayName("하행 종점역이 아닌 구간을 삭제하면 실패한다.")
    @Test
    void deleteSectionNotEndStation() {
        // given
        구간_생성_요청(LINE_ID_1, STATION_ID_2, STATION_ID_3, DISTANCE_10);

        // when
        Response response = 구간_삭제_요청(LINE_ID_1, STATION_ID_2);

        // then
        상태코드_확인(response, HttpStatus.BAD_REQUEST);

        // then
        List<Long> stationIds = 노선_단일_조회_요청(LINE_ID_1).jsonPath().getList("stations.id", Long.class);
        assertThat(stationIds).containsAnyOf(STATION_ID_2, STATION_ID_3);
    }
}
