package nextstep.subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.line.LineRequestStep.*;
import static nextstep.subway.station.StationRequestStep.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

@DisplayName("지하철 구간 관련 테스트")
class SectionAcceptanceTest extends AcceptanceTest {

    private Long 신분당선, 강남역, 역삼역, 삼성역;

    @BeforeEach
    public void setUp() {
        super.beforeEach();
        신분당선 = 지하철_노선_등록되어_있음("신분당선", "red");
        강남역 = 지하철역_등록되어_있음("강남역");
        역삼역 = 지하철역_등록되어_있음("역삼역");
        삼성역 = 지하철역_등록되어_있음("삼성역");
    }

    @DisplayName("지하철 구간을 등록한다.")
    @Test
    void createSection() {
        ExtractableResponse<Response> response = 지하철_구간_등록요청(신분당선, LineRequest.of(강남역, 역삼역, 10));

        지하철_구간_등록됨(response);
    }

    @DisplayName("새로운 구간의 하행역은 현재 등록된 역일 수 없다")
    @Test
    void createSectionWithoutDuplicateDownStation() {
        지하철_구간_등록되어_있음(신분당선, LineRequest.of(강남역, 역삼역, 10));

        ExtractableResponse<Response> response = 지하철_구간_등록요청(신분당선, LineRequest.of(역삼역, 강남역, 10));

        지하철_구간_등록실패됨(response);
    }

    @DisplayName("새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 한다.")
    @Test
    void createSectionNewUpStationMustBePreviousDownStation() {
        지하철_구간_등록되어_있음(신분당선, LineRequest.of(강남역, 역삼역, 10));

        ExtractableResponse<Response> response = 지하철_구간_등록요청(신분당선, LineRequest.of(강남역, 삼성역, 10));

        지하철_구간_등록실패됨(response);
    }

    @DisplayName("지하철 구간 삭제")
    @Test
    void delete() {
        지하철_구간_등록되어_있음(신분당선, LineRequest.of(강남역, 역삼역, 10));

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/{lineId}/sections/", 신분당선)
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


    private void 지하철_구간_등록실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
    }

    private void 지하철_구간_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
        assertThat(response.body().as(LineResponse.class).getSections().size()).isEqualTo(1);
    }
}