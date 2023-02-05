package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import subway.common.Endpoints;
import subway.line.presentation.LineResponse;
import subway.station.presentation.StationResponse;
import subway.utils.RestAssuredClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.common.TestHelper.응답_코드가_일치한다;

public class LineFixtures {
    public static final String RED = "bg-red-600";
    public static final String BLUE = "bg-blue-600";
    public static final String GREEN = "bg-green-600";

    public static void 노선의_정보가_일치한다(
            LineResponse response,
            String expectedLineName,
            String expectedColor
    ) {
        assertThat(response.getName()).isEqualTo(expectedLineName);
        assertThat(response.getColor()).isEqualTo(expectedColor);
    }

    public static void 노선의_정보가_일치한다(
            LineResponse response,
            String expectedLineName,
            String expectedColor,
            List<Long> stationIds
    ) {
        assertThat(response.getName()).isEqualTo(expectedLineName);
        assertThat(response.getColor()).isEqualTo(expectedColor);
        assertThat(response.getStations().stream().map(StationResponse::getId)).containsExactly(
                stationIds.toArray(new Long[stationIds.size()]));
    }

    public static void 노선이_해당_역을_정확히_포함한다(
            LineResponse response,
            List<String> stationNames
    ) {
        assertThat(response.getStations().stream().map(StationResponse::getName))
                .containsExactly(stationNames.toArray(new String[stationNames.size()]));
    }

    public static ExtractableResponse<Response> 노선을_생성한다(Object request) {
        var response = RestAssuredClient.post(
                Endpoints.LINES,
                request
        );
        응답_코드가_일치한다(response.statusCode(), HttpStatus.CREATED);
        return response;
    }

    public static long 노선을_생성하고_노선_아이디를_반환한다(Object request) {
        var response = RestAssuredClient.post(
                Endpoints.LINES,
                request
        );
        응답_코드가_일치한다(response.statusCode(), HttpStatus.CREATED);
        return response.jsonPath().getLong("id");
    }
}
