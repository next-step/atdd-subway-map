package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void beforeEach() {
        RestAssured.port = port;
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성하고 노선 목록을 조회한다")
    @Test
    void createLine() {

        //when
        StationRestAssuredCRUD.createStation("강남역");
        StationRestAssuredCRUD.createStation("양재역");

        ExtractableResponse<Response> createResponse = LineRestAssuredCRUD.createStation("신분당선", "bg-red-600", 1L, 2L, 10);
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> lineNames = LineRestAssuredCRUD.showStationList().jsonPath().getList("name", String.class);
        assertThat(lineNames).contains("신분당선");

    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("2개의 지하철 노선을 생성하고 노선 목록을 조회한다")
    @Test
    void createAndShowTwoLineList() {

        //given
        StationRestAssuredCRUD.createStation("강남역");
        StationRestAssuredCRUD.createStation("양재역");
        StationRestAssuredCRUD.createStation("서현역");

        LineRestAssuredCRUD.createStation("신분당선", "bg-red-600", 1L, 2L, 10);
        LineRestAssuredCRUD.createStation("수인분당선", "bg-yellow-600", 1L, 3L, 10);

        //when
        List<String> names = LineRestAssuredCRUD.showStationList().jsonPath().getList("name", String.class);

        // then
        assertThat(names).containsAll(List.of("신분당선", "수인분당선"));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 생성하고 노선을 조회한다")
    @Test
    void createAndShowLine() {

        //given
        StationRestAssuredCRUD.createStation("강남역");
        StationRestAssuredCRUD.createStation("양재역");

        ExtractableResponse<Response> createResponse = LineRestAssuredCRUD.createStation("신분당선", "bg-red-600", 1L, 2L, 10);

        Long createdId = createResponse.body().jsonPath().getLong("id");

        //when
        String name = LineRestAssuredCRUD.showStation(createdId).jsonPath().getString("name");

        // then
        assertThat(name).isEqualTo("신분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다")
    @Test
    void createAndModifyLine() {

        //given
        StationRestAssuredCRUD.createStation("강남역");
        StationRestAssuredCRUD.createStation("양재역");

        ExtractableResponse<Response> createResponse = LineRestAssuredCRUD.createStation("신분당선", "bg-red-600", 1L, 2L, 10);

        Long createdId = createResponse.body().jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> modifyResponse = LineRestAssuredCRUD.modifyStation(createdId, "수인분당선", "bg-yellow-600");

        // then
        assertThat(modifyResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        String name = LineRestAssuredCRUD.showStation(createdId).jsonPath().getString("name");
        assertThat(name).isEqualTo("수인분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 생성하고 삭제한다")
    @Test
    void createAndDeleteLine() {

        //given
        StationRestAssuredCRUD.createStation("강남역");
        StationRestAssuredCRUD.createStation("양재역");


        ExtractableResponse<Response> createResponse = LineRestAssuredCRUD.createStation("신분당선", "bg-red-600", 1L, 2L, 10);

        Long createdId = createResponse.body().jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> deleteResponse = LineRestAssuredCRUD.deleteStation(createdId);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        List<String> names = LineRestAssuredCRUD.showStationList().jsonPath().getList("name", String.class);
        assertThat(names).doesNotContain("신분당선");
    }
}
