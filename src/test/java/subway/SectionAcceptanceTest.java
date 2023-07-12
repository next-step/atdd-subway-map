package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import subway.dto.response.LineResponse;
import subway.dto.response.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static subway.common.LineFixture.createLine;
import static subway.common.LineFixture.getLines;
import static subway.common.SectionFixture.지하철_노선_구간_등록;
import static subway.common.SectionFixture.지하철_노선_구간_제거_요청;
import static subway.common.StationFixture.createStationAndGetInfo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@DisplayName("구간 관련 기능")
public class SectionAcceptanceTest {

    private static final int DISTANCE = 10;
    private Long 신사역, 강남역;

    private Long 신분당선;


    @BeforeEach
    void setting(){
        신사역 = createStationAndGetInfo("신사역").getId();
        강남역 = createStationAndGetInfo("강남역").getId();

        신분당선 = createLine("신분당선", "bg-red-600", 신사역, 강남역, DISTANCE).jsonPath().getLong("id");
    }


    @DisplayName("구간 등록")
    @Test
    void createSectionTest() {
        Long 광교역 = createStationAndGetInfo("광교역").getId();

        ExtractableResponse<Response> sectionResponse = 지하철_노선_구간_등록(신분당선,강남역,광교역,DISTANCE);

        assertThat(sectionResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<StationResponse> list = getLines().jsonPath().getList("", LineResponse.class).get(0).getStations();

        assertThat(list).hasSize(3);
    }

    @DisplayName("구간 등록. 구간의 상행이 노선의 하행이 아닐 경우에 에러 반환")
    @Test
    void createSectionExceptionWhenNotMachUpStation() {
        Long 광교역 = createStationAndGetInfo("광교역").getId();

        ExtractableResponse<Response> response = 지하철_노선_구간_등록(신분당선,신사역,광교역,DISTANCE);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("구간 등록. 해당 노선에 등록되어있는 역일 경우에 에러 반환")
    @Test
    void createSectionExceptionWhenAlreadyRegistered() {
        //given
        Long 광교역 = createStationAndGetInfo("광교역").getId();

        //when
        ExtractableResponse<Response> response = 지하철_노선_구간_등록(신분당선,광교역,강남역,DISTANCE);
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    @DisplayName("구간 제거")
    @Test
    void removeLineSection() {
        // given
        Long 광교역 = createStationAndGetInfo("광교역").getId();
        지하철_노선_구간_등록(신분당선,강남역, 광교역,DISTANCE);

        // when
        지하철_노선_구간_제거_요청(신분당선, 광교역);

        // then
        ExtractableResponse<Response> response = getLines();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("", LineResponse.class).get(0).getStations()).hasSize(2);
    }

    @DisplayName("구간 제거. 해당 노선에 등록되어있는 마지막 구간이 아닌 경우 에러 반환")
    @Test
    public void removeLineSectionExceptionWhenNotMachLastSections(){
        // given
        Long 광교역 = createStationAndGetInfo("광교역").getId();
        지하철_노선_구간_등록(신분당선,강남역, 광교역,DISTANCE);

        // when
        ExtractableResponse<Response> response = 지하철_노선_구간_제거_요청(신분당선, 강남역);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("구간 제거. 해당 노선의 구간이 1개인 경우 에러 반환")
    @Test
    public void removeLineSectionExceptionWhenSectionsSizeOne(){
        // when
        ExtractableResponse<Response> response = 지하철_노선_구간_제거_요청(신분당선, 강남역);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
