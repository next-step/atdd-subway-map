package subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.acceptanceSetting.StationAcceptanceTestSetting;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends StationAcceptanceTestSetting {

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void 지하철_생성_후_목록_조회시_찾을_수_있다() {

        // when+then
        stationRestAssured.save(STATION_NAME);
        stationRestAssured.save(NEW_STATION_NAME);
        stationRestAssured.save(ANOTHER_STATION_NAME);

        // then
        List<String> stationNames = stationRestAssured.findAll().jsonPath().getList(PATH_NAME, String.class);
        assertThat(stationNames).contains(STATION_NAME);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void 두_개의_지하철을_생성_후_목록_조회시_응답_받은_Response의_길이는_2이다() {

        //given
        stationRestAssured.save(STATION_NAME);
        stationRestAssured.save(NEW_STATION_NAME);

        //when
        ExtractableResponse<Response> readResponse = stationRestAssured.findAll();

        //then
        List<String> stationNames = readResponse.jsonPath().get(PATH_NAME);
        Assertions.assertThat(stationNames).containsExactly(STATION_NAME, NEW_STATION_NAME);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void 지하철을_생성하고_삭제하면_지하철_정보는_삭제_된다() {

        //given
        Long id = stationRestAssured.save(STATION_NAME);

        //when
        stationRestAssured.delete(id);

        //then
        Assertions.assertThat(stationRestAssured.findAll().jsonPath().getList(PATH_NAME)).doesNotContain(STATION_NAME);
    }
}
