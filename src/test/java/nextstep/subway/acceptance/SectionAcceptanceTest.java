package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("tableTruncator")
public class SectionAcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    private DatabaseTruncator databaseTruncator;

    @BeforeEach
    public void setUp() throws Exception {
        RestAssured.port = port;
        databaseTruncator.afterPropertiesSet();
        databaseTruncator.cleanTable();
    }

    private static Map<String, Object> line1;
    private static List<Map<String, Object>> sections;
    private static List<Map<String, Object>> stations;

    static RestUtil restUtil;
    static SubwayTestHelper testHelper;

    @BeforeAll
    static void init() {
        restUtil = new RestUtil();
        testHelper = new SubwayTestHelper();
        line1 = new HashMap<>();
        line1.put("name", "신분당선");
        line1.put("color", "bg-red-600");
        line1.put("upStationId", 1L);
        line1.put("downStationId", 2L);
        line1.put("distance", 10L);
        sections = testHelper.makeSectionList(List.of(2L, 3L, 10L), List.of(3L, 4L, 10L), List.of(2L, 1L, 10L));
        stations = testHelper.makeStationList("강남역", "양재역", "교대역", "신촌역");

    }

    /**
     * Given 지하철 역과 노선을 생성하고
     * When 지하철 노선에 지하철 구간을 등록하면
     * Then 지하철 노선 조회 시 등록한 지하철 구간을 확인할 수 있다
     */
    @DisplayName("지하철 구간을 등록한다")
    @Test
    void registerSection() {
        //Given
        testHelper.makeGivenCondition("/stations", HttpStatus.CREATED.value(), stations.get(0), stations.get(1), stations.get(2), stations.get(3));
        Long id = restUtil.createEntityData("/lines", HttpStatus.CREATED.value(), line1);

        //When
        testHelper.makeGivenCondition("/lines/" + id + "/sections", HttpStatus.OK.value(), sections.get(0), sections.get(1));

        //Then
        ExtractableResponse<Response> lineResponse = restUtil.getResponseDataById("/lines/{id}", id);
        assertThat(lineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());


    }

    /**
     * Given 지하철역과 노선을 생성하고
     * When 생성한 지하철 노선의 하행과 등록할 구간의 상행이 맞지 않으면
     * Then 구간을 등록할 수 없어 에러가 출력된다
     */
    @DisplayName("등록된 노선의 하행과 새로 등록할 구간의 상행이 맞지 않으면 등록할 수 없다.")
    @Test
    void matchStation() {
        //Given
        testHelper.makeGivenCondition("/stations", HttpStatus.CREATED.value(), stations.get(0), stations.get(1), stations.get(2), stations.get(3));
        Long id = restUtil.createEntityData("/lines", HttpStatus.CREATED.value(), line1);

        //When
        Long result = restUtil.createEntityData("/lines/" + id + "/sections", HttpStatus.INTERNAL_SERVER_ERROR.value(), sections.get(1));

        //Then
        assertThat(result).isEqualTo(-1L);
    }

    /**
     * Given 지하철역과 노선을 생성하고
     * When 등록하려는 구간의 하행이 이미 지하철 노선에 등록되어 있다면
     * Then 구간을 등록할 수 없어 에러가 출력된다
     */
    @DisplayName("등록할 구간의 하행은 이미 등록되어 있으면 안된다")
    @Test
    void checkDuplicateStation() {
        //Given
        testHelper.makeGivenCondition("/stations", HttpStatus.CREATED.value(), stations.get(0), stations.get(1), stations.get(2));
        Long id = restUtil.createEntityData("/lines", HttpStatus.CREATED.value(), line1);

        //When
        Long result = restUtil.createEntityData("/lines/" + id + "/sections", HttpStatus.INTERNAL_SERVER_ERROR.value(), sections.get(2));

        //Then
        assertThat(result).isEqualTo(-1L);
    }

    /**
     * Given 지하철 노선에 지하철 구간을 등록하고
     * when 등록한 지하설 구간을 삭제하면
     * Then 해당 지하철 구간은 삭제된다
     */
    @DisplayName("지하철 구간을 삭제한다")
    @Test
    void deleteSection() {
        //Given
        testHelper.makeGivenCondition("/stations", HttpStatus.CREATED.value(), stations.get(0), stations.get(1), stations.get(2));
        Long id = restUtil.createEntityData("/lines", HttpStatus.CREATED.value(), line1);
        restUtil.registEntityData("/lines/" + id + "/sections", sections.get(0));

        //When
        ExtractableResponse<Response> deleteResponse =
                restUtil.deleteEntityDataByIdWithQueryParam("/lines/{id}/sections"
                , id, "stationId", (Long) sections.get(0).get("upStationId"));
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        //Then
        ExtractableResponse<Response> lineResponse = restUtil.getResponseDataById("/lines/{id}", id);
        assertThat(lineResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

    }

    /**
     * Given 지하철역과 노선을 생성하고
     * when 한개의 구간만 지닌 지하철 노선의 구간을 삭제하려고 하면
     * Then 해당 지하철 구간은 삭제할 수 없어 에러를 출력한다
     */
    @DisplayName("한개의 구간만 지닌 노선의 구간을 삭제할 수 없다.")
    @Test
    void canNotDeleteOnlyOneSection() {
        //Given
        testHelper.makeGivenCondition("/stations", HttpStatus.CREATED.value(), stations.get(0), stations.get(1));
        Long id = restUtil.createEntityData("/lines", HttpStatus.CREATED.value(), line1);

        //When
        ExtractableResponse<Response> deleteResponse =
                restUtil.deleteEntityDataByIdWithQueryParam("/lines/{id}/sections"
                        , id, "stationId", (Long) line1.get("upStationId"));

        //Then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());


    }

    /**
     * Given 지하철역과 노선, 구간을 생성하고
     * when  마지막 구간이 아닌 구간을 삭제하려고 하면
     * Then 해당 지하철 구간은 삭제할 수 없어 에러를 출력한다
     */
    @DisplayName("마지막 구간이 아닌 것은 제거 할 수 없다.")
    @Test
    void canNotDeleteNotLastSection() {
        //Given
        testHelper.makeGivenCondition("/stations", HttpStatus.CREATED.value(), stations.get(0), stations.get(1), stations.get(2), stations.get(3));
        Long id = restUtil.createEntityData("/lines", HttpStatus.CREATED.value(), line1);
        testHelper.makeGivenCondition("/lines/" + id + "/sections", HttpStatus.OK.value(), sections.get(0), sections.get(1));

        //When
        ExtractableResponse<Response> deleteResponse =
                restUtil.deleteEntityDataByIdWithQueryParam("/lines/{id}/sections"
                        , id, "stationId", (Long) sections.get(0).get("upStationId"));


        //Then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

    }

}

