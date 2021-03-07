package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.support.LineSteps;
import nextstep.subway.line.support.LineVerifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        //Given
        LineRequest line = new LineRequest("1호선", "blue");

        //When
        ExtractableResponse<Response> response = LineSteps.지하철_노선_생성_요청(line);

        //Then
        LineVerifier.지하철_노선_등록검증(line, response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void findAllLines() {
        //Given
        Long createdId1 = LineSteps.지하철_노선이_등록됨(new LineRequest("1호선", "blue")).getId();
        Long createdId2 = LineSteps.지하철_노선이_등록됨(new LineRequest("2호선", "green")).getId();

        //When
        ExtractableResponse<Response> foundedLinesResponse = LineSteps.지하철_노선_목록_조회요청();

        //Then
        assertAll(
                () -> assertThat(foundedLinesResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> LineVerifier.지하철_노선_목록_조회검증(createdId1, createdId2, foundedLinesResponse)
        );
    }

    @DisplayName("지하철 노선을 조회한다.")
    @ParameterizedTest
    @CsvSource(value = {"1호선:blue","2호선:green","3호선:brown"}, delimiter = ':')
    void findLineById(String station, String color) {
        //Given
        LineResponse createdResponse = LineSteps.지하철_노선이_등록됨(new LineRequest(station, color));

        //When
        ExtractableResponse<Response> foundApiResponse = LineSteps.지하철_특정노선_찾기_요청(createdResponse.getId());
        LineResponse foundedResponse = LineSteps.지하철_노선_요청_응답값(foundApiResponse);

        //Then
        assertAll(
                () -> assertThat(foundApiResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(foundApiResponse.jsonPath().getString("stations")).isNotNull(),
                () -> LineVerifier.지하철_노선_조회검증(foundedResponse, createdResponse)
        );
    }

    @DisplayName("등록되지 않는 지하철 노선을 조회한다.")
    @Test
    void notFoundLine() {
        //Given
        LineResponse createdLine = LineSteps.지하철_노선이_등록됨(new LineRequest("1호선", "blue"));

        //When
        ExtractableResponse<Response> response = LineSteps.지하철_특정노선_찾기_요청(createdLine.getId()+1);

        //Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("지하철 노선을 수정한다.")
    @ParameterizedTest
    @CsvSource(value = {"1호선:blue:blue-600", "2호선:green:green-300", "3호선:brown:brown-200"}, delimiter = ':')
    void updateLine(String station, String color, String updateColor) {
        //Given
        LineResponse createdResponse = LineSteps.지하철_노선이_등록됨(new LineRequest(station, color));

        //When
        ExtractableResponse<Response> updatedApiResponse = LineSteps.지하철_노선_수정_요청(createdResponse.getId(), new LineRequest(station, updateColor));
        LineResponse updatedResponse = LineSteps.지하철_노선_요청_응답값(LineSteps.지하철_특정노선_찾기_요청(createdResponse.getId()));

        //Then
        assertAll(
                () -> assertThat(updatedApiResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(updatedResponse.getColor()).isEqualTo(updateColor)
        );
    }

    @DisplayName("등록되지 않는 노선에 수정을 요청한다")
    @ParameterizedTest
    @CsvSource(value = {"1호선:blue:blue-600", "2호선:green:green-300", "3호선:brown:brown-200"}, delimiter = ':')
    void notFoundUpdateLine(String station, String color, String updateColor) {
        //Given
        LineResponse createdResponse = LineSteps.지하철_노선이_등록됨(new LineRequest(station, color));

        //When
        ExtractableResponse<Response> response = LineSteps.지하철_노선_수정_요청(createdResponse.getId()+1, new LineRequest(station, updateColor));

        //Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        //Given
        LineResponse createdResponse = LineSteps.지하철_노선이_등록됨(new LineRequest("1호선", "blue"));

        //When
        ExtractableResponse<Response> response = LineSteps.지하철_노선_삭제_요청(createdResponse.getId());

        //Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
