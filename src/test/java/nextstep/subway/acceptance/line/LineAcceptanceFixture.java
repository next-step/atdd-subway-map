package nextstep.subway.acceptance.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import static nextstep.subway.acceptance.station.StationRestAssuredTemplate.지하철역_생성;

public class LineAcceptanceFixture {
    private static final ExtractableResponse<Response> 강남역 = 지하철역_생성("강남역");
    private static final ExtractableResponse<Response> 분당역 = 지하철역_생성("분당역");
    private static final ExtractableResponse<Response> 수서역 = 지하철역_생성("수서역");
    private static final ExtractableResponse<Response> 가천대역 = 지하철역_생성("가천대역");

    public static final Long 수서역_ID = Long.parseLong(수서역.jsonPath().getString("id"));
    public static final Long 가천대역_ID = Long.parseLong(가천대역.jsonPath().getString("id"));
    public static final Long 강남역_ID = Long.parseLong(강남역.jsonPath().getString("id"));
    public static final Long 분당역_ID = Long.parseLong(분당역.jsonPath().getString("id"));

}
