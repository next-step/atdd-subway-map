package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        ExtractableResponse<Response> response = getDefaultRestAssured()
                .body(params)
                .post("/stations")
                .then().extract();

        // when
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames = getDefaultRestAssured().get("/stations")
                .then().extract().jsonPath().getList("name", String.class);

        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역을 조회한다.")
    @Test
    void readStation() {
        //given
        Map<String, String> params = new HashMap<>();
        String[] names = {"강남역", "범계역"};

        for(String name : names){
            params.put("name", name);
            getDefaultRestAssured().body(params).post("/stations");
        }
        //when
        List<String> stationNames = getDefaultRestAssured()
                .get("/stations")
                .then()
                .extract().jsonPath().getList("name", String.class);
        //then
        assertThat(stationNames).containsAnyOf("강남역");
        assertThat(stationNames).containsAnyOf("범계역");

    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void deleteStation() {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("name", "명동역");

        long id = getDefaultRestAssured()
                .body(params).post("/stations").jsonPath().getLong("id");

        //when
        getDefaultRestAssured().delete("/stations/"+id).then().statusCode(HttpStatus.NO_CONTENT.value());

        //then
        List<String> list = getDefaultRestAssured()
                .get("/stations").jsonPath().getList("name", String.class);

        assertThat(list).doesNotContain("명동역");
    }

    private RequestSpecification getDefaultRestAssured(){
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when();
    }

}