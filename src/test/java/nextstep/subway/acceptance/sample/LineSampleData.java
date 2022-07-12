package nextstep.subway.acceptance.sample;

import static nextstep.subway.acceptance.template.LineRequestTemplate.지하철노선을_생성을_요청한다;
import static nextstep.subway.acceptance.template.StationRequestTemplate.지하철역_생성을_요청한다;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class LineSampleData {
    public static ExtractableResponse<Response> 신분당선_노선을_생성한다() {
        long downStationId = 지하철역_생성을_요청한다("광교역").jsonPath().getLong("id");
        long upStationId = 지하철역_생성을_요청한다("신사역").jsonPath().getLong("id");

        return 지하철노선을_생성을_요청한다("신분당선", "bg-red-600", downStationId, upStationId, (long) 10);
    }

    public static ExtractableResponse<Response> 일호선_노선을_생성한다() {
        long downStationId = 지하철역_생성을_요청한다("인천역").jsonPath().getLong("id");
        long upStationId = 지하철역_생성을_요청한다("소요산역").jsonPath().getLong("id");

        return 지하철노선을_생성을_요청한다("1호선", "bg-blue-600", downStationId, upStationId, (long) 15);
    }
}
