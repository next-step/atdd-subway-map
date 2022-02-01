package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.acceptance.LineSteps.*;
import static nextstep.subway.acceptance.LineSteps.지하철구간_생성;
import static nextstep.subway.acceptance.StationSteps.지하철역_생성_및_아이디추출;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    public static final String 신분당선 = "신분당선";
    public static final String COLOR = "bg-red-600";
    public static final String 강남역 = "강남역";
    public static final String 광교역 = "양재역";
    public static final int DISTANCE = 10;
    private LineRequest lineRequest;

    private LineRequest 지하철노선_생성_요청_파라미터() {
        return 지하철노선_생성_요청_파라미터(신분당선, COLOR, 강남역, 광교역, DISTANCE);
    }

    private LineRequest 지하철노선_생성_요청_파라미터(String name, String color, String upStation, String downStation, int distance) {
        long upStationId = 지하철역_생성_및_아이디추출(upStation);
        long downStationId = 지하철역_생성_및_아이디추출(downStation);
        return new LineRequest(name, color, upStationId, downStationId, distance);
    }

    @BeforeEach
    public void init() {
        lineRequest = 지하철노선_생성_요청_파라미터();
    }

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> createResponse = 지하철노선_생성(lineRequest);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }


    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        String 경춘선 = "경춘선";
        String 초록색 = "bg-green-600";
        String 상봉역 = "상봉역";
        String 춘천역 = "춘천역";
        int distance = 10;

        지하철노선_생성(lineRequest);

        // given
        LineRequest lineRequest_second = 지하철노선_생성_요청_파라미터(경춘선, 초록색, 상봉역, 춘천역, distance);
        지하철노선_생성(lineRequest_second);

        // when
        Map<String, String> params = new HashMap<>();
        ExtractableResponse<Response> readResponse = 지하철노선_목록_조회(params);

        // then
        assertThat(readResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> lineNames = readResponse.jsonPath().getList("name");
        List<String> lineColors = readResponse.jsonPath().getList("color");

        assertThat(lineNames).contains(신분당선, 경춘선);
        assertThat(lineColors).contains(COLOR, 초록색);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        // when
        long lineId = 지하철노선_생성_및_아이디추출(lineRequest);

        // when
        ExtractableResponse<Response> readResponse = 지하철노선_조회(lineId);

        // then
        assertThat(readResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        String name = readResponse.jsonPath().getString("name");
        String color = readResponse.jsonPath().getString("color");
        List<StationResponse> stations = readResponse.jsonPath().getList("stations", StationResponse.class);

        assertThat(name).isEqualTo(신분당선);
        assertThat(color).isEqualTo(COLOR);
        assertThat(stations).hasSize(2);

    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        long lineId = 지하철노선_생성_및_아이디추출(lineRequest);

        // when
        String _1호정_수정 = "1호선-수정";
        String 파란색_수정 = "bg-blue-600-수정";
        Map<String, String> params = new HashMap<>();
        params.put("name", _1호정_수정);
        params.put("color", 파란색_수정);

        ExtractableResponse<Response> modifyResponse = 지하철노선_수정(lineId, params);

        // then
        assertThat(modifyResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        String name = modifyResponse.jsonPath().getString("name");
        String color = modifyResponse.jsonPath().getString("color");
        assertThat(name).isEqualTo(_1호정_수정);
        assertThat(color).isEqualTo(파란색_수정);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        long lineId = 지하철노선_생성_및_아이디추출(lineRequest);

        // when
        ExtractableResponse<Response> deleteResponse = 지하철노선_삭제(lineId);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름의 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 요청이 실패한다.
     */
    @DisplayName("지하철 노선 등록 실패 - 이름 중복")
    @Test
    void createLineWithDuplicatedException() {
        // given
        지하철노선_생성(lineRequest);

        // when
        ExtractableResponse<Response> createResponse = 지하철노선_생성(lineRequest);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * Given 지하철역을 생성하고
     * When 지하철 구간 생성을 요청 하면
     * Then 지하철 구간 생성이 성공한다.
     */
    @DisplayName("지하철 구간 등록")
    @Test
    void createStation() {
        // given
        LineResponse line = 지하철노선_생성_및_객체추출(lineRequest);
        long downStationId = 지하철역_생성_및_아이디추출("판교역");

        int size = line.getStations().size();
        long upStationId = line.getStations().get(size - 1).getId();
        SectionRequest sectionRequest = new SectionRequest(downStationId, upStationId, 10);
        // when
        ExtractableResponse<Response> createResponse = 지하철구간_생성(line.getId(), sectionRequest);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createResponse.header("Location")).isNotBlank();
    }

    /**
     * Given 지하철 노선을 생성하고
     * Given 지하철역을 생성하고
     * Given 지하철 구간 생성을 요청 하면
     * When 지하철 구간 제거를 요청 하면
     * Then 지하철 구간 제거가 성공한다.
     */
    @DisplayName("지하철 구간 제거")
    @Test
    void getStations() {
        // given
        LineResponse line = 지하철노선_생성_및_객체추출(lineRequest);
        long downStationId = 지하철역_생성_및_아이디추출("판교역");

        int size = line.getStations().size();
        long upStationId = line.getStations().get(size - 1).getId();
        SectionRequest sectionRequest = new SectionRequest(downStationId, upStationId, 10);
        지하철구간_생성(line.getId(), sectionRequest);

        // when
        ExtractableResponse<Response> deleteResponse = 지하철구간_삭제(line.getId(), downStationId);

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


}
