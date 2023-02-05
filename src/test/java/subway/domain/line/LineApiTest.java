package subway.domain.line;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.domain.Section;
import subway.domain.Sections;
import subway.dto.LineResponse;
import java.util.Map;

public class LineApiTest {
    public static ExtractableResponse<Response> 지하철노선을_생성한다(Map<String, Object> param) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(param)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철노선에_구간을_추가한다(int id, Map<String, Object> param) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(param)
                .when().post("/lines/{id}/sections", id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철노선_목록을_조회한다() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철노선을_조회한다(int id) {
        return RestAssured.given().log().all()
                .when().get("/lines/{id}",id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철노선을_수정한다(int id, Map<String, String> param1) {
        return RestAssured.given().log().all()
                .body(param1)
                .contentType(ContentType.JSON)
                .when().put("/lines/{id}", id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철노선을_삭제한다(int id) {
        return RestAssured.given().log().all()
                .when().delete("/lines/{id}",id)
                .then().log().all()
                .extract();
    }

    public static Section 지하철노선의_마지막_구간을_조회한다(int lineId) {
        var tempResponse = 지하철노선을_조회한다(lineId);

        Sections sections = tempResponse.body().as(LineResponse.class).getSections();
        Section section = sections.getSections().get(sections.getSections().size()-1);

        return section;
    }
}
