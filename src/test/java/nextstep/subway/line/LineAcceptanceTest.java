package nextstep.subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    private long requestCreateLine(String name, String color) {
        Map<String,String> params = new HashMap<>();
        params.put("name",name);
        params.put("color",color);

        ExtractableResponse<Response> response =  RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then()
            .log().all()
            .extract();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return response.body()
            .jsonPath().getLong("id");

    }

    private ExtractableResponse<Response> requestFindLines(){
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines")
            .then().log().all().extract();
    }

    private ExtractableResponse<Response> requestFindLine(long id){
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines/{id}",id)
            .then().log().all().extract();
    }

    private ExtractableResponse<Response> requestUpdateLine(Map params,long id){
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE).body(params)
            .when().put("/lines/{id}",id)
            .then().log().all().extract();
    }

    private ExtractableResponse<Response> requestDeleteLine(long id) {
        return RestAssured
            .given().log().all()
            .when().delete("/lines/{id}",id)
            .then().log().all().extract();
    }


    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        // 지하철_노선_생성_요청
        long id = requestCreateLine("신분당선","RED");
        // then
        // 지하철_노선_생성됨
        assertThat(id).isNotNull();
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        // 지하철_노선_등록되어_있음
        // 지하철_노선_등록되어_있음
        requestCreateLine("신분당선","RED");
        requestCreateLine("2호선","GREEN");

        // when
        // 지하철_노선_목록_조회_요청
        ExtractableResponse<Response> response = requestFindLines();

        // then
        // 지하철_노선_목록_응답됨
        // 지하철_노선_목록_포함됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().asString()).contains("신분당선").contains("2호선");
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        // 지하철_노선_등록되어_있음
        long id =  requestCreateLine("신분당선","RED");
        // when
        // 지하철_노선_조회_요청
        ExtractableResponse<Response> response = requestFindLine(id);

        // then
        // 지하철_노선_응답됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        // 지하철_노선_등록되어_있음
        long id =  requestCreateLine("신분당선","RED");
        Map<String,String> params = new HashMap<>();
        params.put("name","신분당선");
        params.put("color","YELLOW");
        // when
        // 지하철_노선_수정_요청
        ExtractableResponse<Response> response = requestUpdateLine(params,id);

        // then
        // 지하철_노선_수정됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.ACCEPTED.value());
        assertThat(response.as(LineResponse.class).getColor()).isEqualTo("YELLOW");
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철_노선_등록되어_있음
        long id =  requestCreateLine("신분당선","RED");
        // when
        // 지하철_노선_제거_요청
        ExtractableResponse<Response> response = requestDeleteLine(id);

        // then
        // 지하철_노선_삭제됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

    }
}
