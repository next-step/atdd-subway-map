package nextstep.subway.acceptance.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.RequestParamsBuilder;
import nextstep.subway.utils.RestTestUtils;

import java.net.URI;
import java.util.Collections;

import static io.restassured.http.Method.DELETE;
import static io.restassured.http.Method.POST;

public class SectionStep {
    public static ExtractableResponse<Response> 구간_생성_요청(Long 노선Id, Long 상행역Id, Long 하행역Id, int 거리) {
        return RestTestUtils.요청_테스트(URI.create(String.format("/lines/%d/sections", 노선Id)), RequestParamsBuilder.builder()
                .addParam("upStationId", 상행역Id)
                .addParam("downStationId", 하행역Id)
                .addParam("distance", 1)
                .build(), POST);
    }

    public static ExtractableResponse<Response> 구간_삭제_요청(Long 노선Id, Long 하행종점역Id) {
        return RestTestUtils.요청_테스트_WithQueryParams(URI.create(String.format("/lines/%d/sections", 노선Id)),
                Collections.emptyMap(),
                RequestParamsBuilder.builder()
                        .addParam("stationId", 하행종점역Id)
                        .build(),
                DELETE);
    }
}
