package subway.domain.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.common.AcceptanceTest;

import java.util.ArrayList;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.domain.station.StationApiTest.*;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        var response = 지하철역을_생성한다("강남역");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        assertThat(지하철역을_조회한다().jsonPath().getList("name").contains("강남역")).isEqualTo(true);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void printStationList(){
        //Given
        지하철역을_생성한다("강남역");
        지하철역을_생성한다("양재역");

        //When
        var response = 지하철역을_조회한다();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        //then
        assertThat(response.jsonPath().getList("name")).hasSize(2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation(){
        //given
        String deleteStationName = "강남역";
        지하철역을_생성한다(deleteStationName);

        ArrayList<Map<String, String>> list = 지하철역을_조회한다().body().as(ArrayList.class);
        Map<String, String> deleteStation = list.stream().filter(item -> item.get("name").equals(deleteStationName)).findFirst().orElse(null);

        //when
        var response = 지하철역을_삭제한다(deleteStation);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //then
        assertThat(지하철역을_조회한다().jsonPath().getList("name").contains(deleteStationName)).isEqualTo(false);
    }

}