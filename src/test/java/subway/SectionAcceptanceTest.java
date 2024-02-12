package subway;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.responseSpecification;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.http.ContentType;
import subway.dto.LineResponse;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;

@DirtiesContext(classMode = BEFORE_CLASS)
public class SectionAcceptanceTest extends BaseAcceptanceTest {
    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        databaseCleanUp.execute();
        createStation(역삼역);
        createStation(선릉역);
        createStation(강남역);
        createStation(왕십리역);
    }

    @Test
    void test_특정_노선에_구간을_등록하면_노선_조회시_등록한_구간을_확인할_수_있다() {
        //when
        LineResponse lineResponse = createLine(getRequestParam_신분당선());
        //then
        LineResponse response = when()
                                .get("/lines/" + lineResponse.getId())
                                .then().log().all().extract().jsonPath().getObject(".", LineResponse.class);
        List<SectionResponse> sectionsResponse = response.getSections();
        assertAll(
            () -> assertThat(sectionsResponse).hasSize(1),
            () -> assertThat(sectionsResponse.get(0).getUpStationId()).isEqualTo(1),
            () -> assertThat(sectionsResponse.get(0).getDownStationId()).isEqualTo(2),
            () -> assertThat(sectionsResponse.get(0).getDistance()).isEqualTo(10)
        );
    }

    @Test
    void 노선이_주어졌을때_해당_노선의_하행_종점역과_새로_등록하려는_구간의_상행_종점역이_같으면_해당_구간을_등록할_수_있다() throws JsonProcessingException {
        //given
        LineResponse lineResponse = createLine(getRequestParam_신분당선());

        //when
        SectionRequest sectionRequest = new SectionRequest(2L, 4L, 10);
        given().body(mapper.writeValueAsString(sectionRequest))
               .contentType(MediaType.APPLICATION_JSON_VALUE)
               .when().post("/lines/" + lineResponse.getId() + "/sections").then().log().all();

        //then
        LineResponse lineAfterResponse = when().get("/lines/" + lineResponse.getId())
                                               .then().log().all().extract().jsonPath().getObject(".", LineResponse.class);
        List<SectionResponse> sectionResponses = lineAfterResponse.getSections();
        assertAll(
            () -> assertThat(sectionResponses).hasSize(2),
            () -> assertThat(sectionResponses.get(sectionResponses.size() - 1).getUpStationName()).isEqualTo("선릉역"),
            () -> assertThat(sectionResponses.get(sectionResponses.size() - 1).getDownStationName()).isEqualTo("왕십리역"),
            () -> assertThat(sectionResponses.get(sectionResponses.size() - 1).getDistance()).isEqualTo(10)
        );
    }
}
