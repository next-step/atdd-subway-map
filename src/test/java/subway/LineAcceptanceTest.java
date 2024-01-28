package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.common.CommonApi;
import subway.common.Fixture;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
    Fixture.Station 지하철역 = Fixture.지하철역();
    Fixture.Station 새로운지하철역 = Fixture.새로운지하철역();
    Fixture.Station 또다른지하철역 = Fixture.또다른지하철역();
    Fixture.Line 신분당선 = Fixture.신분당선(지하철역, 새로운지하철역);
    Fixture.Line 분당선 = Fixture.분당선(지하철역, 또다른지하철역);

    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {
        //when
        ExtractableResponse<Response> response = CommonApi.Line.createLineBy(신분당선);
        Long lineId = response.jsonPath().getLong("id");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> getResponse = CommonApi.Line.retrieveLineBy(lineId);
        Fixture.Line line = getResponse.as(Fixture.Line.class);

        assertThat(line).isEqualTo(신분당선);
    }

    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void showLines() {
        // given
        CommonApi.Line.createLineBy(신분당선);
        CommonApi.Line.createLineBy(분당선);

        //when
        ExtractableResponse<Response> response = CommonApi.Line.listLine();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        //then
        Fixture.Line[] lines = response.as(Fixture.Line[].class);
        assertThat(lines).containsExactly(신분당선, 분당선);
    }

    @DisplayName("지하철노선을 조회한다.")
    @Test
    void retrieveLine() {
        // given
        Long lineId = CommonApi.Line.createLineBy(신분당선).jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> response = CommonApi.Line.retrieveLineBy(lineId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        //then
        Fixture.Line line = response.as(Fixture.Line.class);
        assertThat(line).isEqualTo(신분당선);
    }

    @DisplayName("지하철노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        Long lineId = CommonApi.Line.createLineBy(분당선).jsonPath().getLong("id");
        String newName = "다른분당선";
        String newColor = "bg-red-600";

        //when
        ExtractableResponse<Response> response = CommonApi.Line.updateLineBy(lineId, newName, newColor);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //then
        Fixture.Line line = response.as(Fixture.Line.class);
        assertThat(line.name).isEqualTo(newName);
        assertThat(line.color).isEqualTo(newColor);
    }

    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        Long lineId = CommonApi.Line.createLineBy(분당선).jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> response = CommonApi.Line.deleteLineBy(lineId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //then
        ExtractableResponse<Response> getResponse = CommonApi.Line.retrieveLineBy(lineId);
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }


}
