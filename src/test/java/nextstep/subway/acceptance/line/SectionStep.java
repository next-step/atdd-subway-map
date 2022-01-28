package nextstep.subway.acceptance.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.RequestParamsBuilder;
import nextstep.subway.utils.RestTestUtils;

import java.net.URI;

import static io.restassured.http.Method.POST;

public class SectionStep {
    public static ExtractableResponse<Response> 구간_생성_요청(Long 노선Id, Long 상행역Id, Long 하행역Id, int 거리) {
        return RestTestUtils.요청_테스트(URI.create(String.format("/lines/%d/sections", 노선Id)), RequestParamsBuilder.builder()
                .addParam("upStationId", 상행역Id)
                .addParam("downStationId", 하행역Id)
                .addParam("distance", 1)
                .build(), POST);
    }
}
