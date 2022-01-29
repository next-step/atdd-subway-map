package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관리 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    private String uriOfLine;
    private String upStationId;
    private String downStationId;
    private String distance;

    @BeforeEach
    public void setUp() {
        super.setUp();
        upStationId = StationSteps.지하철_역_생성_요청_및_위치_반환("강남역");
        downStationId = StationSteps.지하철_역_생성_요청_및_위치_반환("양재역");
        distance = "5";
        uriOfLine = LineSteps.지하철_노선_생성_요청("신분당선", "red", upStationId, downStationId, distance).header("Location");
    }

    /**
     * Given 새로운 구간의 상행역이 해당 노선에 하행 종점역으로 등록되어 있을 때
     * When 상행역과 미등록된 하행역을 구간 등록 요청하면
     * Then 요청이 성공한다
     */
    @DisplayName("구간 등록 성공")
    @Test
    void should_return_201_when_request_to_create_section() {
        // given
        var newDownStationId = StationSteps.지하철_역_생성_요청_및_위치_반환("양재시민의숲");
        var distance = "10";

        // when
        var response = 구간_등록_요청(downStationId, newDownStationId, distance);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Given 새로운 구간의 상행역이 해당 노선에 하행 종점역으로 등록 되어 있지 않을때
     * When 상행역과 미등록된 하행역을 구간 등록 요청하면
     * Then 요청이 실패한다
     */
    @DisplayName("하행 종점역으로 등록되지 않은 역을 상행으로 등록 요청시 실패")
    @Test
    void should_return_400_when_request_to_create_section_that_contain_unregistered_up_station() {
        // given
        var unregisteredUpStationId = StationSteps.지하철_역_생성_요청_및_위치_반환("청계산입구");
        var newDownStationId = StationSteps.지하철_역_생성_요청_및_위치_반환("양재시민의숲");
        var distance = "7";

        // when
        var response = 구간_등록_요청(unregisteredUpStationId, newDownStationId, distance);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 등록된 구간이 2개 이상일때
     * When 하행 종점역을 제거 요청하면
     * Then 요청이 성공한다.
     */
    @DisplayName("구간 제거 성공")
    @Test
    void should_return_204_when_request_to_delete_section() {
        // given
        var newDownStationId = StationSteps.지하철_역_생성_요청_및_위치_반환("양재시민의숲");
        var distance = "10";
        구간_등록_요청(downStationId, newDownStationId, distance);

        // when
        var response = 구간_제거_요청(newDownStationId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 등록된 구간이 1개일때
     * When 하행 종점역을 제거 요청하면
     * Then 요청이 실패한다.
     */
    @DisplayName("구간이 1개일 경우 제거 실패")
    @Test
    void should_return_400_when_section_is_only_one() {
        // when
        var response = 구간_제거_요청(downStationId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 등록된 구간이 2개 이상일때
     * When 등록되지 않은 역을 제거 요청하면
     * Then 요청이 실패한다.
     */
    @DisplayName("미등록된 역 제거 실패")
    @Test
    void should_return_400_when_request_to_delete_unregistered_station() {
        // given
        var newDownStationId = StationSteps.지하철_역_생성_요청_및_위치_반환("양재시민의숲");
        var distance = "10";
        구간_등록_요청(downStationId, newDownStationId, distance);

        var unregisteredStationId = StationSteps.지하철_역_생성_요청_및_위치_반환("청계산입구");

        // when
        var response = 구간_제거_요청(unregisteredStationId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private ExtractableResponse<Response> 구간_등록_요청(String upStationId, String downStationId, String distance) {
        var params = Map.of(
                "upStationId", upStationId,
                "downStationId", downStationId,
                "distance", distance
        );

        var uri = uriOfLine + "/sections";

        return RestAssured.given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(uri)
                .then()
                .extract();
    }

    private ExtractableResponse<Response> 구간_제거_요청(String newDownStationId) {
        var uri = uriOfLine + "/sections?stationId=" + newDownStationId;

        return RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(uri)
                .then()
                .extract();
    }

}
