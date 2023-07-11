package subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import lombok.Builder;
import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.RestAssuredTest;
import subway.line.LineCreateRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static subway.utils.LineTestUtils.지하철_노선을_등록한다;
import static subway.utils.StationTestUtils.주어진_이름으로_지하철역을_생성한다;

@DisplayName("지하철노선 관련 기능")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SectionAcceptanceTest extends RestAssuredTest {
    /**
     * given 지하철 노선과 그 노선의 상행역 하행역을 생성하면
     * when 해당 정보를 바탕으로 구간을 등록한다
     * then 등록한 노선을 조회할 수 있다
     */
    @DisplayName("구간을 생성한다.")
    @Test
    public void createSectionTest() {
        //given
        //상행역, 하행역 생성
        Long upStationId = 주어진_이름으로_지하철역을_생성한다("강남역");
        Long downStationId = 주어진_이름으로_지하철역을_생성한다("판교역");

        //노선 생성
        LineCreateRequest lineCreateRequest
                = new LineCreateRequest("신분당선", "bg-red-600", upStationId, downStationId, 10);
        Long createdLineId = 지하철_노선을_등록한다(lineCreateRequest);

        CreateSectionRequest createSectionRequest = CreateSectionRequest.builder()
                .downStationId(downStationId)
                .upStationId(upStationId)
                .distance(10)
                .build();

        //when
        Long createdSectionId = 지하철_노선에_구간을_등록한다(createdLineId, createSectionRequest);

        //then
        List<Long> sectionIds = 지하철_노선의_구간들을_조회한다(createdLineId);
        assertThat(sectionIds).contains(createdSectionId);
    }

    List<Long> 지하철_노선의_구간들을_조회한다(Long lineId) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{id}", lineId)
                .then().log().all()
                .extract();

        List<Long> result = response.jsonPath().getList("$['stations'][*]['id']", Long.class);

        return result;
    }

    Long 지하철_노선에_구간을_등록한다(Long lineId, CreateSectionRequest createSectionRequest) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(createSectionRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();

        return response.jsonPath().getLong("id");
    }

    @Builder
    @Getter
    class CreateSectionRequest {
        private Long downStationId;
        private Long upStationId;
        private int distance;
    }
}
