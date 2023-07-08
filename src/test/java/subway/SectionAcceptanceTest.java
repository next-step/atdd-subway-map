package subway;


import common.DependentTest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.line.Line;
import subway.line.LineRequest;
import subway.line.LineResponse;
import subway.section.SectionRequest;
import subway.section.StationDoesNotMatchException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 관련 기능")
@DependentTest
@Sql(scripts = {"/clear.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class SectionAcceptanceTest {

    private LineResponse 분당선;
    private StationResponse 야탑역;
    private StationResponse 이매역;
    @BeforeEach
    void init() {
        야탑역 = StationTestHelper.createStation("야탑역");
        이매역 = StationTestHelper.createStation("이매역");
        분당선 = LineTestHelper.createLine(new LineRequest("분당선", "bg-red-600", 야탑역.getId(), 이매역.getId(), 20L));
    }

    /**
     * GIVEN 지하철 노선이 등록되어 있고, 하나의 구간이 등록되어 있는 상태에서
     * WHEN 지하철 구간을 등록하면
     * THEN 지하철 구간이 등록된다.
     */
    @Test
    @DisplayName("지하철 구간을 등록한다.")
    void createSection() {
        // given
        StationResponse 서현역 = StationTestHelper.createStation("서현역");

        // when
        LineTestHelper.createSection(분당선.getId(), 이매역.getId(), 서현역.getId(), 10L);

        // then
        LineResponse lineResponse = LineTestHelper.selectLine(분당선.getId());
        assertThat(lineResponse.getStations())
                .hasSize(3)
                .extracting(StationResponse::getName)
                .containsExactly("야탑역", "이매역", "서현역");
    }

    @Test
    @DisplayName("지하철 구간 등록시 구간의 하행 종점역과 요청시 상행 종점역이 일치하지 않으면 예외가 발생한다.")
    void createSectionWithInvalidUpStation() {
        // given
        StationResponse 서현역 = StationTestHelper.createStation("서현역");

        // when
        ExtractableResponse<Response> response = RestAssured
                .given()
                    .body(new SectionRequest(야탑역.getId(), 서현역.getId(), 5L))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/lines/{id}/sections", 분당선.getId())
                .then()
                    .extract();

        assertThat(response)
                .extracting(ExtractableResponse::statusCode)
                .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    @DisplayName("가장 마지막 역을 구간제거하면 성공한다")
    void deleteSection() {
        // given
        StationResponse 서현역 = StationTestHelper.createStation("서현역");
        LineTestHelper.createSection(분당선.getId(), 이매역.getId(), 서현역.getId(), 10L);

        // when
        RestAssured
                .given()
                .when()
                    .delete("/lines/{lineId}/sections/{stationId}", 분당선.getId(), 서현역.getId())
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .extract();

        // then
        LineResponse lineResponse = LineTestHelper.selectLine(분당선.getId());
        assertThat(lineResponse.getStations())
                .hasSize(2)
                .extracting(StationResponse::getName)
                .containsExactly("야탑역", "이매역");
    }

    @Test
    @DisplayName("마지막역이 아닌 역을 구간제거하면 실패한다.")
    void deleteSectionWithNotLastStation() {
        // given
        StationResponse 서현역 = StationTestHelper.createStation("서현역");
        LineTestHelper.createSection(분당선.getId(), 이매역.getId(), 서현역.getId(), 10L);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given()
                .when()
                    .delete("/lines/{lineId}/sections/{stationId}", 분당선.getId(), 이매역.getId())
                .then()
                    .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    @DisplayName("구간이 하나일 때 삭제하면 실패한다.")
    void deleteSectionWithOnlyOneSection() {
        // when
        ExtractableResponse<Response> response = RestAssured
                .given()
                .when()
                    .delete("/lines/{lineId}/sections/{stationId}", 분당선.getId(), 야탑역.getId())
                .then()
                    .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
