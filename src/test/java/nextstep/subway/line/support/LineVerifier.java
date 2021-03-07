package nextstep.subway.line.support;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.assertj.core.api.Assertions;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class LineVerifier {

    public static void 지하철_노선_등록됨(LineRequest givenLine, ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank(),
                () -> assertThat(response.jsonPath().getString("id")).isNotNull(),
                () -> assertThat(response.jsonPath().getString("name")).isEqualTo(givenLine.getName()),
                () -> assertThat(response.jsonPath().getString("color")).isEqualTo(givenLine.getColor()),
                () -> assertThat(response.jsonPath().getString("createdDate")).isNotNull(),
                () -> assertThat(response.jsonPath().getString("modifiedDate")).isNotNull()
        );
    }

    public static void 지하철_노선_조회됨(ExtractableResponse<Response> foundApiResponse, LineResponse foundedResponse, LineResponse createdResponse) {
        assertAll(
                () -> assertThat(foundApiResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(foundApiResponse.jsonPath().getString("stations")).isNotNull(),
                () -> assertThat(foundedResponse.getId()).isEqualTo(createdResponse.getId()),
                () -> assertThat(foundedResponse.getName()).isEqualTo(createdResponse.getName()),
                () -> assertThat(foundedResponse.getColor()).isEqualTo(createdResponse.getColor()),
                () -> assertThat(foundedResponse.getModifiedDate()).isEqualTo(createdResponse.getModifiedDate()),
                () -> assertThat(foundedResponse.getCreatedDate()).isEqualTo(createdResponse.getCreatedDate())
        );
    }

    public static void 지하철_노선_목록_조회됨(Long createdId1, Long createdId2, ExtractableResponse<Response> foundedLinesResponse) {
        List<Long> createdIds = Arrays.asList(createdId1, createdId2).stream().collect(Collectors.toList());
        List<Long> lineIds = foundedLinesResponse.jsonPath().getList(".", LineResponse.class).stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());

        assertThat(foundedLinesResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(lineIds).containsAll(createdIds);
    }

    public static void 지하철_노선_생성_실패됨(ExtractableResponse<Response> response) {
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.jsonPath().getString("errorMessage")).isNotNull()
        );
    }

    public static void 지하철_노선_수정됨(ExtractableResponse<Response> updatedApiResponse, LineResponse updatedResponse, String updateColor) {
        assertAll(
                () -> assertThat(updatedApiResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(updatedResponse.getColor()).isEqualTo(updateColor)
        );
    }
}
