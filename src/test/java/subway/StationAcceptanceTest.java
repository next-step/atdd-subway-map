package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import subway.fixture.StationTestFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
    void createStation() {
        String 지하철_이름_강남역 = "강남역";
        ExtractableResponse<Response> response =
                StationTestFixture.createStationFromName(지하철_이름_강남역);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames =
                allStations().jsonPath().getList("name", String.class);
        assertThat(stationNames).containsAnyOf(지하철_이름_강남역);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Sql(value = "/sql/delete-station.sql")
    @Test
    void showStations() {
        // given
        String 지하철_이름_강남역 = "강남역";
        String 지하철_이름_역삼역 = "역삼역";
        List<String> inputNames = List.of(
                지하철_이름_강남역,
                지하철_이름_역삼역
        );

        inputNames.forEach(StationTestFixture::createStationFromName);
        int inputSize = inputNames.size();

        // when
        ExtractableResponse<Response> response = allStations();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> stationNames = response.jsonPath().getList("name", String.class);
        assertThat(stationNames).hasSize(inputSize);
        assertThat(stationNames).containsExactly(지하철_이름_강남역, 지하철_이름_역삼역);
    }


    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStations(){
        int id = StationTestFixture.createStationFromName("강남역").jsonPath().getInt("id");
        deleteById(id);

        int size = allStations().jsonPath().getList("name", String.class).size();
        assertThat(size).isEqualTo(0);
    }

    private void deleteById(int id) {
        RestAssured.given().log().all()
                .when().delete("/stations/{id}",id)
                .then().log().all()
                .extract();
    }




    private static ExtractableResponse<Response> allStations() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    private Response stationsById(int id) {
        return RestAssured.given().log().all()
                .when().get("/stations/{id}",id);

    }
}
