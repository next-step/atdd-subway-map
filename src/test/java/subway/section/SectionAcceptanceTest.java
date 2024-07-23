package subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import subway.dto.line.LineRequest;
import subway.dto.section.SectionRequest;
import subway.util.TestUtil;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.util.TestUtil.createLine;
import static subway.util.TestUtil.createStation;


@DisplayName("지하철 구간 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    private static Long 강남역;
    private static Long 양재역;
    private static Long 판교역;
    private static Long 광교역;


    @BeforeEach
    void initStation() {
        강남역 = createStation("강남역").jsonPath().getLong("id");
        양재역 = createStation("양재역").jsonPath().getLong("id");
        판교역 = createStation("판교역").jsonPath().getLong("id");
        광교역 = createStation("광교역").jsonPath().getLong("id");
    }
    /**
     * Given: 새로운 지하철 구간 정보를 입력하고,
     * When: 관리자가 새로운 구간을 등록하면
     * Then: 새로운 구간의 상행역이 해당 노선에 등록된 하행 종점역이고
     * Then: 새로운 구간의 하행 종점역이 해당 노선이 포함되지 않았으면,
     * Then: 해당 구간이 등록되고 노선 정보에 포함된다.
     */
    @DisplayName("지하철 구간을 추가한다.")
    @Test
    void createSection() {

        // given
        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역, 양재역, 10L);
        Response lineResponse = createLine(lineRequest);
        SectionRequest sectionRequest = new SectionRequest(양재역, 광교역, 10L);

        // when
        Response sectionResponse = TestUtil.createSection(lineResponse.getHeader("Location")+"/sections", sectionRequest);

        assertAll(
                () -> assertThat(sectionResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(sectionRequest.getUpStationId().equals(lineRequest.getDownStationId())),
                () -> assertThat(sectionResponse.jsonPath().getList("stations.id", Long.class))
                        .contains(sectionRequest.getDownStationId())
        );

    }

    /**
     * Given: 특정 노선의 구간이 등록되어있고,
     * When: 관리자가 해당 노선의 구간을 제거하면,
     * Then: 요청한 구간이 해당 노선의 하행 종점역이고
     * Then: 해당 노선의 구간이 2개 이상이면,
     * Then: 해당 구간이 삭제되고 노선 목록에서 제외된다.
     */
    @DisplayName("지하철 구간을 삭제한다.")
    @Test
    void deleteSection() {
        // given
        LineRequest lineRequest = new LineRequest("신분당선", "bg-red-600", 강남역, 양재역, 10L);
        String lineUrl = createLine(lineRequest).getHeader("Location");
        SectionRequest sectionRequest = new SectionRequest(양재역, 광교역, 10L);
        TestUtil.createSection(lineUrl+"/sections", sectionRequest);
        Response lineResponse = TestUtil.showLine(lineUrl);


        //when
        ExtractableResponse<Response> response =
                given().log().all()
                .when().delete(lineUrl+"/sections?stationId="+광교역)
                .then().log().all()
                .extract();

        //then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(광교역 == lineRequest.getDownStationId()),
                () -> assertThat(response.jsonPath().getList("stations.id", Long.class)).doesNotContain(광교역),
                () -> assertThat(lineResponse.jsonPath().getList("stations").size() > 2)
        );

    }


}
