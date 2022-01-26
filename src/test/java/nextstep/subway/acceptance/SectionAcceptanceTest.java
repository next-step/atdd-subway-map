package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.linestep.LineRequestStep.*;
import static nextstep.subway.acceptance.stationstep.StationRequestStep.역_생성;
import static nextstep.subway.acceptance.testenum.TestLine.신분당선;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *  Feature     : 노선의 지하철역 구간 관리
 *  Backgound   : 노선이 있어야하고, 지하철역이 최소 2개 이상 존재해야한다.
 */
@DisplayName("노선의 지하철역 구간 관리")
public class SectionAcceptanceTest extends AcceptanceTest{
    private Long 상행선Id;
    private Long 하행선Id;
    private int 거리;
    private Long 노선Id;

    @BeforeEach
    void 노선과_지하철역_미리_생성() {
        상행선Id = extractId(역_생성("강남역"));
        하행선Id = extractId(역_생성("역삼역"));
        거리 = 10;
        노선Id = extractId(노선_생성(신분당선));
    }

    private Long extractId(ExtractableResponse<Response> response) {
        return response.body().jsonPath().getLong("id");
    }

    /**
     *  Scenario: 생성된 노선과 지하철역들을 통해 구간을 등록한다.
     *  when    : 구간 등록을 요청하면
     *  then    : 구간이 등록된다.
     */
    @DisplayName("구간 등록")
    @Test
    void 구간_등록() {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("downStationId", String.valueOf(하행선Id));
        requestBody.put("upStationId", String.valueOf(상행선Id));
        requestBody.put("distance", 거리);

        // when
        ExtractableResponse<Response> postResponse = RestAssured
                .given().log().all()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.ALL_VALUE)

                .when()
                .post("/lines/" + 노선Id + "/sections")

                .then().log().all()
                .extract();

        // then
        assertThat(postResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> getResponse = RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)

                .when()
                .get("/lines/" + 노선Id + "/sections")

                .then().log().all()
                .extract();

        assertThat(getResponse.body().jsonPath().getLong("sections.upStationId")).isEqualTo(상행선Id);
    }

    /**
     *  Scenario: 생성된 구간을 삭제한다.
     *  given   : 주어진 노선과 지하철역을 통해 구간을 생성하고
     *  when    : 구간 삭제 요청을 하면
     *  then    : 구간이 삭제된다.
     */
    @DisplayName("구간 삭제")
    @Test
    void 구간_삭제() {
        // given

        // when

        // then
    }
}
