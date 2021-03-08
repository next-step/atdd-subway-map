package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LineSectionVerificationSteps {

    public static void 지하철_노선에_지하철_역_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선에_지하철_역_포함_됨(ExtractableResponse<Response> response, List<StationResponse> stationResponses) {
        LineResponse lineResponse = response.as(LineResponse.class);
        assertThat(lineResponse.getStations()).containsExactlyElementsOf(stationResponses);
    }

}
