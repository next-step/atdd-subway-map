package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SubwayLineAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    // TODO: 지하철 노선 생성에 대한 인수 테스트 코드 작성
    @DisplayName("지하철 노선을 생성합니다.")
    @Test
    void createSubwayLine() throws Exception {
        // given

        // when

        // then
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    // TODO: 지하철 노선 목록 조회에 대한 인수 테스트 코드 작성
    @DisplayName("지하철 노선 목록을 조회합니다.")
    @Test
    void getSubwayLines() throws Exception {
        // given

        // when

        // then
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다
     */
    // TODO: 지하철 노선 조회에 대한 인수 테스트 코드 작성
    @DisplayName("지하철 노선을 조회합니다.")
    @Test
    void getSubwayLine() throws Exception {
        // given

        // when

        // then
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    // TODO: 지하철 노선 수정에 대한 인수 테스트 코드 작성
    @DisplayName("지하철 노선을 수정합니다.")
    @Test
    void updateSubwayLine() throws Exception {
        // given

        // when

        // then
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    // TODO: 지하철 노선 삭제에 대한 인수 테스트 코드 작성
    @DisplayName("지하철 노선을 삭제합니다.")
    @Test
    void deleteSubwayLine() throws Exception {
        // given

        // when

        // then
    }

    private Map<String, String> createParams(List<String> keys, List<String> values) {
        if (keys.size() != values.size()) {
            throw new RuntimeException("생성하려는 key 와 value 의 length 가 같아야 합니다.");
        }

        final Map<String, String> params = new HashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            params.put(keys.get(i), values.get(i));
        }

        return params;
    }

    private ExtractableResponse<Response> createSubwayLineRequest(Map<String, String> param) {
        final ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(param)
                .when().post("/subway-line")
                .then().log().all()
                .extract();

        return response;
    }

    private ExtractableResponse<Response> getSubwayLinesRequest() {
        return null;
    }

    private ExtractableResponse<Response> getSubwayLineRequest() {
        return null;
    }

    private ExtractableResponse<Response> updateSubwayLineRequest() {
        return null;
    }

    private ExtractableResponse<Response> deleteSubwayLineRequest() {
        return null;
    }
}
