package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.MediaType;

public class LineSectionRequestSteps {

    public static ExtractableResponse<Response> 지하철_노선에_지하철_역_등록_요청(long lineId, StationResponse upStation, StationResponse downStation, int distance) {
        SectionRequest sectionRequest = new SectionRequest(upStation.getId(), downStation.getId(), distance);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest)
                .when()
                .post("/lines/{id}/sections", lineId) // TODO : URI 외부에서 주입
                .then().log().all()
                .extract();
    }
}
