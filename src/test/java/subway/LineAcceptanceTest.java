package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import subway.common.CommonApi;
import subway.common.LineFixture;
import subway.common.StationFixture;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class LineAcceptanceTest {

    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {
        //when
        StationFixture 지하철역 = StationFixture.지하철역();
        StationFixture 새로운지하철역 = StationFixture.새로운지하철역();
        LineFixture 신분당선 = LineFixture.신분당선(지하철역, 새로운지하철역);

        ExtractableResponse<Response> response = CommonApi.Line.createLineBy(신분당선);
        Long lineId = response.jsonPath().getLong("id");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        ExtractableResponse<Response> getResponse = CommonApi.Line.retrieveLineBy(lineId);
        LineFixture line = getResponse.as(LineFixture.class);
        assertThat(line)
                .usingRecursiveComparison()
                .ignoringFields("id", "distance")
                .isEqualTo(신분당선);
    }

    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void showLines() {
        // given
        StationFixture 지하철역 = StationFixture.지하철역();
        StationFixture 새로운지하철역 = StationFixture.새로운지하철역();
        StationFixture 또다른지하철역 = StationFixture.또다른지하철역();
        LineFixture 신분당선 = LineFixture.신분당선(지하철역, 새로운지하철역);
        LineFixture 분당선 = LineFixture.분당선(지하철역, 또다른지하철역);
        CommonApi.Line.createLineBy(신분당선);
        CommonApi.Line.createLineBy(분당선);

        //when
        ExtractableResponse<Response> response = CommonApi.Line.listLine();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        //then
        LineFixture[] lines = response.as(LineFixture[].class);
        assertThat(lines[0])
                .usingRecursiveComparison()
                .ignoringFields("id", "distance")
                .isEqualTo(신분당선);

        assertThat(lines[1])
                .usingRecursiveComparison()
                .ignoringFields("id", "distance")
                .isEqualTo(분당선);

    }

    @DisplayName("지하철노선을 조회한다.")
    @Test
    void retrieveLine() {
        // given
        StationFixture 지하철역 = StationFixture.지하철역();
        StationFixture 새로운지하철역 = StationFixture.새로운지하철역();
        LineFixture 신분당선 = LineFixture.신분당선(지하철역, 새로운지하철역);
        Long lineId = CommonApi.Line.createLineBy(신분당선).jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> response = CommonApi.Line.retrieveLineBy(lineId);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        //then
        LineFixture line = response.as(LineFixture.class);
        assertThat(line)
                .usingRecursiveComparison()
                .ignoringFields("id", "distance")
                .isEqualTo(신분당선);
    }

    @DisplayName("지하철노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        StationFixture 지하철역 = StationFixture.지하철역();
        StationFixture 또다른지하철역 = StationFixture.또다른지하철역();
        LineFixture 분당선 = LineFixture.분당선(지하철역, 또다른지하철역);

        Long lineId = CommonApi.Line.createLineBy(분당선).jsonPath().getLong("id");
        String newName = "다른분당선";
        String newColor = "bg-red-600";

        //when
        ExtractableResponse<Response> response = CommonApi.Line.updateLineBy(lineId, newName, newColor);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //then
        ExtractableResponse<Response> getResponse = CommonApi.Line.retrieveLineBy(lineId);
        LineFixture line = getResponse.as(LineFixture.class);
        assertThat(line.name).isEqualTo(newName);
        assertThat(line.color).isEqualTo(newColor);
    }

    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        StationFixture 지하철역 = StationFixture.지하철역();
        StationFixture 또다른지하철역 = StationFixture.또다른지하철역();
        LineFixture 분당선 = LineFixture.분당선(지하철역, 또다른지하철역);
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
