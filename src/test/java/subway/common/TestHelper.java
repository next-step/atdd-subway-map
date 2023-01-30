package subway.common;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import subway.line.LineResponse;
import subway.station.StationResponse;
import subway.utils.RestAssuredClient;

import static org.assertj.core.api.Assertions.assertThat;

public class TestHelper {
    public static void 응답_코드가_일치한다(int actualStatusCode, HttpStatus expectedStatusCode) {
        assertThat(actualStatusCode).isEqualTo(expectedStatusCode.value());
    }

    public static String 생성_헤더(ExtractableResponse<Response> response) {
        return response.header("Location");
    }

    public static void 노선의_정보가_일치한다(
            LineResponse response,
            String expectedLineName,
            String expectedColor,
            long expectedUpStationId,
            long expectedDownStationId
    ) {
        assertThat(response.getName()).isEqualTo(expectedLineName);
        assertThat(response.getColor()).isEqualTo(expectedColor);
        assertThat(response.getStations().stream().map(StationResponse::getId)).containsExactly(
                expectedUpStationId, expectedDownStationId);
    }

    public static ExtractableResponse<Response> 노선을_생성한다(Request request) {
        var response = RestAssuredClient.post(
                Endpoints.LINES,
                request
        );
        응답_코드가_일치한다(response.statusCode(), HttpStatus.CREATED);
        return response;
    }
}
