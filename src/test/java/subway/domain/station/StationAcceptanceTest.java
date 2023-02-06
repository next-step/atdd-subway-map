package subway.domain.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.common.AcceptanceTest;
import subway.dto.StationResponse;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.common.AssertResponseTest.*;
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
        assertAll("지하철 역 생성 테스트 (독립적)",
                () -> 응답_상태코드_검증(HttpStatus.CREATED.value(), response.statusCode()),
                () -> 응답_정보_검증("강남역", response.body().as(StationResponse.class).getName()));
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

        //then
        assertAll("지하철 역 조회 테스트 (독립적)",
                () -> 응답_상태코드_검증(HttpStatus.OK.value(), response.statusCode()),
                () -> 응답_정보_갯수_검증(2, response.jsonPath().getList("name")));
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
        지하철역을_생성한다("강남역");
        StationResponse stationResponse = 지하철역을_생성한다("양재역").body().as(StationResponse.class);

        //when
        var response = 지하철역을_삭제한다(String.valueOf(stationResponse.getId()));

        //then
        assertAll("지하철 역 삭제 테스트 (독립적)",
                () -> 응답_상태코드_검증(HttpStatus.NO_CONTENT.value(), response.statusCode()),
                () -> 응답_역정보_미포함_검증(stationResponse, 지하철역을_조회한다().jsonPath().getList(".", StationResponse.class)));
    }

}