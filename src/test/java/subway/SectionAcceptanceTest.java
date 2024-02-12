package subway;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.http.ContentType;
import subway.dto.LineResponse;
import subway.dto.SectionResponse;

@DirtiesContext(classMode = BEFORE_CLASS)
public class SectionAcceptanceTest extends BaseAcceptanceTest {
    @BeforeEach
    void setUp() {
        databaseCleanUp.execute();
    }

    @Test
    void test_특정_노선에_구간을_등록하면_노선_조회시_등록한_구간을_확인할_수_있다() {
        //given
        createStation(역삼역);
        createStation(선릉역);
        createStation(강남역);
        createStation(왕십리역);

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
}
