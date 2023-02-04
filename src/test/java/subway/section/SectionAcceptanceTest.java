package subway.section;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import subway.util.BaseAcceptanceTest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class SectionAcceptanceTest extends BaseAcceptanceTest {

    @BeforeEach
    void setup() {
        신분당선 = new HashMap<>();
        신분당선.put("name", SHIN_BUN_DANG);
        신분당선.put("color", "bg-red-600");
        신분당선.put("upStationId", 1);
        신분당선.put("downStationId", 2);
        신분당선.put("distance", 10);
    }

    @DisplayName("지하철 노선에 구간을 등록한다")
    @Test
    void addSection() {
        // given
        // 지하철 역을 생성하고
        ExtractableResponse<Response> createdUpStationResponse = 지하철역_생성(강남역);
        long upStationId = createdUpStationResponse.jsonPath().getLong("id");

        ExtractableResponse<Response> createdDownStationResponse = 지하철역_생성(역삼역);
        long downStationId = createdDownStationResponse.jsonPath().getLong("id");

        ExtractableResponse<Response> createdLineResponse = 지하철노선_생성(신분당선);
        long lineId = createdLineResponse.jsonPath().getLong("id");

        // when
        // 지하철 노선에 구간을 등록하면
        ExtractableResponse<Response> createdSectionResponse = 지하철_노선에_구간_등록_요청(lineId, upStationId, downStationId, 10);

        // then
        // 지하철 노선에 구간이 등록된다
        assertThat(createdSectionResponse.statusCode())
                .isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 지하철_노선에_구간_등록_요청(final long lineId, final long upStationId, final long downStationId, final int i) {

        final Map<String, ? extends Number> params = Map.of(
                "upStationId", upStationId,
                "downStationId", downStationId,
                "distance", i
        );

//        MapHelper.readValue(params, null);
        return RestAssured.given().log().all()
                .body(params)
                .contentType(ContentType.JSON)
                .when().post(LINE_PATH + "/" + lineId + SECTION_PATH)
                .then().log().all().extract();
    }

}
