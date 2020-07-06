package nextstep.subway.line.acceptance;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static nextstep.subway.line.acceptance.step.LineAcceptanceStep.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        // 지하철_노선_생성_요청
        ExtractableResponse<Response> response = 지하철_노선을_생성한다("신분당선", "bg-red-600", LocalTime.of(5, 30), LocalTime.of(23, 30), 5);

        // then
        // 지하철_노선_생성됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(HttpHeaders.LOCATION)).isNotBlank();
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLine2() {
        // given
        // 지하철_노선_등록되어_있음
        지하철_노선을_생성한다("신분당선", "bg-red-600", LocalTime.of(5, 30), LocalTime.of(23, 30), 5);

        ExtractableResponse<Response> response = 지하철_노선을_생성한다("신분당선", "bg-red-600", LocalTime.of(5, 30), LocalTime.of(23, 30), 5);

        // then
        // 지하철_노선_생성_실패됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        // 지하철_노선_등록되어_있음
        // 지하철_노선_등록되어_있음
        String newLineName1 = "신분당선";
        String newLineName2 = "공항철도";

        지하철_노선을_생성한다(newLineName1, "bg-red-600", LocalTime.of(5, 30), LocalTime.of(23, 30), 5);
        지하철_노선을_생성한다(newLineName2, "bg-red-500", LocalTime.of(5, 30), LocalTime.of(23, 30), 5);

        // when
        // 지하철_노선_목록_조회_요청
        ExtractableResponse<Response> getLinesRequest = 지하철_노선_목록을_조회한다();

        // then
        // 지하철_노선_목록_응답됨
        // 지하철_노선_목록_포함됨
        assertThat(getLinesRequest.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<LineResponse> existLines = getLinesRequest.as(new TypeRef<List<LineResponse>>() {});
        assertThat(existLines)
                .extracting(LineResponse::getName)
                .contains(newLineName1, newLineName2);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> createResponse = 지하철_노선을_생성한다("신분당선", "bg-red-600", LocalTime.of(5, 30), LocalTime.of(23, 30), 5);

        // when
        // 지하철_노선_조회_요청
        String uri = createResponse.header("Location");

        ExtractableResponse<Response> response = 지하철_노선을_조회한다(uri);

        // then
        // 지하철_노선_응답됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(LineResponse.class)).isNotNull();
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> createResponse = 지하철_노선을_생성한다("신분당선", "bg-red-600", LocalTime.of(5, 30), LocalTime.of(23, 30), 5);

        String existLineLocation = createResponse.header(HttpHeaders.LOCATION);
        String newLineName = "공항철도";
        Map<String, String> updateLineRequestParams = getLineRequestParameterMap(newLineName, "bg-red-600", LocalTime.of(5, 30), LocalTime.of(23, 30), 5);

        // when
        // 지하철_노선_수정_요청
        ExtractableResponse<Response> updateResponse = 지하철_노선을_수정한다(existLineLocation, updateLineRequestParams);

        // then
        // 지하철_노선_수정됨
        assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        ExtractableResponse<Response> getLineResponse = 지하철_노선을_조회한다(existLineLocation);
        LineResponse lineResponse = getLineResponse.as(LineResponse.class);
        assertThat(lineResponse.getName()).isEqualTo(newLineName);

    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철_노선_등록되어_있음
        ExtractableResponse<Response> createResponse = 지하철_노선을_생성한다("신분당선", "bg-red-600", LocalTime.of(5, 30), LocalTime.of(23, 30), 5);
        String existLineLocation = createResponse.header(HttpHeaders.LOCATION);

        // when
        // 지하철_노선_제거_요청
        ExtractableResponse<Response> deleteResponse = 지하철_노선을_제거한다(existLineLocation);

        // then
        // 지하철_노선_삭제됨
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        ExtractableResponse<Response> getLineResponse = 지하철_노선을_조회한다(existLineLocation);
        assertThat(getLineResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
