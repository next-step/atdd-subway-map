package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import subway.common.*;
import subway.controller.line.LineResponse;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class LineAcceptanceTest {
    Station.Fixture 지하철역;
    Station.Fixture 새로운지하철역;
    Station.Fixture 또다른지하철역;
    Line.Fixture 신분당선;
    Line.Fixture 분당선;

    @BeforeEach
    void setUpFixture() {
        지하철역 = Station.지하철역();
        새로운지하철역 = Station.새로운지하철역();
        또다른지하철역 = Station.또다른지하철역();
        신분당선 = Line.신분당선(지하철역, 새로운지하철역);
        분당선 = Line.분당선(지하철역, 또다른지하철역);
    }

    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {
        //when
        ExtractableResponse<Response> response = Line.Api.createLineBy(신분당선);
        LineResponse actual = response.as(LineResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> getResponse = Line.Api.retrieveLineBy(actual.getId());
        LineResponse line = getResponse.as(LineResponse.class);
        assertThat(actual).usingRecursiveComparison().ignoringFields("distance").isEqualTo(line);
    }

    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void showLines() {
        // given
        LineResponse expected_신분당선 = Line.Api.createLineBy(신분당선).as(LineResponse.class);
        LineResponse expected_분당선 = Line.Api.createLineBy(분당선).as(LineResponse.class);

        //when
        ExtractableResponse<Response> response = Line.Api.listLine();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        //then
        LineResponse[] lines = response.as(LineResponse[].class);
        assertThat(lines[0]).usingRecursiveComparison().ignoringFields("id", "distance").isEqualTo(expected_신분당선);
        assertThat(lines[1]).usingRecursiveComparison().ignoringFields("id", "distance").isEqualTo(expected_분당선);

    }

    @DisplayName("지하철노선을 조회한다.")
    @Test
    void retrieveLine() {
        // given
        LineResponse expected = Line.Api.createLineBy(신분당선).as(LineResponse.class);

        //when
        ExtractableResponse<Response> response = Line.Api.retrieveLineBy(expected.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        //then
        LineResponse line = response.as(LineResponse.class);
        assertThat(line).usingRecursiveComparison().ignoringFields("distance").isEqualTo(expected);
    }

    @DisplayName("지하철노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        Long lineId = Line.Api.createLineBy(분당선).jsonPath().getLong("id");
        String newName = "다른분당선";
        String newColor = "bg-red-600";

        //when
        ExtractableResponse<Response> response = Line.Api.updateLineBy(lineId, newName, newColor);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //then
        ExtractableResponse<Response> getResponse = Line.Api.retrieveLineBy(lineId);
        LineResponse line = getResponse.as(LineResponse.class);
        assertThat(line.getId()).isEqualTo(lineId);
        assertThat(line.getName()).isEqualTo(newName);
        assertThat(line.getColor()).isEqualTo(newColor);
    }

    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        Long lineId = Line.Api.createLineBy(분당선).jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> response = Line.Api.deleteLineBy(lineId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //then
        ExtractableResponse<Response> getResponse = Line.Api.retrieveLineBy(lineId);
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }


}
