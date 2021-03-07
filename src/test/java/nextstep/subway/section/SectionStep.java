package nextstep.subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.dto.SectionResponse;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.Extractor;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionStep {
    private static final String DEFAULT_PATH = "/lines/%d/sections";

    public static Long 지하철_노선_아이디_추출(ExtractableResponse<Response> response) {
        return 응답_아이디_추출(response, 2);
    }

    public static Long 지하철_노선_구간_아이디_추출(ExtractableResponse<Response> response) {
        return 응답_아이디_추출(response, 4);
    }

    public static Long 응답_아이디_추출(ExtractableResponse<Response> response, int index) {
        return Long.parseLong(response.header("Location").split("/")[index]);
    }

    public static ExtractableResponse<Response> 지하철_노선_구간_등록(Long extractLineId, SectionRequest 금정_범계) {
        String path = 서비스_호출_경로_생성(extractLineId, null);
        return Extractor.post(path, 금정_범계);
    }



    public static void 기존_지하철_구간_하행역_신규_상행역_일치함(Long prevDownStationId, ExtractableResponse<Response> newCreatedSectionResponse) {
        SectionResponse newSectionResponse = 지하철_노선_구간_추출됨(newCreatedSectionResponse);
        assertThat(prevDownStationId).isEqualTo(newSectionResponse.getUpStation().getId());
    }

    public static SectionResponse 지하철_노선_구간_추출됨(ExtractableResponse<Response> response) {
        return response.jsonPath().getObject(".", SectionResponse.class);
    }

    private static String 서비스_호출_경로_생성(Long lineId, Long sectionId) {
        String path = String.format(DEFAULT_PATH, lineId);
        if (Objects.nonNull(sectionId)) {
            return path + "/" + sectionId;
        }
        return path;
    }

    public static void 지하철_노선_구간_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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
        path += "?stationId="+stationId;
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
}
