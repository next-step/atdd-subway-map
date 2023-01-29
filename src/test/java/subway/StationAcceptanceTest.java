package subway;

import io.restassured.RestAssured;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;


@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStationTest() {
        // when
        createStation("강남역");

        // then
        checkCanFindCreatedStationInStationList("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철역 목록 생성")
    @Test
    void createStationListTest(){
      // given
      createStation("강남역");
      createStation("역삼역");

      // then
      checkStationListSizeEqualsToCreatedStationSize(2);
    };
    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철역 삭제")
    @Test
    void deleteStationTest(){
        // given
        Integer stationId = createStationAndReturnId("강남역");

        // when
        deleteStation(stationId);

        // then
        checkCanNotFindDeletedStataionInStationList("강남역");
    }

    private void createStation(String stationName){
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        RestAssured
                .given()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .log()
                    .all()
                .when()
                    .post("/stations")
                .then()
                    .log()
                    .all()
                    .statusCode(HttpStatus.CREATED.value());
    }

    private Integer createStationAndReturnId(String stationName){
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        Integer id = RestAssured
                .given()
                    .body(params)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .log()
                    .all()
                .when()
                    .post("/stations")
                .then()
                    .log()
                    .all()
                    .extract().jsonPath().getInt("id");

        return id;
    }

    private void deleteStation(Integer stationId){

        RestAssured
                .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .log()
                    .all()
                .when()
                    .delete("/stations/{id}", stationId)
                .then()
                    .log()
                    .all();
    }

    private void checkCanFindCreatedStationInStationList(String createdStationName){
        RestAssured
                .given()
                .log()
                .all()
                .when()
                .get("/stations")
                .then()
                .body("name", hasItem(createdStationName));
    }

    private void checkStationListSizeEqualsToCreatedStationSize(Integer createdStationSize){
        RestAssured.given()
                .log()
                .all()
                .when()
                .get("/stations")
                .then()
                .body("name.size()", equalTo(createdStationSize));
    }

    private void checkCanNotFindDeletedStataionInStationList(String deletedStationName){
        RestAssured
                .given()
                .log()
                .all()
                .when()
                .get("/stations")
                .then()
                .body("name", not(hasItem(deletedStationName)));
    }
}