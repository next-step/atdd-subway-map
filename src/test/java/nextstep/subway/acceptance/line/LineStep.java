package nextstep.subway.acceptance.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.RequestParamsBuilder;
import nextstep.subway.utils.RestTestUtils;

import java.net.URI;

import static io.restassured.http.Method.GET;
import static io.restassured.http.Method.POST;

public class LineStep {
    public static ExtractableResponse<Response> 노선_생성_요청(String 노선이름, String 노선색, Long 상행종점역, Long 하행종점역) {
        return RestTestUtils.요청_테스트(URI.create("/lines"), RequestParamsBuilder.builder()
                .addParam("name", 노선이름)
                .addParam("color", 노선색)
                .addParam("upStationId", 상행종점역)
                .addParam("downStationId", 하행종점역)
                .addParam("distance", 1)
                .build(), POST);
    }

    public static ExtractableResponse<Response> 노선_전체_조회_요청() {
        return RestTestUtils.요청_테스트(URI.create("/lines"), GET);
    }
}
