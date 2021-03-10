package nextstep.subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.station.StationSteps.*;
import static nextstep.subway.utils.HttpAssertions.*;
import static nextstep.subway.utils.HttpTestUtils.리소스_ID;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given & when
        ExtractableResponse<Response> response = 지하철_역_생성("강남역");

        // then
        응답_HTTP_CREATED(response);
        헤더_LOCATION_존재(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        지하철_역_생성("강남역");

        // when
        ExtractableResponse<Response> response = 지하철_역_생성("강남역");

        // then
        응답_HTTP_BAD_REQUEST(response);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        /// given
        ExtractableResponse<Response> createResponse1 = 지하철_역_생성("강남역");
        ExtractableResponse<Response> createResponse2 = 지하철_역_생성("역삼역");

        // when
        ExtractableResponse<Response> response = 지하철_역_목록_조회();

        // then
        응답_HTTP_OK(response);
        List<Long> expectedLineIds = Arrays.asList(리소스_ID(createResponse1), 리소스_ID(createResponse2));
        List<Long> resultLineIds = response.jsonPath().getList(".", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_역_생성("강남역");

        // when
        ExtractableResponse<Response> response = 지하철_역_삭제(리소스_ID(createResponse));

        // then
        응답_HTTP_NO_CONTENT(response);
    }



}
