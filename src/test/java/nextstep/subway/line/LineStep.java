package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.domain.Line;
import nextstep.subway.utils.Extractor;
import org.assertj.core.api.ListAssert;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class LineStep {

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return Extractor.get(서비스_호출_경로_생성(null));

    }

    public static List<Long> 지하철_노선_객체_리스트_반환(ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", Line.class).stream()
                .map(Line::getId)
                .collect(Collectors.toList());
    }

    public static ListAssert<Long> 지하철_노선_목록_포함됨(List<Long> createdLineIds, List<Long> resultLineIds) {
        return assertThat(resultLineIds).containsAll(createdLineIds);
    }

    public static void 응답_결과_확인(ExtractableResponse<Response> response, HttpStatus status) {
        assertThat(response.statusCode()).isEqualTo(status.value());
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(Long createdId, HashMap<String, String> params) {
        return Extractor.put(서비스_호출_경로_생성(createdId), params);
    }

    public static void 지하철_노선_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_수정_확인(ExtractableResponse<Response> response, HashMap<String, String> params) {
        Line line = response.jsonPath().getObject(".", Line.class);

        assertThat(line.getName()).isEqualTo(params.get("name"));
        assertThat(line.getColor()).isEqualTo(params.get("color"));
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(Long id) {
        return Extractor.get(서비스_호출_경로_생성(id));
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(Long createdId) {
        return Extractor.delete(서비스_호출_경로_생성(createdId));
    }

    public static void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static ExtractableResponse<Response> 지하철_노선_등록(Map<String, String> params) {
        return Extractor.post(서비스_호출_경로_생성(null), params);
    }

    public static List<Long> 지하철_노선_아이디_추출(ExtractableResponse<Response>... list) {
        return Stream.of(list)
                .map(LineStep::지하철_노선_아이디_추출)
                .collect(Collectors.toList());
    }

    public static Long 지하철_노선_아이디_추출(ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }

    public static void 지하철_노선_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    public static String 서비스_호출_경로_생성(Long createdId) {
        String path = "/lines";
        if (Objects.nonNull(createdId)) {
            return path + "/" + createdId;
        }

        return path;
    }
}
