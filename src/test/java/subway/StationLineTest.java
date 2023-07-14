package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.dto.LineResponse;
import subway.dto.StationResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static subway.common.LineFixture.createLine;
import static subway.common.LineFixture.getLines;
import static subway.common.StationFixture.createStationAndGetInfo;

@DisplayName("노선 관련 기능")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationLineTest {

    private final String COLOR_RED = "bg-red-600";

    private final String COLOR_BLUE = "bg-blue-600";
    private final int DISTANCE = 10;

    private StationResponse 신사역, 광교역, 소요산역, 광명역;
    @BeforeEach
    void set(){
        신사역 = createStationAndGetInfo("신사역");
        광교역 = createStationAndGetInfo("광교역");
        소요산역 = createStationAndGetInfo("소요산역");
        광명역 = createStationAndGetInfo("광명역");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     * */
    @DisplayName("노선 생성")
    @Test
    void createLineTest() {
        //given
        //when
        ExtractableResponse<Response> response =
                createLine("신분당선", COLOR_RED, 신사역.getId(), 광교역.getId(), DISTANCE);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        LineResponse line = getLines().jsonPath().getList("",LineResponse.class).get(0);

        assertThat(line.getName()).isEqualTo("신분당선");
        assertThat(line.getColor()).isEqualTo(COLOR_RED);
        assertThat(line.getUpStationId()).isEqualTo(신사역.getId());
        assertThat(line.getDownStationId()).isEqualTo(광교역.getId());
    }



    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     * */
    @DisplayName("노선 목록 조회")
    @Test
    void showLines() {
        //given
        createLine("신분당선", COLOR_RED, 신사역.getId(), 광교역.getId(), DISTANCE);
        createLine("1호선", COLOR_BLUE,  소요산역.getId(), 광명역.getId(), DISTANCE);

        //when
        ExtractableResponse<Response> response = getLines();
        List<String> lines = response.jsonPath().getList("name", String.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lines.size()).isEqualTo(2);
        assertThat(lines).contains("신분당선","1호선");

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     * */
    @DisplayName("노선 정보 조회")
    @Test
    void showLineInfo() {
        //given
        createLine("신분당선",COLOR_RED, 신사역.getId(), 광교역.getId() , DISTANCE);

        //when
        ExtractableResponse<Response> response = getLines();
        LineResponse line = response.jsonPath().getList("", LineResponse.class).get(0);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(line.getName()).isEqualTo("신분당선");
        assertThat(line.getUpStationId()).isEqualTo(신사역.getId());
        assertThat(line.getDownStationId()).isEqualTo(광교역.getId());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     * */
    @DisplayName("노선 정보 수정")
    @Test
    void updateLineInfo() {
        //given
        Long 신분당선 = createLine("신분당선", COLOR_RED, 신사역.getId(), 광교역.getId(),DISTANCE)
                            .jsonPath().getObject("", LineResponse.class).getId();

        //when
        Map<String, Object> param = new HashMap<>();
        param.put("id",신분당선);
        param.put("name","1호선");
        param.put("color",COLOR_BLUE);

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("/lines")
                .then().log().all()
                .extract();

        LineResponse line = response.jsonPath().getObject("", LineResponse.class);


        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(line.getName()).isEqualTo("1호선");
        assertThat(line.getColor()).isEqualTo(COLOR_BLUE);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     * */
    @DisplayName("노선 삭제")
    @Test
    void deleteLine() {

        //given
        Long 신분당선 = createLine("신분당선", COLOR_RED
                    , 신사역.getId()
                    , 광교역.getId()
                    ,10)
                        .jsonPath().getObject("", LineResponse.class).getId();

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/lines/" + 신분당선)
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());


        List<String> names = getLines().jsonPath().getList("name", String.class);

        assertThat(names).isEmpty();
    }

}
