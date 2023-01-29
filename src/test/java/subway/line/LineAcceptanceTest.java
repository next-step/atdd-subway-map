package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.RestUtils;
import subway.line.web.dto.LineRequest;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    private List<LineRequest> requests;

    @PostConstruct
    void init() {
        createStations();
        this.requests = getRequests();
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        LineRequest request = requests.get(0);
        create(request);

        // then
        List<String> lineNames = RestUtils.getListFromResponse(getAllLines(), "name", String.class);
        assertThat(lineNames).contains(request.getName());
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        for (LineRequest request : requests) {
            create(request);
        }

        // when
        List<String> lineNames = RestUtils.getListFromResponse(getAllLines(),"name", String.class);

        // then
        assertThat(lineNames).containsAll(requests.stream().map(LineRequest::getName).collect(Collectors.toList()));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void modifyLine() {

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {

    }

    private List<LineRequest> getRequests() {
        LineRequest request1 = LineRequest.builder()
                .name("신분당선")
                .color("bg-red-600")
                .upStationId(1L)
                .downStationId(2L)
                .distance(10)
                .build();

        LineRequest request2 = LineRequest.builder()
                .name("분당선")
                .color("bg-yellow-600")
                .upStationId(1L)
                .downStationId(3L)
                .distance(10)
                .build();

        return List.of(request1, request2);
    }

    private void createStations() {
        List<String> names = List.of("강남역", "양재역", "판교역");
        for (String name : names) {
            RestAssured.given().log().all()
                    .body(Map.of("name", name))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post("/stations")
                    .then().log().all()
                    .statusCode(HttpStatus.CREATED.value());
        }
    }

    private ExtractableResponse<Response> create(LineRequest request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
    }

    private ExtractableResponse<Response> getAllLines() {
        return RestAssured.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

}
