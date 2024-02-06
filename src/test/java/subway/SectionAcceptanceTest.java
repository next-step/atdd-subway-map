package subway;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;

import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;
import subway.dto.LineResponse;

public class SectionAcceptanceTest extends BaseAcceptanceTest {

    @Test
    void test_특정_노선에_구간을_등록하면_노선_조회시_등록한_구간을_확인할_수_있다() {
        //given
        createStation(역삼역);
        createStation(선릉역);
        createStation(강남역);
        createStation(왕십리역);

        LineResponse lineResponse = createLine(getRequestParam_신분당선());

        //when
        Map<String, String> sectionPostRequest = Map.of(
                                    "downStationId", "4",
                                    "upStationId", "2",
                                    "distance", "10");
        given().body(sectionPostRequest)
               .contentType(ContentType.JSON)
               .when().post("/lines/" + lineResponse.getId() + "/sections")
               .then().log().all();

        //then
        LineResponse response = when()
                                .get("/lines/" + lineResponse.getId())
                                .then().log().all().extract().jsonPath().getObject(".", LineResponse.class);
        assertAll(
            () -> assertThat(response.countSections()).hasSize(1),
            () -> assertThat(response.getUpStationId()).isEqualTo(1),
            () -> assertThat(response.getDownStationId()).isEqualTo(4),
            () -> assertThat(response.getDistance()).isEqualTo(10)
        );
    }
}
