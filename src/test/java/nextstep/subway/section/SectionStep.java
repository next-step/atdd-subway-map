package nextstep.subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.dto.SectionResponse;
import nextstep.subway.utils.Extractor;
import org.assertj.core.api.Assertions;

import java.util.Objects;

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



    public static void 기존_지하철_구간_하행역_신규_상행역_일치함(Long prevDownStationId, ExtractableResponse<Response> response) {
        SectionResponse sectionResponse = 지하철_노선_구간_추출됨(response);
        Assertions.assertThat(prevDownStationId).isEqualTo(sectionResponse.getUpStation().getId());
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
}
