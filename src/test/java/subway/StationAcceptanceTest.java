package subway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@DirtiesContext
@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    private static final String 강남역 = "강남역";
    private static final String 역삼역 = "역삼역";

    /** When 지하철역을 생성하면 Then 지하철역이 생성된다 Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다 */
    @DisplayName("지하철역을 생성한다.")
    @Sql({"/sql/truncate-table.sql", "/sql/insert-line-data.sql"})
    @Test
    void createStationTest() {
        // when
        createStation(2, 강남역);

        // then
        checkCanFindCreatedStationInStationList(강남역);
    }

    /** Given 2개의 지하철역을 생성하고 When 지하철역 목록을 조회하면 Then 2개의 지하철역을 응답 받는다 */
    // TODO: 지하철역 목록 조회 인수 테스트 메서드 생성
    @DisplayName("지하철역 목록 생성")
    @Sql({"/sql/truncate-table.sql", "/sql/insert-line-data.sql"})
    @Test
    void createStationListTest() {
        // given
        createStation(2, 강남역);
        createStation(2, 역삼역);

        // then
        checkCreatedStationEqualsToStationList(강남역, 역삼역);
    }
    ;

    /** Given 지하철역을 생성하고 When 그 지하철역을 삭제하면 Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다 */
    // TODO: 지하철역 제거 인수 테스트 메서드 생성
    @DisplayName("지하철역 삭제")
    @Sql({"/sql/truncate-table.sql", "/sql/insert-line-data.sql"})
    @Test
    void deleteStationTest() {
        // given
        Integer stationId = createStation(2, 강남역).jsonPath().getInt("id");

        // when
        deleteStation(stationId);

        // then
        checkCanNotFindDeletedStataionInStationList(강남역);
    }

    private ExtractableResponse<Response> createStation(Integer lineId, String stationName) {

        Map<String, Object> params = new HashMap<>();
        params.put("lineId", 1);
        params.put("name", stationName);

        ExtractableResponse<Response> response =
                RestAssured.given()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .log()
                        .all()
                        .when()
                        .post("/stations")
                        .then()
                        .log()
                        .all()
                        .extract();

        assertEquals(HttpStatus.CREATED.value(), response.statusCode());

        return response;
    }

    private void deleteStation(Integer stationId) {

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log()
                .all()
                .when()
                .delete("/stations/{id}", stationId)
                .then()
                .log()
                .all();
    }

    private void checkCanFindCreatedStationInStationList(String... createdStationName) {

        List<String> stationList =
                RestAssured.given()
                        .log()
                        .all()
                        .when()
                        .get("/stations")
                        .then()
                        .log()
                        .all()
                        .extract()
                        .jsonPath()
                        .getList("name", String.class);

        assertThat(stationList).containsAll(List.of(createdStationName));
    }

    private void checkCreatedStationEqualsToStationList(String... createdStationName) {
        List<String> stationList =
                RestAssured.given()
                        .log()
                        .all()
                        .when()
                        .get("/stations")
                        .then()
                        .log()
                        .all()
                        .extract()
                        .jsonPath()
                        .getList("name", String.class);

        assertAll(
                () -> assertThat(stationList).containsAll(List.of(createdStationName)),
                () -> assertEquals(stationList.size(), createdStationName.length));
    }

    private void checkCanNotFindDeletedStataionInStationList(String deletedStationName) {
        RestAssured.given()
                .log()
                .all()
                .when()
                .get("/stations")
                .then()
                .body("name", not(hasItem(deletedStationName)));
    }
}
