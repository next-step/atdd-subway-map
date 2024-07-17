package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.model.request.LineRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.StationAcceptanceTest.createStation;

@DisplayName("지하철노선 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    /**
     * 지하철 노선 생성
     * Given: 새로운 지하철 노선 정보를 입력하고,
     * When: 관리자가 노선을 생성하면,
     * Then: 해당 노선이 생성되고 노선 목록에 포함된다.
     */
    @DisplayName("지하철노선을 생성한다")
    @Test
    void createLine() {

        // Given/When
        Long LineId =
                createLine("신분당선"
                        , "bg-red-600"
                        , "강남역"
                        , "신논현역"
                        , 10L);

        // Then
        String lineName = showLine(LineId);

        assertThat(lineName).isEqualTo("신분당선");
    }

    /**
     * 지하철 노선 목록 조회
     * Given: 여러 개의 지하철 노선이 등록되어 있고,
     * When: 관리자가 지하철 노선 목록을 조회하면,
     * Then: 모든 지하철 노선 목록이 반환된다.
     */
    @DisplayName("지하철노선을 목록을 조회한다")
    @Test
    void showLineList() {

        // Given
        createLine("신분당선"
                , "bg-red-600"
                , "강남역"
                , "신논현역"
                , 10L);

        createLine("2호선"
                , "bg-red-600"
                , "강남역"
                , "서초역"
                , 10L);

        // Then
        List<String> lineNames = showLines();

        assertThat(lineNames).contains("신분당선","2호선");
    }

    /**
     * 지하철 노선 조회
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 조회하면,
     * Then: 해당 노선의 정보가 반환된다.
     */
    @DisplayName("특정 지하철노선을 조회한다")
    @Test
    void showLine() {
        // Given
        createLine("신분당선"
                , "bg-red-600"
                , "강남역"
                , "신논현역"
                , 10L);

        createLine("2호선"
                , "bg-red-600"
                , "강남역"
                , "서초역"
                , 10L);

        // When
        String lineName = showLine(2L);

        // Then
        assertThat(lineName).isEqualTo("2호선");
    }

    /**
     * 지하철 노선 수정
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 수정하면,
     * Then: 해당 노선의 정보가 수정된다.
     */
    @DisplayName("특정 지하철노선을 수정한다")
    @Test
    void modifyLine() {

        // Given
        Long lineId =
                createLine("신분당선"
                            , "bg-red-600"
                            , "강남역"
                            , "신논현역"
                            , 10L);

        Map<String, String> param = new HashMap<>();
        param.put("name", "분당선");
        param.put("color", "bg-red-700");

        String lineName = modifyLine(lineId, param);

        assertThat(lineName).isEqualTo("분당선");
    }

    /**
     * 지하철 노선 삭제
     * Given: 특정 지하철 노선이 등록되어 있고,
     * When: 관리자가 해당 노선을 삭제하면,
     * Then: 해당 노선이 삭제되고 노선 목록에서 제외된다.
     */
    @DisplayName("특정 지하철노선을 삭제한다")
    @Test
    void deleteLine() {

        // Given
        Long lineId =
                createLine("신분당선"
                        , "bg-red-600"
                        , "강남역"
                        , "신논현역"
                        , 10L);


        // When
        deleteLine(lineId);

        // Then
        List<String> lineNames = showLines();
        assertThat(lineNames).doesNotContain("신분당선");

    }


    private Long createLine(String name
                            , String color
                            , String upStation
                            , String downStation
                            , Long distance){

        LineRequest param =
                new LineRequest(name
                            , color
                            , createStation(upStation)
                            , createStation(downStation)
                            , distance);
        return createLine(param);
    }


    private Long createLine(LineRequest lineRequest) {
        return RestAssured.given().log().all()
                        .body(lineRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/line")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract()
                        .body().jsonPath().getLong("id");
    }

    private List<String> showLines(){
        return RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().get("/lines")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .body().jsonPath().getList("name", String.class);
    }

    private String showLine(Long id){
        return RestAssured.given().log().all()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().get("/lines/{id}", id)
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .body().jsonPath().getString("name");
    }

    private String modifyLine(Long id, Map<String, String> param){
        return RestAssured.given().log().all()
                        .body(param)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().put("/lines/{id}", id)
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .extract()
                        .body().jsonPath().getString("name");
    }

    private void deleteLine(Long id){
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/{id}", id)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

}