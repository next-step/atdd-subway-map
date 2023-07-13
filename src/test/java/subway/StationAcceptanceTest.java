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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {

    static String name = "기본값";
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    @DirtiesContext
    void createStation() {
        ExtractableResponse<Response> response = 지하철_역_추가_요청(name);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        List<String> stationNames = 지하철_역_조회();
        assertThat(stationNames).containsAnyOf(name);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    @DirtiesContext
    void showStations() {
        ExtractableResponse<Response> response_gangnam = 지하철_역_추가_요청("강남역");
        assertThat(response_gangnam.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> response_ddp = 지하철_역_추가_요청("동대문역사문화공원역");
        assertThat(response_ddp.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        List<String> list = 지하철_역_조회();
        assertThat(list.size()).isEqualTo(2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    @DirtiesContext
    void deleteStation() {
        ExtractableResponse<Response> response_gangnam = 지하철_역_추가_요청("강남역");
        assertThat(response_gangnam.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> response_ddp = 지하철_역_추가_요청("동대문역사문화공원역");
        assertThat(response_ddp.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> response_delete = 지하철_역_삭제_요청(((Response)response_ddp).getHeader("location"));
        assertThat(response_delete.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        List<String> list = 지하철_역_조회();
        assertThat(list.size()).isEqualTo(1);
    }

    public static ExtractableResponse<Response> 지하철_역_추가_요청(String name) {

        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();

    }

    public static List<String> 지하철_역_조회() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
    }

    public static ExtractableResponse<Response> 지하철_역_삭제_요청(String location) {
        return RestAssured.given().log().all()
                .when().delete(location)
                .then().log().all()
                .extract();
    }
}