package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class StationRestAssuredTest {
    @DisplayName("주어진 아이디의 지하철역을 삭제한다.")
    public void deleteStation(Long id) {
        var deleteResponse = RestAssured.given().log().all()
                .when().delete("/stations/" + id)
                .then().log().all()
                .extract();

        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


    @DisplayName("주어진 이름의 지하철역을 생성한다.")
    public Long createStation(String station) {
        Map<String, String> params = new HashMap<>();
        params.put("name", station);

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.body().jsonPath().getLong("id");
    }

    @DisplayName("지하철역 목록을 조회한다.")
    public List<String> getStationNameList() {
        var response = RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);

        return response.jsonPath().getList("name", String.class);

    }
}
