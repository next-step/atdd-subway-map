package subway.line;


import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static common.fixture.subway.LineFixture.*;
import static common.fixture.subway.StationFixture.*;
import static common.utils.CustomAssertions.상태코드_확인;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(scripts = {"classpath:SQLScripts/00.clear-database.sql"})
public class LineAcceptanceTest {
    Long STATION_ID_1;
    Long STATION_ID_2;
    Long STATION_ID_3;

    @BeforeEach
    void setup() {
        STATION_ID_1 = 역_생성_ID_반환(역_생성_요청(GN_STATION));
        STATION_ID_2 = 역_생성_ID_반환(역_생성_요청(YS_STATION));
        STATION_ID_3 = 역_생성_ID_반환(역_생성_요청(SL_STATION));
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선이 생성된다
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {
        // when
        Response response = 노선_생성_요청(SBD_LINE_NAME, RED_LINE_COLOR, STATION_ID_1, STATION_ID_2, DISTANCE_10);

        // then
        상태코드_확인(response, HttpStatus.CREATED);

        // then
        List<String> lineNames = 노선_이름_목록_반환(노선_목록_조회_요청());
        assertThat(lineNames).containsAnyOf(SBD_LINE_NAME);
    }


    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void searchLines() {
        // given
        노선_생성_요청(SBD_LINE_NAME, RED_LINE_COLOR, STATION_ID_1, STATION_ID_2, DISTANCE_10);
        노선_생성_요청(BD_LINE_NAME, GREEN_LINE_COLOR, STATION_ID_1, STATION_ID_3, DISTANCE_10);

        // when
        List<String> lineNames = 노선_이름_목록_반환(노선_목록_조회_요청());

        // then
        assertThat(lineNames).containsAnyOf(SBD_LINE_NAME, BD_LINE_NAME);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선을 조회한다.")
    @Test
    void searchLine() {
        // given
        Long createdId = 노선_생성_ID_반환(노선_생성_요청(SBD_LINE_NAME, RED_LINE_COLOR, STATION_ID_1, STATION_ID_2, DISTANCE_10));

        // when
        String lineName = 노선_단일_조회_요청(createdId).jsonPath().getString("name");

        // then
        assertThat(lineName).isEqualTo(SBD_LINE_NAME);
    }


    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        Long createdId = 노선_생성_ID_반환(노선_생성_요청(SBD_LINE_NAME, RED_LINE_COLOR, STATION_ID_1, STATION_ID_2, DISTANCE_10));

        // when
        노선_수정_요청(createdId, BD_LINE_NAME, GREEN_LINE_COLOR);

        //then
        String updatedLineName = 노선_단일_조회_요청(createdId).jsonPath().getString("name");
        assertThat(updatedLineName).isEqualTo(BD_LINE_NAME);
    }


    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 지하철 노선이 삭제된다
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        Long createdId = 노선_생성_ID_반환(노선_생성_요청(SBD_LINE_NAME, RED_LINE_COLOR, STATION_ID_1, STATION_ID_2, DISTANCE_10));

        // when
        상태코드_확인(노선_삭제_요청(createdId), HttpStatus.NO_CONTENT);

        //then
        List<String> lineNames = 노선_이름_목록_반환(노선_목록_조회_요청());
        assertThat(lineNames).isEmpty();
    }

}
