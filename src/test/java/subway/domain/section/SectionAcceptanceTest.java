package subway.domain.section;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static subway.common.SetupTest.분당선_노선을_생성한다;
import static subway.common.SetupTest.신분당선_노선을_생성한다;

@DisplayName("지하철구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@Sql("/setup-station.sql")
public class SectionAcceptanceTest {

    /**
     * When 지하철 구간을 생성하면
     * Then 지하철노선에 생성한 구간을 등록한다.
     */
    @DisplayName("지하철구간을 등록한다.")
    @Test
    void 지하철구간을_등록한다(){
        //given
        신분당선_노선을_생성한다();

        //when
        Map<String, Object> param = new HashMap<>();
        param.put("upStationId", 2);
        param.put("downStationId", 3);
        param.put("distance", 5);

        var response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(param)
                .when().post("/lines/{id}/section", 1)
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.jsonPath().getInt("sections[1].upStationId")).isEqualTo(2);
        assertThat(response.jsonPath().getInt("sections[1].downStationId")).isEqualTo(3);
    }

    /**
     * When 지하철 구간을 생성한다
     * Given 기존 노선의 하행 종점역이 아닌 역을 상행역으로 둔 구간을 추가한다.
     * Then "해당 역을 상행역으로 둔 구간을 생성할 수 없습니다." 에러 메세지와 함께 Exception이 발생한다.
     */
    @DisplayName("새로운 지하철 구간을 등록할때 상행역이 기존 구간의 하행 종점역이 아닌 경우 예외가 발생한다.")
    @Test
    void 새로운_지하철구간_추가_중_예외가_발생한다_1(){
        신분당선_노선을_생성한다();

        //when
        Map<String, Object> param = new HashMap<>();
        param.put("upStationId", 3);
        param.put("downStationId", 4);
        param.put("distance", 5);

        var response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(param)
                .when().post("/lines/{id}/section", 1)
                .then().log().all()
                .extract();
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("새로운 지하철구간을 등록할때 하행역은 기존 구간에 등록된 경우 예외가 발생한다.")
    @Test
    void 새로운_지하철구간_등록_중_예외가_발생한다_2(){
        신분당선_노선을_생성한다();

        //when
        Map<String, Object> param = new HashMap<>();
        param.put("upStationId", 2);
        param.put("downStationId", 1);
        param.put("distance", 4);

        var response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(param)
                .when().post("/lines/{id}/section", 1)
                .then().log().all()
                .extract();
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
