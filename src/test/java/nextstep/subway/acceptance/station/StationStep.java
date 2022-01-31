package nextstep.subway.acceptance.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.RequestParamsBuilder;
import nextstep.subway.utils.RestTestUtils;

import java.net.URI;

import static io.restassured.http.Method.POST;

public class StationStep {
    public static ExtractableResponse<Response> 지하철역_생성_요청(String 역이름) {
        return RestTestUtils.요청_테스트(URI.create("/stations"), RequestParamsBuilder.builder()
                .addParam("name", 역이름)
                .build(), POST);
    }
}
