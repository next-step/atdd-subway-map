package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.line.dto.LineRequest;
import subway.station.dto.StationRequest;
import subway.station.dto.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.station.StationAcceptanceTest.지하철역을_생성한다;
import static subway.station.StationAcceptanceTest.지하철역이_정상적으로_생성;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 역삼역;

    @BeforeEach
    void init() {
        //given - 지하철역이 등록되어있다.

        //  when
        var 강남역_request = new StationRequest() {{
            setName("강남역");
        }};
        var 강남역_response = 지하철역을_생성한다(강남역_request);
        //  then
        지하철역이_정상적으로_생성(강남역_response);
        강남역 = 강남역_response.as(StationResponse.class);

        //  when
        var 역삼역_request = new StationRequest() {{
            setName("역삼역");
        }};
        var 역삼역_response = 지하철역을_생성한다(역삼역_request);
        //  then
        지하철역이_정상적으로_생성(역삼역_response);
        역삼역 = 역삼역_response.as(StationResponse.class);
    }

    /**
     * 지하철노선 생성
     *  - When 지하철 노선을 생성하면
     *  - Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void Should_지하철노선을_생성하면_Then_지하철노선이_생성된다() {
        // when
        var 이호선_request = new LineRequest("2호선", "blue", 강남역.getId(), 역삼역.getId(), 10L);
        ExtractableResponse<Response> 이호선_response = 지하철노선을_생성한다(이호선_request);

        // then
        지하철노선이_정상적으로_생성(이호선_response);

        // then
        // ExtractableResponse<Response> lineResponse = 지하철노선을_조회한다();
    }


    /**
     * 지하철노선목록 조회
     *  - Given 2개의 지하철 노선을 생성하고
     *  - When 지하철 노선 목록을 조회하면
     *  - Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */

    /**
     * 지하철노선 조회
     *  - Given 지하철 노선을 생성하고
     *  - When 생성한 지하철 노선을 조회하면
     *  - Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */

    /**
     * 지하철노선 수정
     *  - Given 지하철 노선을 생성하고
     *  - When 생성한 지하철 노선을 수정하면
     *  - Then 해당 지하철 노선 정보는 수정된다
     */

    /**
     * 지하철노선 삭제
     *  - Given 지하철 노선을 생성하고
     *  - When 생성한 지하철 노선을 삭제하면
     *  - Then 해당 지하철 노선 정보는 삭제된다
     */

    private ExtractableResponse<Response> 지하철노선을_생성한다(LineRequest request) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철노선을_조회한다() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    private void 지하철노선이_정상적으로_생성(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
