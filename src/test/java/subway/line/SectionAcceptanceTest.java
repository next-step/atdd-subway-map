package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import subway.common.RestAssuredCondition;
import subway.common.RestAssuredUtils;
import subway.line.dto.response.SectionResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    String LINE_API_URI = "/lines";
    String LINE_SECTION_API_URI = "/lines/%s/sections";

    String STATION_API_URI = "/stations";

    String SLASH = "/";

    @BeforeEach
    void setUp() {
        createStation();
        createLine();
    }

    @DisplayName("구간 등록 기능")
    @Test
    void createSection() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "10");

        Long lineId = 1L;

        ExtractableResponse<Response> createSectionResponse = RestAssuredUtils.create(new RestAssuredCondition(String.format(LINE_SECTION_API_URI, lineId), params));
        assertThat(createSectionResponse.statusCode()).isEqualTo(400);
        assertThat(createSectionResponse.body().jsonPath().getString("message")).isEqualTo("노선이 일치하지 않습니다.");

        // when
        Map<String, String> stationParams = new HashMap<>();
        stationParams.put("name", "을지로역");
        RestAssuredUtils.create(new RestAssuredCondition(STATION_API_URI, stationParams));

        // then
        params.put("upStationId", "2");
        params.put("downStationId", "3");
        params.put("distance", "10");
        createSectionResponse = RestAssuredUtils.create(new RestAssuredCondition(String.format(LINE_SECTION_API_URI, lineId), params));
        assertThat(createSectionResponse.statusCode()).isEqualTo(201);
        assertThat(createSectionResponse.body().jsonPath().getObject(".", SectionResponse.class).getId()).isEqualTo(3);


    }

    @DisplayName("구간 제거 기능")
    @Test
    void deleteSection() {

        // given
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "10");

        Long lineId = 1L;

        ExtractableResponse<Response> createSectionResponse = RestAssuredUtils.create(new RestAssuredCondition(String.format(LINE_SECTION_API_URI, lineId), params));
        assertThat(createSectionResponse.statusCode()).isEqualTo(400);
        assertThat(createSectionResponse.body().jsonPath().getString("message")).isEqualTo("노선이 일치하지 않습니다.");


        Map<String, String> stationParams = new HashMap<>();
        stationParams.put("name", "을지로역");
        RestAssuredUtils.create(new RestAssuredCondition(STATION_API_URI, stationParams));


        // when
        params.put("upStationId", "2");
        params.put("downStationId", "3");
        params.put("distance", "10");
        createSectionResponse = RestAssuredUtils.create(new RestAssuredCondition(String.format(LINE_SECTION_API_URI, lineId), params));
        assertThat(createSectionResponse.statusCode()).isEqualTo(201);

        ExtractableResponse<Response> deleteSectionResponse = RestAssuredUtils.delete(new RestAssuredCondition(String.format(LINE_SECTION_API_URI + "?stationId=%s", lineId, 3)));
        assertThat(deleteSectionResponse.statusCode()).isEqualTo(204);

        // then
        ExtractableResponse<Response> getSectionResponse = RestAssuredUtils.inquiry(new RestAssuredCondition(String.format(LINE_SECTION_API_URI, lineId)));

        assertThat(getSectionResponse.statusCode()).isEqualTo(200);

        List<SectionResponse> sectionResponses = getSectionResponse.body().jsonPath().getList(".", SectionResponse.class);

        assertThat(sectionResponses.size()).isEqualTo(2);
        assertThat(sectionResponses.get(0).getStationId()).isEqualTo(1L);
        assertThat(sectionResponses.get(1).getStationId()).isEqualTo(2L);

    }

    private void createStation() {
        Map<String, String> params = new HashMap<>();

        params.put("name", "강남역");
        RestAssuredUtils.create(new RestAssuredCondition(STATION_API_URI, params));

        params.put("name", "삼성역");
        RestAssuredUtils.create(new RestAssuredCondition(STATION_API_URI, params));


    }

    private void createLine() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "bg-green-500");
        params.put("upStationId", "1");
        params.put("downStationId", "2");
        params.put("distance", "20");

        RestAssuredUtils.create(new RestAssuredCondition(LINE_API_URI, params));

    }


}
