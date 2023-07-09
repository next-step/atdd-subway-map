package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.RestAssuredTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static subway.utils.StationTestUtils.주어진_이름으로_지하철역을_생성한다;


@DisplayName("지하철노선 관련 기능")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest extends RestAssuredTest {


    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Test
    void 지하철_노선을_생성한다() {
        //given
        Long gangnamStationId = 주어진_이름으로_지하철역을_생성한다("강남역");
        Long pangyoStationId = 주어진_이름으로_지하철역을_생성한다("판교역");

        LineCreateRequest lineCreateRequest
                = new LineCreateRequest("신분당선", "bg-red-600", gangnamStationId, pangyoStationId, 10);

        //when
        ExtractableResponse<Response> response = 지하철_노선을_등록_api를_호출한다(lineCreateRequest);

        //then
        지하철_노선_등록_api_응답코드를_검증한다(response.statusCode());
        지하철_노선_등록이_정상적인지_검증한다(((Number) response.jsonPath().get("id")).longValue());
    }

    void 지하철_노선_등록이_정상적인지_검증한다(Long targetId) {
        ExtractableResponse<Response> response = 전체_지하철_노선_목록_조회_api를_호출한다();
        List<Object> responseIds = response.jsonPath().getList("id");
        List<Long> responseIdList = responseIds.stream()
                .map(r -> ((Number) r).longValue())
                .collect(Collectors.toList());

        assertThat(responseIdList).contains(targetId);
    }

    void 지하철_노선_등록_api_응답코드를_검증한다(int statusCode) {
        assertThat(statusCode).isEqualTo(HttpStatus.SC_CREATED);
    }

    ExtractableResponse<Response> 지하철_노선을_등록_api를_호출한다(LineCreateRequest lineCreateRequest) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(lineCreateRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        return response;
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @Test
    void 지하철_노선_목록을_조회한다() {
        //given
        /* 첫번째 노선 등록 */
        신분당선_노선_테스트_데이터_생성();

        /* 두번째 노선 등록 */
        구호선_노선_테스트_데이터_생성();

        //when
        ExtractableResponse<Response> response = 전체_지하철_노선_목록_조회_api를_호출한다();

        //then
        assertThat(response.jsonPath().getList(".").size()).isEqualTo(2);
    }

    ExtractableResponse<Response> 신분당선_노선_테스트_데이터_생성() {
        Long gangnamStationId = 주어진_이름으로_지하철역을_생성한다("강남역");
        Long pangyoStationId = 주어진_이름으로_지하철역을_생성한다("판교역");

        LineCreateRequest sinboonLineCreateRequest
                = new LineCreateRequest("신분당선", "bg-red-600", gangnamStationId, pangyoStationId, 10);

        return 지하철_노선을_등록_api를_호출한다(sinboonLineCreateRequest);
    }

    ExtractableResponse<Response> 구호선_노선_테스트_데이터_생성() {
        Long sinnonhyeonStationId = 주어진_이름으로_지하철역을_생성한다("신논현역");
        Long sapyongStationId = 주어진_이름으로_지하철역을_생성한다("사평역");

        LineCreateRequest lineNineCreateRequest
                = new LineCreateRequest("9호선", "bg-yellow-400", sinnonhyeonStationId, sapyongStationId, 10);

        return 지하철_노선을_등록_api를_호출한다(lineNineCreateRequest);
    }

    ExtractableResponse<Response> 전체_지하철_노선_목록_조회_api를_호출한다() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();

        return response;
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    void 지하철_노선_조회한다() {
        //given
        ExtractableResponse<Response> response = 신분당선_노선_테스트_데이터_생성();
        Long createdId = ((Number) response.jsonPath().get("id")).longValue();

        //when
        ExtractableResponse<Response> result = 지하철_노선_아이디를_바탕으로_조회하는_api를_호출한다(createdId);

        //then
        assertThat((String) result.jsonPath().get("name")).isEqualTo("신분당선");
    }

    ExtractableResponse<Response> 지하철_노선_아이디를_바탕으로_조회하는_api를_호출한다(Long id) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{id}", id)
                .then().log().all()
                .extract();

        return response;
    }


    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @Test
    void 지하철_노선을_수정한다() {
        //given
        ExtractableResponse<Response> response = 신분당선_노선_테스트_데이터_생성();
        Long createdId = ((Number) response.jsonPath().get("id")).longValue();

        //when
        LineChangeRequest lineChangeRequest = new LineChangeRequest("98호선", "super_red");
        지하철_노선_데이터_수정_api를_호출한다(createdId, lineChangeRequest);

        //then
        ExtractableResponse<Response> result = 지하철_노선_아이디를_바탕으로_조회하는_api를_호출한다(createdId);
        assertThat((String) result.jsonPath().get("name")).isEqualTo("98호선");
        assertThat((String) result.jsonPath().get("color")).isEqualTo("super_red");
    }

    void 지하철_노선_데이터_수정_api를_호출한다(Long id, LineChangeRequest lineChangeRequest) {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineChangeRequest)
                .when().put("/lines/{id}", id)
                .then().log().all();
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @Test
    void 지하철_노선을_삭제한다() {
        //given
        ExtractableResponse<Response> response = 신분당선_노선_테스트_데이터_생성();
        Long createdId = ((Number) response.jsonPath().get("id")).longValue();

        //when
        아이디_기반으로_지하철_노선_데이터를_삭제_api를_호출한다(createdId);

        //then
        ExtractableResponse<Response> result = 지하철_노선_아이디를_바탕으로_조회하는_api를_호출한다(createdId);
        assertThat(result.statusCode()).isNotEqualTo(HttpStatus.SC_OK);
    }


    void 아이디_기반으로_지하철_노선_데이터를_삭제_api를_호출한다(Long id) {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{id}", id)
                .then().log().all();
    }
}
