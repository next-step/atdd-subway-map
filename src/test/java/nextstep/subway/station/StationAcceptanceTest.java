package nextstep.subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.support.StationSteps;
import nextstep.subway.station.support.StationVerifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        //Given
        StationRequest stationRequest = new StationRequest("강남역");

        //When
        ExtractableResponse<Response> response = StationSteps.지하철역_생성_요청(stationRequest);

        //Then
        StationVerifier.지하철역_등록_검증(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @ParameterizedTest
    @CsvSource(value = {"강남역", "역삼역", "방배역"})
    void createStationWithDuplicateName(String stationName) {
        //Given
        StationSteps.지하철역_등록됨(new StationRequest(stationName));

        //When
        ExtractableResponse<Response> response = StationSteps.지하철역_생성_요청(new StationRequest(stationName));

        //Then
        StationVerifier.지하철역_생성_실패됨(response);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        ///Given
        Long createdId1 = StationSteps.지하철역_등록됨(new StationRequest("강남역")).getId();
        Long createdId2 = StationSteps.지하철역_등록됨(new StationRequest("역삼역")).getId();

        //When
        ExtractableResponse<Response> response = StationSteps.지하철역_목록_조회요청();

        //Then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> StationVerifier.지하철역_목록_조회검증(createdId1, createdId2, response)
        );
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        //Given
        StationResponse createdStation = StationSteps.지하철역_등록됨(new StationRequest("강남역"));

        //When
        ExtractableResponse<Response> response = StationSteps.지하철역_삭제_요청(createdStation.getId());

        //Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
