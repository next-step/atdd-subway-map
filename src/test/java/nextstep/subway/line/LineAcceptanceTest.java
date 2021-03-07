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
        LineVerifier.지하철_노선_등록됨(line, response);
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
        LineVerifier.지하철_노선_목록_조회됨(createdId1, createdId2, foundedLinesResponse);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @ParameterizedTest
    @CsvSource(value = {"1호선:blue","2호선:green","3호선:brown"}, delimiter = ':')
    void findLineById(String lineName, String color) {
        //Given
        LineResponse createdResponse = LineSteps.지하철_노선이_등록됨(new LineRequest(lineName, color));

        //When
        ExtractableResponse<Response> foundApiResponse = LineSteps.지하철_특정노선_찾기_요청(createdResponse.getId());
        LineResponse foundedResponse = LineSteps.지하철_노선_요청_응답값(foundApiResponse);

        //Then
        LineVerifier.지하철_노선_조회됨(foundApiResponse, foundedResponse, createdResponse);
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
    void updateLine(String lineName, String color, String updateColor) {
        //Given
        LineResponse createdResponse = LineSteps.지하철_노선이_등록됨(new LineRequest(lineName, color));

        //When
        ExtractableResponse<Response> updatedApiResponse = LineSteps.지하철_노선_수정_요청(createdResponse.getId(), new LineRequest(lineName, updateColor));
        LineResponse updatedResponse = LineSteps.지하철_노선_요청_응답값(LineSteps.지하철_특정노선_찾기_요청(createdResponse.getId()));

        //Then
        LineVerifier.지하철_노선_수정됨(updatedApiResponse, updatedResponse, updateColor);
    }

    @DisplayName("등록되지 않는 노선에 수정을 요청한다")
    @ParameterizedTest
    @CsvSource(value = {"1호선:blue:blue-600", "2호선:green:green-300", "3호선:brown:brown-200"}, delimiter = ':')
    void notFoundUpdateLine(String lineName, String color, String updateColor) {
        //Given
        LineResponse createdResponse = LineSteps.지하철_노선이_등록됨(new LineRequest(lineName, color));

        //When
        ExtractableResponse<Response> response = LineSteps.지하철_노선_수정_요청(createdResponse.getId()+1, new LineRequest(lineName, updateColor));

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

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @ParameterizedTest
    @CsvSource(value = {"1호선:blue","2호선:green","3호선:brown"}, delimiter = ':')
    void createLineWithDuplicationName(String lineName, String color) {
        //Given
        LineSteps.지하철_노선이_등록됨(new LineRequest(lineName, color));

        //When
        ExtractableResponse<Response> response = LineSteps.지하철_노선_생성_요청(new LineRequest(lineName, color));

        //Then
        LineVerifier.지하철_노선_생성_실패됨(response);
    }

}
