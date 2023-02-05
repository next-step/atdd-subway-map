package subway;


import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static subway.SubwayFixture.LINE_SHIN_BUN_DANG_REQUEST;
import static subway.SubwayFixture.LINE_TWO_REQUEST;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;


@DisplayName("지하철 노선 관련 기능")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@Sql("/stations.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선을 생성한다.")
    @Test
    void 지하철_노선_생성() {

        //when
        ExtractableResponse<Response> response = createSubwayLine(LINE_SHIN_BUN_DANG_REQUEST);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        //todo : 생성한 노선찾기
        assertThat(response.jsonPath().getString("name")).contains("신분당선");
    }


    private ExtractableResponse<Response> createSubwayLine(LineRequest lineRequest) {
        return given().log().all()
            .body(lineRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then()
            .log().all()
            .statusCode(HttpStatus.CREATED.value())
            .extract();
    }

    private ExtractableResponse<Response> getSubwayLines() {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract();
    }

    /**
     * 		given 2개의 지하철노선을 생성하고
     * 	  when 지하철노선 목록을 조회하면
     * 	  then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */

    @DisplayName("지하철 노선 목록을 조회")
    @Test
    void 지하철_노선_목록_조회() {
        // given
        createSubwayLine(LINE_SHIN_BUN_DANG_REQUEST);
        createSubwayLine(LINE_TWO_REQUEST);

        // when
        ExtractableResponse<Response> response = getSubwayLines();

        // then
        List<String> lineNames = response.jsonPath().getList("name", String.class);
        assertThat(lineNames).containsAnyOf(LINE_SHIN_BUN_DANG_REQUEST.getName(),
            LINE_TWO_REQUEST.getName());
    }

    private ExtractableResponse<Response> getSubwayLine(Long id) {
        return given()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .pathParam("id", id)
            .when().get("/lines/{id}")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract();
    }


    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */

    @DisplayName("지하철노선 조회")
    @Test
    void 지하철노선_조회() {
        //given
        ExtractableResponse<Response> createdResponse = createSubwayLine(
            LINE_SHIN_BUN_DANG_REQUEST);

        //when
        Long id = createdResponse.jsonPath().getLong("id");
        ExtractableResponse<Response> getResponse = getSubwayLine(id);

        //then
        String name = getResponse.jsonPath().getString("name");
        assertThat(name).isEqualTo(LINE_SHIN_BUN_DANG_REQUEST.getName());

    }

    private void updateSubwayLine(Long id) {
        RestAssured.given()
            .pathParam("id", id)
            .log().all()
            .body(LineRequest.of(SubwayFixture.LINE_NEW_SHIN_BUN_DANG_REQUEST.getId(),
                SubwayFixture.LINE_NEW_SHIN_BUN_DANG_REQUEST.getName(),
                SubwayFixture.LINE_NEW_SHIN_BUN_DANG_REQUEST.getColor()))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().put("/lines/{id}")
            .then().statusCode(HttpStatus.OK.value())
            .log().all()
            .extract();
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선 수정")
    @Test
    void 지하철노선_수정() {
        //given
        ExtractableResponse<Response> createdResponse = createSubwayLine(
            LINE_SHIN_BUN_DANG_REQUEST);
        //when
        Long id = createdResponse.jsonPath().getLong("id");
        updateSubwayLine(id);

        //then
        String lineName = getSubwayLine(id).jsonPath().getString("name");
        assertThat(lineName).isEqualTo("새로운 신분당선");
    }

    private void deleteSubwayLine(Long id) {
        given()
            .pathParam("id", id)
            .log().all()
            .when()
            .delete("/lines/{id}")
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value())
            .log().all()
            .extract();
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선 삭제")
    @Test
    void 지하철노선_삭제() {
        //given
        ExtractableResponse<Response> createdResponse = createSubwayLine(
            LINE_SHIN_BUN_DANG_REQUEST);
        //when
        Long id = createdResponse.jsonPath().getLong("id");
        deleteSubwayLine(id);
        //then
        List<String> subwayNameList = getSubwayLines().jsonPath().getList("name", String.class);
        assertThat(subwayNameList).doesNotContain(LINE_SHIN_BUN_DANG_REQUEST.getName());
    }


}