package subway;

import com.navercorp.fixturemonkey.ArbitraryBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.station.dto.StationRequest;
import subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.RestAssuredWrapper.*;

@DisplayName("지하철역 관련 기능")
class StationAcceptanceTest extends AcceptanceTestBase {
    private static final int STATION_NAME_MAX_LENGTH = 20;

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // When: 지하철역을 생성하면
        StationRequest stationRequest = stationRequestArbitraryBuilder().sample();
        ExtractableResponse<Response> postResponse = post("/stations", stationRequest);

        // Then: 지하철역이 생성된다
        assertThat(postResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // Then: 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
        ExtractableResponse<Response> getResponse = get("/stations");
        List<String> stationNames = getStationNames(getResponse);
        assertThat(stationNames).containsAnyOf(stationRequest.getName());
    }

    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void findStations() {
        // Given: 2개의 지하철역을 생성하고
        List<StationRequest> stationRequests = FixtureMonkeyWrapper.giveMe(stationRequestArbitraryBuilder(), 2);
        stationRequests.forEach(stationRequest -> post("/stations", stationRequest));

        // When: 지하철역 목록을 조회하면
        ExtractableResponse<Response> getResponse = get("/stations");

        // Then: 2개의 지하철역을 응답 받는다
        List<String> stationNames = getStationNames(getResponse);
        assertThat(stationNames).containsExactlyElementsOf(stationRequests.stream()
                .map(StationRequest::getName).collect(Collectors.toList()));
    }

    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        // Given: 지하철역을 생성하고
        StationRequest stationRequest = stationRequestArbitraryBuilder().sample();
        ExtractableResponse<Response> postResponse = post("/stations", stationRequest);

        // When: 그 지하철역을 삭제하면
        Long id = postResponse.as(StationResponse.class).id();
        delete(String.format("/stations/%s", id));

        // Then: 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
        ExtractableResponse<Response> deleteResponse = get("/stations");
        List<StationResponse> stationResponses = getResponse(deleteResponse);

        assertThat(stationResponses).isEmpty();
    }

    private static ArbitraryBuilder<StationRequest> stationRequestArbitraryBuilder() {
        return FixtureMonkeyWrapper.create().giveMeBuilder(StationRequest.class)
                .set("name", Arbitraries.strings().ofMaxLength(STATION_NAME_MAX_LENGTH));
    }

    private static List<StationResponse> getResponse(ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", StationResponse.class);
    }

    private static List<String> getStationNames(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("name", String.class);
    }
}
