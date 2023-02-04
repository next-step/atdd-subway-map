package subway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import subway.station.StationRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest extends AcceptanceTest{
    @Autowired
    private StationRepository stationRepository;

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("강남역 지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        StationRestAssuredTest.createStation("강남역");

        // then
        List<String> stationNameList = StationRestAssuredTest.getStationNameList();
        assertThat(stationNameList).containsAnyOf("강남역");
    }



    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void getStationList() {

        //Given
        StationRestAssuredTest.createStation("강남역");
        StationRestAssuredTest.createStation("역삼역");

        //Then
        List<String> stationNameList = StationRestAssuredTest.getStationNameList();

        assertThat(stationNameList).containsAnyOf("강남역", "역삼역");
        assertThat(stationNameList).hasSize(2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제합니다.")
    @Test
    public void deleteStation() {
        // Given
        Long id = StationRestAssuredTest.createStation("강남역");
        StationRestAssuredTest.createStation("역삼역");

        // When
        StationRestAssuredTest.deleteStation(id);

        // Then

        var stationNameList = StationRestAssuredTest.getStationNameList();

        assertThat(stationNameList).hasSize(1);
        assertThat(stationNameList).containsExactly("역삼역");
        assertThat(stationNameList).doesNotContain("강남역");
    }


}