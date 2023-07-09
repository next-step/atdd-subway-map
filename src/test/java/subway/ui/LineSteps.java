package subway.ui;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;
import java.util.stream.Collectors;

import static subway.ui.StationSteps.지하철역_생성_요청_Response_반환;
import static subway.util.AcceptanceTestUtil.*;

public class LineSteps {

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(final String name) {
        LineCreateRequest request = createLineCreateRequest(name);
        return post("/lines", request);
    }

    public static LineResponse 지하철_노선_생성_요청_Response_반환(String name, Long upStationId, Long downStationId) {
        LineCreateRequest request = new LineCreateRequest(name, "bg-red-600", upStationId, downStationId, 10L);
        return post("/lines", request).as(LineResponse.class);
    }

    public static LineResponse 지하철_노선_생성_요청_Response_반환(final String name) {
        LineCreateRequest request = createLineCreateRequest(name);
        return post("/lines", request).as(LineResponse.class);
    }

    public static List<String> 지하철_노선_목록_조회_요청() {
        return get("/lines", "name", String.class);
    }

    public static List<ExtractableResponse<Response>> 지하철_노선_여러개_생성_요청(List<String> names) {
        return names.stream().map(LineSteps::지하철_노선_생성_요청).collect(Collectors.toList());
    }

    public static LineResponse 지하철_노선_조회_요청(Long lineId) {
        return get("/lines/{id}", lineId).as(LineResponse.class);
    }

    public static List<String> 지하철_노선_조회_요청_노선에_속한_역_반환(Long lineId) {
        return get("/lines/{id}", lineId).as(LineResponse.class)
                .getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
    }

    public static LineResponse 지하철_노선_수정_요청(Long lineId, LineUpdateRequest request) {
        return put("/lines/{id}", lineId, request).as(LineResponse.class);
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(Long lineId) {
        return delete("/lines/{id}", lineId);
    }

    public static ExtractableResponse<Response> 지하철_노선_구간_등록_요청(Long lineId, SectionRequest request) {
        return post("/lines/{id}/sections", lineId, request);
    }

    private static LineCreateRequest createLineCreateRequest(String name) {
        return new LineCreateRequest(
                name,
                "bg-red-600",
                지하철역_생성_요청_Response_반환("상행종점역").getId(),
                지하철역_생성_요청_Response_반환("상행종점역").getId(),
                10L
        );
    }
}
