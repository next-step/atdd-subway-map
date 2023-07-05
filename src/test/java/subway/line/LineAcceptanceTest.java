package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.utils.TestUtils.주어진_이름으로_지하철역을_생성한다;


@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private StationRepository stationRepository;

    @LocalServerPort
    private int serverPort;

    @BeforeEach
    void setUp() {
        RestAssured.port = serverPort;
    }


    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Test
    void 지하철_노선을_생성한다() {
        //given
        Long gangnamStationId = 주어진_이름으로_지하철역을_생성한다("강남역");
        Long pangyoStationId = 주어진_이름으로_지하철역을_생성한다("판교역");

        LineCreateRequest lineCreateRequest
                = new LineCreateRequest("신분당선", "bg-red-600", gangnamStationId, pangyoStationId, 10);

        //when
        Line line = 지하철_노선을_등록_api를_호출한다(lineCreateRequest);

        //then
        List<Line> lines = lineRepository.findAll();
        assertThat(lines.get(0).getId()).isEqualTo(line.getId());
    }

    Line 지하철_노선을_등록한다(LineCreateRequest lineCreateRequest) {
        //given
        Station downStation = 주어진_아이디로_지하철역을_조회한다(lineCreateRequest.getDownStationId());
        Station upStation = 주어진_아이디로_지하철역을_조회한다(lineCreateRequest.getUpStationId());

        Line line = Line.builder()
                .color(lineCreateRequest.getColor())
                .name(lineCreateRequest.getName())
                .stations(Arrays.asList(downStation, upStation))
                .distance(lineCreateRequest.getDistance())
                .build();

        return lineRepository.save(line);
    }

    Station 주어진_아이디로_지하철역을_조회한다(Long id) {
        return stationRepository.findById(id).get();
    }

    Line 지하철_노선을_등록_api를_호출한다(LineCreateRequest lineCreateRequest) {
        Line response = RestAssured.given().log().all()
                .body(lineCreateRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract()
                .as(Line.class);

        return response;
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @Test
    void 지하철_노선_목록을_조회한다() {
        //given
        /* 첫번째 노선 등록 */
        신분당선_노선_테스트_데이터_생성();

        /* 두번째 노선 등록 */
        구호선_노선_테스트_데이터_생성();

        //when
        List<Line> lines = 전체_지하철_노선_목록_조회_api를_호출한다();

        //then
        assertThat(lines.size()).isEqualTo(2);
    }

    Line 신분당선_노선_테스트_데이터_생성() {
        Long gangnamStationId = 주어진_이름으로_지하철역을_생성한다("강남역");
        Long pangyoStationId = 주어진_이름으로_지하철역을_생성한다("판교역");

        LineCreateRequest sinboonLineCreateRequest
                = new LineCreateRequest("신분당선", "bg-red-600", gangnamStationId, pangyoStationId, 10);

        return 지하철_노선을_등록한다(sinboonLineCreateRequest);
    }

    Line 구호선_노선_테스트_데이터_생성() {
        Long sinnonhyeonStationId = 주어진_이름으로_지하철역을_생성한다("신논현역");
        Long sapyongStationId = 주어진_이름으로_지하철역을_생성한다("사평역");

        LineCreateRequest lineNineCreateRequest
                = new LineCreateRequest("9호선", "bg-yellow-400", sinnonhyeonStationId, sapyongStationId, 10);

        return 지하철_노선을_등록한다(lineNineCreateRequest);
    }

    List<Line> 전체_지하철_노선_목록을_조회() {
        return lineRepository.findAll();
    }

    List<Line> 전체_지하철_노선_목록_조회_api를_호출한다() {
        List<Line> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract()
                .as(List.class);

        return response;
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    void 지하철_노선_조회한다() {
        //given
        Line line = 신분당선_노선_테스트_데이터_생성();

        //when
        Line result = 지하철_노선_아이디를_바탕으로_조회하는_api를_호출한다(line.getId());

        //then
        assertThat(result.getName()).isEqualTo("신분당선");
    }

    Line 지하철_노선_아이디를_바탕으로_조회한다(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    Line 지하철_노선_아이디를_바탕으로_조회하는_api를_호출한다(Long id) {
        Line response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/{id}", id)
                .then().log().all()
                .extract()
                .as(Line.class);

        return response;
    }


    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @Test
    void 지하철_노선을_수정한다() {
        //given
        Line line = 신분당선_노선_테스트_데이터_생성();

        //when
        LineChangeRequest lineChangeRequest = new LineChangeRequest("98호선", "super_red");
        지하철_노선_데이터_수정_api를_호출한다(line.getId(), lineChangeRequest);

        //then
        Line result = 지하철_노선_아이디를_바탕으로_조회한다(line.getId());
        assertThat(result.getName()).isEqualTo("98호선");
        assertThat(result.getColor()).isEqualTo("super_red");
    }

    void 지하철_노선_데이터를_수정한다(Long id, LineChangeRequest lineChangeRequest) {
        Line line = 지하철_노선_아이디를_바탕으로_조회한다(id);

        if (lineChangeRequest.getColor() != null) {
            line.updateColor(lineChangeRequest.getColor());
        }
        if (lineChangeRequest.getName() != null) {
            line.updateName(lineChangeRequest.getName());
        }
        lineRepository.save(line);
    }

    void 지하철_노선_데이터_수정_api를_호출한다(Long id, LineChangeRequest lineChangeRequest) {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(lineChangeRequest)
                .when().put("/lines/{id}", id)
                .then().log().all();
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @Test
    void 지하철_노선을_삭제한다() {
        //given
        Line line = 신분당선_노선_테스트_데이터_생성();

        //when
        아이디_기반으로_지하철_노선_데이터를_삭제한다(line.getId());

        //then
        assertThatThrownBy(() -> {
            지하철_노선_아이디를_바탕으로_조회한다(line.getId());
        }).isInstanceOf(NoSuchElementException.class);
    }

    void 아이디_기반으로_지하철_노선_데이터를_삭제한다(Long id) {
        lineRepository.deleteById(id);
    }
}
