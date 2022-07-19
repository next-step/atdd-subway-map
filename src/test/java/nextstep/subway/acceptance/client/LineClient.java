package nextstep.subway.acceptance.client;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.client.dto.LineCreationRequest;
import nextstep.subway.acceptance.client.dto.LineModificationRequest;
import org.springframework.stereotype.Component;

@Component
public class LineClient {

    private static final String LINES_PATH = "/lines";
    private static final String LINE_PATH = "/lines/{id}";

    private final ApiCRUD apiCRUD;

    public LineClient(ApiCRUD apiCRUD) {
        this.apiCRUD = apiCRUD;
    }

    public ExtractableResponse<Response> createLine(LineCreationRequest lineRequest) {
        return apiCRUD.create(LINES_PATH, lineRequest);
    }

    public ExtractableResponse<Response> fetchLines() {
        return apiCRUD.read(LINES_PATH);
    }

    public ExtractableResponse<Response> fetchLine(Long lineId) {
        return apiCRUD.read(LINE_PATH, lineId);
    }

    public ExtractableResponse<Response> modifyLine(Long lineId, String name, String color) {
        LineModificationRequest lineRequest = new LineModificationRequest(name, color);
        return apiCRUD.update(LINES_PATH+ "/" + lineId, lineRequest);
    }

    public ExtractableResponse<Response> deleteLine(Long lineId) {
        return apiCRUD.delete(LINE_PATH, lineId);
    }

}
