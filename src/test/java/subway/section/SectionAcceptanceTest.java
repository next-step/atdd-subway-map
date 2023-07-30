package subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.common.exception.ApiError;
import subway.common.exception.ErrorMessage;
import subway.line.LineRequest;
import subway.line.LineResponse;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
public class SectionAcceptanceTest {
    static Map<String, Long> stationMap = new HashMap<>();
    LineResponse lineInfo;
    @BeforeEach
    @DisplayName("라인생성하기")
    void makeLine(){
        Map<String, String> a = SectionStep.지하철역_요청_만들기("A");
        Map<String, String> b = SectionStep.지하철역_요청_만들기("B");
        Map<String, String> c = SectionStep.지하철역_요청_만들기("C");

        stationMap.put("A", SectionStep.지하철역_생성(a).jsonPath().getLong("id"));
        stationMap.put("B", SectionStep.지하철역_생성(b).jsonPath().getLong("id"));
        stationMap.put("C", SectionStep.지하철역_생성(c).jsonPath().getLong("id"));

        LineRequest lineRequest = new LineRequest("1호선", "yellow", stationMap.get("A"), stationMap.get("B"), 7);
        lineInfo = SectionStep.라인생성(lineRequest).as(LineResponse.class);
    }


    @DisplayName("구간 등록 기능")
    @Test
    void makeSections(){
        ExtractableResponse<Response> response = SectionStep.구간생성(new SectionRequest(3L, 2L, 5), lineInfo.getId());
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("라인의 하행선이 아닌 id 입력시 오류")
    @Test
    void makeTestException(){
        ExtractableResponse<Response> response = SectionStep.구간생성(new SectionRequest(2L, 3L, 5), lineInfo.getId());
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.as(ApiError.class).getMessage()).isEqualTo(ErrorMessage.IS_NOT_DOWNSTAION.getMessage());
    }

    @DisplayName("구간 삭제 기능")
    @Test
    void deleteSections(){
        SectionStep.구간생성(new SectionRequest(3L, 2L,  5), lineInfo.getId());
        ExtractableResponse<Response> response = SectionStep.구간삭제(lineInfo.getId(), 3L);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("마지막 구간이 아니면 에러발생")
    @Test
    void deleteSectionsException(){
        SectionStep.구간생성(new SectionRequest(3L, 2L,  5), lineInfo.getId());
        ExtractableResponse<Response> response = SectionStep.구간삭제(lineInfo.getId(), 2L);
        assertThat(response.as(ApiError.class).getMessage()).isEqualTo(ErrorMessage.IS_NOT_LAST_STATION.getMessage());
    }

    @DisplayName("구간이 1개 인 경우 삭제 불가능")
    @Test
    void noSectionException(){
        ExtractableResponse<Response> response = SectionStep.구간삭제(lineInfo.getId(), 2L);
        assertThat(response.as(ApiError.class).getMessage()).isEqualTo(ErrorMessage.THERE_IS_NO_SECTIONS.getMessage());
    }


}
