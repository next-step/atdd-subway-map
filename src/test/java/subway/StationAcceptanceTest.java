package subway;

import com.navercorp.fixturemonkey.ArbitraryBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.RestAssuredWrapper.*;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTestBase {
    private static final int STATION_NAME_MAX_LENGTH = 20;

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when 지하철역을 생성하면
        StationRequest stationRequest = stationRequestArbitraryBuilder().sample();
        ExtractableResponse<Response> response = post("/stations", stationRequest);

        // then 지하철역이 생성된다
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
        List<String> stationNames = get("/stations").jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf(stationRequest.getName());
    }

    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void showStations() {
        // given 2개의 지하철역을 생성하고
        List<StationRequest> stationRequests = FixtureMonkeyWrapper.giveMe(stationRequestArbitraryBuilder(), 2);
        stationRequests.forEach(stationRequest -> post("/stations", stationRequest));

        // when 지하철역 목록을 조회하면
        ExtractableResponse<Response> response = get("/stations");

        // then 2개의 지하철역을 응답 받는다
        List<StationResponse> stationResponses = getResponse(response);
        assertThat(stationResponses).hasSize(2);
    }

    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        // given 지하철역을 생성하고
        StationRequest stationRequest = stationRequestArbitraryBuilder().sample();
        ExtractableResponse<Response> postResponse = post("/stations", stationRequest);

        // when 그 지하철역을 삭제하면
        Long id = postResponse.as(StationResponse.class).getId();
        delete(String.format("/stations/%s", id));

        // then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
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
}
