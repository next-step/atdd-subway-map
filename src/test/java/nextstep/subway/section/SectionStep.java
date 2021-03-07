package nextstep.subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.utils.Extractor;

import java.util.Objects;

public class SectionStep {
    private static final String DEFAULT_PATH = "/lines/%d/sections";

    public static Long 응답_아이디_추출(ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/")[2]);
    }

    public static ExtractableResponse<Response> 지하철_노선_구간_등록(Long extractLineId, SectionRequest 금정_범계) {
        String path = 서비스_호출_경로_생성(extractLineId, null);
        return Extractor.post(path, 금정_범계);
    }

    public static void 등록된_구간_상행역_하행_종점역_일치함(ExtractableResponse<Response> response) {

    }

    private static String 서비스_호출_경로_생성(Long lineId, Long sectionId) {
        String path = String.format(DEFAULT_PATH, lineId);
        if (Objects.nonNull(sectionId)) {
            return path + "/" + sectionId;
        }
        return path;
    }
}
