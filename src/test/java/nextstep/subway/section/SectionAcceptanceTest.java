package nextstep.subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.line.LineSteps.지하철_노선_생성;
import static nextstep.subway.station.StationSteps.지하철_역_생성;
import static nextstep.subway.utils.HttpAssertions.응답_HTTP_CREATED;
import static nextstep.subway.utils.HttpTestUtils.리소스_ID;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선에 구간을 추가한다.")
    @Test
    void createSection() {
        // given
        Long 강남역_ID = 리소스_ID(지하철_역_생성("강남역"));
        Long 양재역_ID = 리소스_ID(지하철_역_생성("양재역"));

        LineRequest lineRequest = new LineRequest().name("신분당선").color("red")
                .upStationId(강남역_ID).downStationId(양재역_ID).distance(100);
        Long 신분당선_ID = 리소스_ID(지하철_노선_생성(lineRequest));

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all().contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/" + 신분당선_ID + "/sections")
                .then().log().all().extract();

        // then
        응답_HTTP_CREATED(response);
    }

}
