package subway.domain.section;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.common.AcceptanceTest;
import subway.domain.Section;
import subway.domain.Sections;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static subway.domain.line.LineApiTest.지하철노선의_마지막_구간을_조회한다;
import static subway.common.SetupTest.*;
import static subway.domain.station.StationApiTest.지하철역을_생성한다;

@DisplayName("지하철구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    @BeforeEach
    void setup() {
        지하철역을_생성한다("강남역");
        지하철역을_생성한다("미금역");
        지하철역을_생성한다("오리역");
        지하철역을_생성한다("판교역");
        지하철역을_생성한다("정자역");
        지하철역을_생성한다("동천역");
    }

    /**
     * When 지하철 구간을 생성하면
     * Then 지하철노선에 생성한 구간을 등록한다.
     */
    @DisplayName("지하철구간을 등록한다.")
    @Test
    void 지하철구간을_등록한다() {
        //given
        신분당선_노선을_생성한다();

        //when
        Map<String, Object> param = new HashMap<>();
        param.put("upStationId", 1);
        param.put("downStationId", 2);
        param.put("distance", 5);

        var response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(param)
                .when().post("/lines/{id}/sections", 1).then().log().all().extract();

        //then
        assertAll("지하철 구간 등록 테스트 (독립적)",
                () -> assertEquals(response.statusCode(), HttpStatus.CREATED.value(), "Fail https status code"),
                () -> assertEquals(response.body().jsonPath().getObject("sections", Sections.class).getSections().get(0).getUpStation().getId(), 1, "fail add section upStation"),
                () -> assertEquals(response.body().jsonPath().getObject("sections", Sections.class).getSections().get(0).getDownStation().getId(), 2, "fail add section downStation"));
    }

    /**
     * When 지하철 구간을 생성한다
     * Given 기존 노선의 하행 종점역이 아닌 역을 상행역으로 둔 구간을 추가한다.
     * Then "upStation's not correct" 에러 메세지와 함께 Exception이 발생한다.
     */
    @DisplayName("새로운 지하철 구간을 등록할때 상행역이 기존 구간의 하행 종점역이 아닌 경우 예외가 발생한다.")
    @Test
    void 새로운_지하철구간_추가_중_예외가_발생한다_1() {
        신분당선_노선을_생성한뒤_새로운_구간을_추가한다();

        //when
        Map<String, Object> param = new HashMap<>();
        param.put("upStationId", 4);
        param.put("downStationId", 5);
        param.put("distance", 5);

        var response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(param)
                .when().post("/lines/{id}/sections", 1)
                .then().log().all()
                .extract();
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * given 구간을 생성한다
     * when 기존 노선에 등록되어 있는 역을 새로운 구간의 하행역인 구간을 추가한다.
     * then "downStation's already exists"라는 에러 메세지와 함께 예외가 발생한다.
     */
    @DisplayName("새로운 지하철구간을 등록할때 하행역은 기존 구간에 등록된 경우 예외가 발생한다.")
    @Test
    void 새로운_지하철구간_등록_중_예외가_발생한다_2() {
        //given
        신분당선_노선을_생성한뒤_새로운_구간을_추가한다();

        //when
        Map<String, Object> param = new HashMap<>();
        param.put("upStationId", 3);
        param.put("downStationId", 1);
        param.put("distance", 4);

        var response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(param)
                .when().post("/lines/{id}/sections", 1)
                .then().log().all()
                .extract();
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * when 기존 노선의 마지막 구간을 조회한다.
     * then 해당 구간을 삭제한다.
     */
    @DisplayName("기존 노선의 마지막 구간을 삭제한다.")
    @Test
    void 기존_노선의_마지막_구간을_삭제한다() {
        //given
        신분당선_노선을_생성한뒤_새로운_구간을_추가한다();
        Section section = 지하철노선의_마지막_구간을_조회한다(1);

        //when
        var response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .param("sectionId", section.getId())
                .when().delete("/lines/{id}/sections", 1)
                .then().log().all()
                .extract();

        assertAll("지하철 구간 삭제 테스트 (독립적)",
                () -> assertEquals(response.statusCode(), HttpStatus.OK.value(), "Fail https status code"),
                () -> assertEquals(response.body().jsonPath().getObject("sections", Sections.class).getSections().contains(section), false, "fail delete section"));
    }

    /**
     * given 기존 노선에 구간을 2개 추가한다.
     * when 노선의 구간을 삭제한다.
     * then "line have only one section"라는 에러 메세지와 함께 예외가 발생한다.
     */
    @DisplayName("노선의 마지막 구간이 아닌 구간을 삭제할 경우 예외가 발생한다.")
    @Test
    void 기존_노선의_마지막_구간_삭제_중_예외가_발생한다_1() {
        //given
        신분당선_노선을_생성한뒤_새로운_구간을_추가한다();

        //when
        var response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .param("sectionId", 1)
                .when().delete("/lines/{id}/sections", 1)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * given 기존 노선에 구간을 1개 추가한다.
     * when 노선의 구간을 삭제한다.
     * then "line have only one section"라는 에러 메세지와 함께 예외가 발생한다.
     */
    @DisplayName("구간이 한개뿐인 노선의 구간을 삭제할 경우 예외가 발생한다.")
    @Test
    void 기존_노선의_마지막_구간_삭제_중_예외가_발생한다_2() {
        //given
        신분당선_노선을_생성한다();

        //when
        var response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .param("sectionId", 2)
                .when().delete("/lines/{id}/sections", 1)
                .then().log().all()
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
