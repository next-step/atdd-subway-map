package subway.acceptance.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import subway.acceptance.common.handler.RequestHandler;


@DisplayName("지하철 노선의 구간 삭제 관련 기능")
@ActiveProfiles("acceptance")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionDeleteAcceptanceTest {

    /**
     * Given ??
     * When ??
     * Then ??
     */
    @DisplayName("지하철 노선에 구간을 제거한다.")
    @Test
    void deleteSection() {
        ExtractableResponse<Response> 삭제_지하철_노선의_구간_응답 = RequestHandler.삭제_지하철_노선의_구간();
    }

}