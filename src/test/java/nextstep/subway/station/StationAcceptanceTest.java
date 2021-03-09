package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.CREATED;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    @Autowired
    private StationSupport stationSupport;

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        ExtractableResponse<Response> response = stationSupport.지하철역_생성_요청("강남역");

        // then
        지하철역_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        stationSupport.지하철역_등록되어_있음("강남역");

        // when
        ExtractableResponse<Response> response = stationSupport.지하철역_생성_요청("강남역");

        // then
        지하철역_생성_실패됨(response);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        // given
        Long stationId1 = stationSupport.지하철역_등록되어_있음("강남역");
        Long stationId2 = stationSupport.지하철역_등록되어_있음("역삼역");

        // when
        ExtractableResponse<Response> response = stationSupport.지하철역_조회_요청();

        // then
        지하철역_조회됨(response, stationId1, stationId2);
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        Long stationId = stationSupport.지하철역_등록되어_있음("강남역");

        // when
        ExtractableResponse<Response> response = stationSupport.지하철역_제거_요청(stationId);

        // then
        지하철역_제거됨(response);
    }


    private void 지하철역_생성됨(ExtractableResponse<Response> response) {
        응답코드_확인(response, CREATED);
        assertThat(response.header("Location")).isNotBlank();
    }

    private void 지하철역_생성_실패됨(ExtractableResponse<Response> response) {
        응답코드_확인(response, BAD_REQUEST);
    }

    private void 지하철역_조회됨(ExtractableResponse<Response> response, Long ...stationIds) {
        응답코드_확인(response, OK);
        assertThat(getResultLines(response))
                .containsAll(Arrays.asList(stationIds));
    }

    private void 지하철역_제거됨(ExtractableResponse<Response> response) {
        응답코드_확인(response, NO_CONTENT);
    }

    private void 응답코드_확인(ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    private List<Long> getResultLines(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getList(".", StationResponse.class)
                .stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
    }
}
