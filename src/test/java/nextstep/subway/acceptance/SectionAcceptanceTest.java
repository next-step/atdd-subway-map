package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.SectionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.acceptance.LineAcceptanceUtil.지하철_노선_생성_요청;
import static nextstep.subway.acceptance.StationAcceptanceUtil.지하철_역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관리 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    private static String location;

    @BeforeEach
    void init() {
        지하철_역_생성_요청("동암역");
        지하철_역_생성_요청("강남역");
        지하철_역_생성_요청("부평역");
        지하철_역_생성_요청("신촌역");

        ExtractableResponse<Response> response = 지하철_노선_생성_요청(new LineRequest("신분당선", "bg-red-600", 4L, 2L, 10));
        location = response.header("location");
    }

    @DisplayName("지하철 노선에 구간 생성")
    @Test
    void createSection() {
        // given: 역을 생성한다.
        // and: 노선을 생성한다.
        // when: 구간 생성을 요청하면
        // then: 구간 생성이 성공한다.

        SectionRequest requestBody = new SectionRequest("4", "2", 10);
        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when()
                .post(location + "/sections")
                .then()
                .log()
                .all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.")
    @Test
    void createSection2() {

        // when: 신구간의 상행역이 등록되어있는 하행 종점역이 아닌 경우
        SectionRequest requestBody = new SectionRequest("2", "4", 10);
        ExtractableResponse<Response> response = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when()
                .post(location + "/sections")
                .then()
                .log()
                .all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
