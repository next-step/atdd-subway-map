package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.line.LineRepository;
import subway.line.LineRequest;
import subway.line.LineResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    @Autowired
    private LineRepository lineRepository;

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 등록하고 생성한 노선을 찾을 수 있다..")
    @Test
    public void createLineTest() {
        StationAcceptanceTest stationAcceptanceTest = new StationAcceptanceTest();
        Long upStationId = stationAcceptanceTest.createStation("지하철역1");
        Long downStationId = stationAcceptanceTest.createStation("지하철역2");

        // When
        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", upStationId, downStationId, 10L);
        createLine(lineRequest);

        // Then
        var response = getLineResponseList();
        List<String> name = response.jsonPath().getList("name", String.class);

        assertThat(name).hasSize(1);
        assertThat(name).containsExactly("신분당선");
    }



    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */

    @DisplayName("지하철 노선도를 조회할 수 있다.")
    private ExtractableResponse<Response> getLineResponseList() {
        var response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);

        return response;
    }

    @DisplayName("지하철 노선을 등록할 수 있다.")
    private void createLine(LineRequest lineRequest) {
        var response = RestAssured.given().log().all()
                .body(lineRequest)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
    }
}
