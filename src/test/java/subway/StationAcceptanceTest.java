package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    @BeforeEach
    @DisplayName("테이블 초기화")
    void resetTable(){
        지하철역_초기화();
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames =
                RestAssured.given().log().all()
                        .when().get("/stations")
                        .then().log().all()
                        .extract().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */

    @DisplayName("2개의 지하철역 생성후 목록조회")
    @Test
    void createTwoStations(){
        final String[] names = {"수원역", "금정역"};
        // Given
        for (String name : names){
            Map<String, String> params = 지하철역_요청_만들기(name);
            ExtractableResponse<Response> response = 지하철역_생성(params);
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        }

        //when
        List<String> nameList = 지하철역_전체조회();

        //then
        assertThat(nameList).containsExactly("수원역","금정역");
    }


    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("생성한 지하철역을 삭제하면 조회안된다.")
    @Test
    void createAndDeleteStation(){
        final String[] names = {"수원역", "금정역"};
        Map<String, Integer> resultMap = new HashMap<>();
        // Given
        for (String name : names) {
            Map<String, String> params = 지하철역_요청_만들기(name);
            ExtractableResponse<Response> response = 지하철역_생성(params);
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            resultMap.put(name, response.jsonPath().getInt("id"));
        }
        // Given
        assertThat(resultMap.size()).isEqualTo(2);
        // When
        ExtractableResponse<Response> response = 지하철역_삭제(resultMap.get("수원역"));
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        List<String> stationList = 지하철역_전체조회();

        // Then
        assertThat(stationList).isNotIn("수원역");
    }

    private ExtractableResponse<Response> 지하철역_삭제(int id) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/stations/{ID}", id)
                .then().log().all()
                .extract();
    }


    private Map<String, String> 지하철역_요청_만들기(String name){
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return params;
    }
    private ExtractableResponse<Response> 지하철역_생성(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private List<String> 지하철역_전체조회() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name");
    }
    private void 지하철역_초기화() {
        List<Integer> idList = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("id", Integer.class);
        for (int id : idList){
            지하철역_삭제(id);
        }
    }
}