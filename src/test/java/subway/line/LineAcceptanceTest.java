package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.testhelper.LineApiCaller;
import subway.testhelper.StationApiCaller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철노선 관련 기능")
@Sql({"/test-sql/table-truncate.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {

    private Map<String, String> newBunDangLineParams;
    private Map<String, String> zeroLineParams;
    private Long firstSectionId;
    private Long secondSectionId;
    private Long thirdSectionId;
    private Long forthSectionId;

    @BeforeEach
    void setUpClass() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");
        firstSectionId = StationApiCaller.callCreateStation(params).jsonPath().getObject("id", Long.class);
        params.put("name", "삼성역");
        secondSectionId = StationApiCaller.callCreateStation(params).jsonPath().getObject("id", Long.class);
        params.put("name", "선릉역");
        thirdSectionId = StationApiCaller.callCreateStation(params).jsonPath().getObject("id", Long.class);
        params.put("name", "교대역");
        forthSectionId = StationApiCaller.callCreateStation(params).jsonPath().getObject("id", Long.class);

        newBunDangLineParams = new HashMap<>();
        newBunDangLineParams.put("name", "신분당선");
        newBunDangLineParams.put("color", "bg-red-600");
        newBunDangLineParams.put("upStationId", firstSectionId.toString());
        newBunDangLineParams.put("downStationId", secondSectionId.toString());
        newBunDangLineParams.put("distance", "10");

        zeroLineParams = new HashMap<>();
        zeroLineParams.put("name", "0호선");
        zeroLineParams.put("color", "bg-red-100");
        zeroLineParams.put("upStationId", firstSectionId.toString());
        zeroLineParams.put("downStationId", thirdSectionId.toString());
        zeroLineParams.put("distance", "10");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철노선을 생성한다.")
    @Test
    void createLine() {
        // when
        LineApiCaller.callApiCreateLines(newBunDangLineParams);

        // then
        ExtractableResponse<Response> response = LineApiCaller.callApiFindLines();
        List<String> actual = response.jsonPath().getList("name", String.class);
        String expected = "신분당선";
        assertThat(actual).containsAnyOf(expected);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선들의 목록을 조회한다.")
    @Test
    void findLines() {
        // given
        LineApiCaller.callApiCreateLines(newBunDangLineParams);
        LineApiCaller.callApiCreateLines(zeroLineParams);

        // when
        ExtractableResponse<Response> response  = LineApiCaller.callApiFindLines();
        List<String> actual = response.jsonPath().getList("name", String.class);

        // then
        String[] expected = {"신분당선", "0호선"};
        assertThat(actual).containsExactly(expected);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선을 조회한다.")
    @Test
    void findLine() {
        // given
        ExtractableResponse<Response> response = LineApiCaller.callApiCreateLines(newBunDangLineParams);
        String location = response.header("location");

        // when
        response = LineApiCaller.callApiFindLine(location);

        // then
        String actual = response.jsonPath().getObject("name", String.class);
        String expected = "신분당선";
        assertThat(actual).isEqualTo(expected);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> response = LineApiCaller.callApiCreateLines(newBunDangLineParams);
        String location = response.header("location");

        // when
        LineUpdateRequest request = new LineUpdateRequest("다른분당선", "bg-red-600");
        response = LineApiCaller.callApiUpdateLine(request, location);

        // then
        response = LineApiCaller.callApiFindLine(location);
        LineResponse actual = response.jsonPath().getObject(".", LineResponse.class);
        String expectedName = "다른분당선";
        String expectedColor = "bg-red-600";
        assertThat(actual.getName()).isEqualTo(expectedName);
        assertThat(actual.getColor()).isEqualTo(expectedColor);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void deleteStation() {
        // given
        ExtractableResponse<Response> response = LineApiCaller.callApiCreateLines(newBunDangLineParams);
        String location = response.header("location");

        // when
        LineApiCaller.callApiDeleteLine(location);

        // then
        response = LineApiCaller.callApiFindLines();
        List<LineResponse> actual = response.jsonPath().getList(".", LineResponse.class);
        List<LineResponse> expected = Collections.emptyList();
        assertThat(actual).containsAll(expected);
    }

    /**
     * GIVEN 지하철 노선을 생성하고
     * WHEN 노선을 수정하면
     * THEN 수정된 노선이 조회 된다
     */
    @DisplayName("지하철노선의 구간을 수정한다.")
    @Test
    void updateSections() {
        // given
        ExtractableResponse<Response> response = LineApiCaller.callApiCreateLines(newBunDangLineParams);
        String location = response.header("location");

        // when
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", secondSectionId.toString());
        params.put("downStationId", thirdSectionId.toString());
        params.put("distance", "10");
        LineApiCaller.callApiUpdateSections(params, location);

        // then
        response = LineApiCaller.callApiFindLine(location);
        List<Long> actual = response.jsonPath().getList("stations.id", Long.class);
        Long[] expected = {firstSectionId, secondSectionId, thirdSectionId};
        assertThat(actual).containsExactly(expected);
    }

    /**
     * GIVEN 지하철 노선을 생성하고
     * WHEN 새로운 구간의 상행역이 기존의 하행역과 일치 하지 않는다면
     * THEN BadRequest(400) HTTP STATUS 가 발생한다
     */
    @DisplayName("지하철노선의 구간을 수정할때 새로운 구간의 상행역이 기존의 하행역과 일치 하지 않는다면 Bad Request가 발생한다")
    @Test
    void updateSections2() {
        // given
        ExtractableResponse<Response> response = LineApiCaller.callApiCreateLines(newBunDangLineParams);
        String location = response.header("location");

        // when
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", thirdSectionId.toString());
        params.put("downStationId", forthSectionId.toString());
        params.put("distance", "10");
        response = given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(location + "/sections")
                .then().log().all()
                .extract();

        // then
        int actual = response.statusCode();
        int expected = HttpStatus.BAD_REQUEST.value();
        assertThat(actual).isEqualTo(expected);

        String actualBody = response.asString();
        String expectedBody = "마지막 구간과 추가될 구간의 시작은 같아야 합니다.";
        assertThat(actualBody).isEqualTo(expectedBody);
    }

    /**
     * GIVEN 지하철 노선을 생성하고
     * WHEN 새로운 구간이 이미 해당 노선에 등록되어있는 역이면
     * THEN  BadRequest(400) HTTP STATUS 가 발생한다
     */
    @DisplayName("지하철노선의 구간을 수정할때 새로운 구간이 이미 해당 노선에 등록되어있는 역이면 Bad Request가 발생한다")
    @Test
    void updateSections3() {
        // given
        ExtractableResponse<Response> response = LineApiCaller.callApiCreateLines(newBunDangLineParams);
        String location = response.header("location");

        // when
        Map<String, String> params = new HashMap<>();
        params.put("upStationId", secondSectionId.toString());
        params.put("downStationId", firstSectionId.toString());
        params.put("distance", "10");
        response = given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(location + "/sections")
                .then().log().all()
                .extract();

        // then
        int actual = response.statusCode();
        int expected = HttpStatus.BAD_REQUEST.value();
        assertThat(actual).isEqualTo(expected);

        String actualBody = response.asString();
        String expectedBody = "이미 구간에 포함 되어 있는 역 입니다.";
        assertThat(actualBody).isEqualTo(expectedBody);
    }

    /**
     * GIVEN 지하철 노선을 생성하고 노선을 수정 후
     * WHEN 지하철 마지막 구간을 제거하면
     * THEN 마지막 구간이 제거된다
     */
    @DisplayName("지하철 구간을 제거한다.")
    @Test
    void deleteSections() {
        // given
        ExtractableResponse<Response> response = LineApiCaller.callApiCreateLines(newBunDangLineParams);
        String location = response.header("location");

        Map<String, String> params = new HashMap<>();
        params.put("upStationId", secondSectionId.toString());
        params.put("downStationId", thirdSectionId.toString());
        params.put("distance", "10");
        LineApiCaller.callApiUpdateSections(params, location);

        // when
        LineApiCaller.callApiDeleteSection(location, thirdSectionId.toString());

        // then
        response = LineApiCaller.callApiFindLine(location);
        List<Long> actual = response.jsonPath().getList("stations.id", Long.class);
        Long[] expected = {firstSectionId, secondSectionId};
        assertThat(actual).containsExactly(expected);
    }
}
