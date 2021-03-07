package nextstep.subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.Extractor;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class StationStep {
    private static final String DEFAULT_PATH = "/stations";
    public static final String LOCATION = "Location";

    public static ExtractableResponse<Response> 지하철역_생성_요청(Map<String, String> station) {
        return Extractor.post(DEFAULT_PATH, station);
    }

    public static void 응답_결과_확인(ExtractableResponse<Response> response, HttpStatus status) {
        assertThat(response.statusCode()).isEqualTo(status.value());
    }

    public static void 지하철역_위치_확인(ExtractableResponse<Response> response) {
        assertThat(response.header(LOCATION)).isNotBlank();
    }

    public static ExtractableResponse<Response> 지하철역_전체_조회_요청() {
        return Extractor.get(DEFAULT_PATH);
    }

    public static void 지하철역_목록_포함됨(List<Long> expectedLineIds, List<Long> resultLineIds) {
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    public static List<Long> 지하철역_아이디_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
    }

    public static List<Long> 지하철역_위치_아이디_추출(ExtractableResponse<Response> ...createResponse) {
        return Stream.of(createResponse)
                .map(StationStep::지하철역_위치_아이디_추출)
                .collect(Collectors.toList());
    }

    public static Long 지하철역_위치_아이디_추출(ExtractableResponse<Response> createResponse) {
        return Long.parseLong(createResponse.header(LOCATION).split("/")[2]);
    }

    public static ExtractableResponse<Response> 지하철역_제거_요청(Long stationId) {

        return Extractor.delete(서비스_호출_경로_생성(stationId));
    }

    public static String 서비스_호출_경로_생성(Long createdId) {
        if (Objects.nonNull(createdId)) {
            return DEFAULT_PATH + "/" + createdId;
        }

        return DEFAULT_PATH;
    }
}
