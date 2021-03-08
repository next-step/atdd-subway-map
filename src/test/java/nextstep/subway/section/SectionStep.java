package nextstep.subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.Extractor;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionStep {
    private static final String DEFAULT_PATH = "/lines/%d/sections";

    public static ExtractableResponse<Response> 지하철_노선_구간_등록(Long extractLineId, SectionRequest 금정_범계) {
        String path = 서비스_호출_경로_생성(extractLineId, null);
        return Extractor.post(path, 금정_범계);
    }

    public static void 기존_지하철_구간_하행역_신규_상행역_일치함(ExtractableResponse<Response> newCreatedSectionResponse, StationResponse ...stations) {
        List<String> lineStations = 지하철_노선_구간_추출됨(newCreatedSectionResponse);
        List<String> stationNames = Stream.of(stations)
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        assertThat(lineStations).containsAll(stationNames);
    }

    public static List<String> 지하철_노선_구간_추출됨(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getList("stations.name", String.class);
    }

    private static String 서비스_호출_경로_생성(Long lineId, Long sectionId) {
        String path = String.format(DEFAULT_PATH, lineId);

        if (Objects.nonNull(sectionId)) {
            return path + "/" + sectionId;
        }
        return path;
    }

    public static void 지하철_노선_구간_등록_실패됨(ExtractableResponse<Response> response) {
        응답_종류_확인(response, HttpStatus.BAD_REQUEST);
    }

    private static void 응답_종류_확인(ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    public static SectionRequest 지하철_노선_구간_생성(StationResponse upStation, StationResponse downStation, int distance) {
        return SectionRequest.Builder()
                .upStationId(upStation.getId())
                .downStationId(downStation.getId())
                .distance(distance)
                .build();
    }

    public static void 응답_에러_메세지_확인(ExtractableResponse<Response> response, String msg) {
        assertThat(response.jsonPath().getObject("cause", String.class)).isEqualTo(msg);
    }

    public static ExtractableResponse<Response> 지하철_노선_구간_삭제_요청(Long lineId, Long stationId) {
        String path = 서비스_호출_경로_생성(lineId, null);
        path += "?stationId=" + stationId;
        return Extractor.delete(path);
    }

    public static void 지하철_노선_구간_삭제됨(ExtractableResponse<Response> deletedResponse) {
        assertThat(deletedResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 지하철_노선_지하철역_삭제됨(ExtractableResponse<Response> response, Long stationId) {
        List<Long> stationIds = response.jsonPath()
                .getList("stations.id", Long.class);

        assertThat(stationIds).isNotIn(stationId);
    }

    public static void 지하철_노선_구간_삭제_실패됨(ExtractableResponse<Response> response) {
        응답_종류_확인(response, HttpStatus.BAD_REQUEST);
    }
}
