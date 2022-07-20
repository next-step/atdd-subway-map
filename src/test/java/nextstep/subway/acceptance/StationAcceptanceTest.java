package nextstep.subway.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static nextstep.subway.acceptance.StationTestFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AbstractAcceptanceTest{
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        String 신림역 = 역_생성("강남역");


        // then
        List<String> stationNames =
                showStations()
                        .jsonPath()
                        .getList("name", String.class);

        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStation() {
        //given
        역_생성("신림역");
        역_생성("봉천역");

        //when
        List<String> stations =
                showStations()
                        .jsonPath()
                        .getList("name", String.class);

        //then
        assertThat(stations.size()).isEqualTo(2);
        assertThat(stations).containsExactly("신림역", "봉천역");


    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        //given
        String 신림역 = 역_생성("신림역");

        //when
        removeStation(신림역);

        List<String> stationName = showStations().jsonPath().getList("name", String.class);

        //then
        assertThat(stationName).doesNotContain("신림역");
    }
}