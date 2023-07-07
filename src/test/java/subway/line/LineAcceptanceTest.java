package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import subway.constants.Endpoint;
import subway.fixture.LineFixture;
import subway.fixture.StationFixture;
import subway.line.dto.request.SaveLineRequestDto;
import subway.line.dto.request.UpdateLineRequestDto;
import subway.station.dto.request.SaveStationRequestDto;
import subway.support.AcceptanceTest;
import subway.support.DatabaseCleanUp;
import subway.support.RestAssuredClient;

import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.UNDEFINED_PORT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 관련 기능")
@AcceptanceTest
public class LineAcceptanceTest {

    @LocalServerPort
    private int port;

    private static final String LINE_BASE_URL = Endpoint.LINE_BASE_URL.getUrl();

    private static final String LINE_ID_KEY = "id";

    private static final String LINE_NAME_KEY = "name";

    private Long gangnamStationId;

    private Long gwanggyoStationId;

    private Long cheongnyangniStationId;

    private Long chuncheonStation;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @BeforeEach
    void setUp() {
        if (RestAssured.port == UNDEFINED_PORT) {
            RestAssured.port = port;
        }
        databaseCleanUp.execute();

        this.gangnamStationId = saveStation(StationFixture.GANGNAM_STATION);
        this.gwanggyoStationId = saveStation(StationFixture.GWANGGYO_STATION);
        this.cheongnyangniStationId = saveStation(StationFixture.CHEONGNYANGNI_STATION);
        this.chuncheonStation = saveStation(StationFixture.CHUNCHEONSTATION_STATION);
    }

    /**
     * <pre>
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     * </pre>
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        SaveLineRequestDto sinbundangLine = LineFixture.SINBUNDANG_LINE;
        sinbundangLine
                .setUpStationId(gangnamStationId)
                .setDownStationId(gwanggyoStationId);
        saveLine(sinbundangLine);

        // then
        List<String> lineNames = findLinesAll()
                .jsonPath()
                .getList(LINE_NAME_KEY, String.class);

        assertAll(
                () -> assertThat(lineNames.size()).isEqualTo(1),
                () -> assertThat(lineNames).containsAnyOf(sinbundangLine.getName())
        );
    }

    /**
     * <pre>
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     * </pre>
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void readLines() {
        // given
        SaveLineRequestDto sinbundangLine = LineFixture.SINBUNDANG_LINE;
        sinbundangLine
                .setUpStationId(gangnamStationId)
                .setDownStationId(gwanggyoStationId);

        SaveLineRequestDto gyeongchunLine = LineFixture.GYEONGCHUN_LINE;
        gyeongchunLine
                .setUpStationId(cheongnyangniStationId)
                .setDownStationId(chuncheonStation);

        Stream.of(sinbundangLine, gyeongchunLine)
                .forEach(this::saveLine);

        // when
        ExtractableResponse<Response> findLinesAllResponse = findLinesAll();
        List<String> lineNames = findLinesAllResponse
                .jsonPath()
                .getList(LINE_NAME_KEY, String.class);

        // then
        assertThat(lineNames)
                .containsOnly(sinbundangLine.getName(), gyeongchunLine.getName());
    }

    /**
     * <pre>
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     * </pre>
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void readLine() {
        // given
        SaveLineRequestDto gyeongchunLine = LineFixture.GYEONGCHUN_LINE;
        gyeongchunLine
                .setUpStationId(cheongnyangniStationId)
                .setDownStationId(chuncheonStation);
        Long savedLineId = saveLine(gyeongchunLine)
                .jsonPath()
                .getLong(LINE_ID_KEY);

        // when
        String foundStationName = findLineById(savedLineId)
                .jsonPath()
                .getString(LINE_NAME_KEY);

        // then
        assertThat(foundStationName).isEqualTo(gyeongchunLine.getName());
    }

    /**
     * <pre>
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     * </pre>
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        SaveLineRequestDto sinbundangLine = LineFixture.SINBUNDANG_LINE;
        sinbundangLine
                .setUpStationId(gangnamStationId)
                .setDownStationId(gwanggyoStationId);
        Long savedLineId = saveLine(sinbundangLine)
                .jsonPath()
                .getLong(LINE_ID_KEY);

        // when
        UpdateLineRequestDto updateSinbundangLine = LineFixture.UPDATE_SINBUNDANG_LINE;
        String path = String.format("%s/%d", LINE_BASE_URL, savedLineId);
        ExtractableResponse<Response> updateStationResponse = RestAssuredClient.put(path, updateSinbundangLine);

        // then
        assertAll(
                () -> assertThat(updateStationResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> {
                    String updatedLine = findLineById(savedLineId)
                            .jsonPath()
                            .getString(LINE_NAME_KEY);

                    assertThat(updatedLine).isEqualTo(updateSinbundangLine.getName());
                }
        );
    }

    /**
     * <pre>
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     * </pre>
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void deleteLine() {
        // given
        SaveLineRequestDto gyeongchunLine = LineFixture.GYEONGCHUN_LINE;
        gyeongchunLine
                .setUpStationId(cheongnyangniStationId)
                .setDownStationId(chuncheonStation);
        Long savedLineId = saveLine(gyeongchunLine)
                .jsonPath()
                .getLong(LINE_ID_KEY);

        // when
        String path = String.format("%s/%d", LINE_BASE_URL, savedLineId);
        ExtractableResponse<Response> deleteStationByIdResponse = RestAssuredClient.delete(path);

        // then
        assertAll(
                () -> assertThat(deleteStationByIdResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> {
                    List<String> LineNames = RestAssuredClient.get(LINE_BASE_URL)
                            .jsonPath()
                            .getList(LINE_NAME_KEY, String.class);

                    assertThat(LineNames).doesNotContain(gyeongchunLine.getName());
                }
        );
    }

    /**
     * <pre>
     * 지하철역을 생성하는 API를 호출하고
     * 저장된 지하철역의 id를 반환하는 함수
     * </pre>
     *
     * @param station
     * @return saved station id
     */
    private Long saveStation(SaveStationRequestDto station) {
        return RestAssuredClient.post(Endpoint.STATION_BASE_URL.getUrl(), station)
                .jsonPath()
                .getLong("id");
    }

    /**
     * <pre>
     * 지하철 노선을 생성하는 API를 호출하는 함수
     * </pre>
     *
     * @param line
     * @return ExtractableResponse
     */
    private ExtractableResponse<Response> saveLine(SaveLineRequestDto line) {
        ExtractableResponse<Response> saveLineResponse =
                RestAssuredClient.post(LINE_BASE_URL, line);
        assertThat(saveLineResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return saveLineResponse;
    }

    /**
     * <pre>
     * 모든 지하철 노선들을 조회하는 API를 호출하는 함수
     * </pre>
     *
     * @return ExtractableResponse
     */
    private ExtractableResponse<Response> findLinesAll() {
        ExtractableResponse<Response> findStationsAllResponse = RestAssuredClient.get(LINE_BASE_URL);
        assertThat(findStationsAllResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        return findStationsAllResponse;
    }

    /**
     * <pre>
     * 지하철 노선을 id로 조회하는 API를 호출하는 함수
     * </pre>
     *
     * @param id
     * @return ExtractableResponse
     */
    private ExtractableResponse<Response> findLineById(Long id) {
        String path = String.format("%s/%d", LINE_BASE_URL, id);
        ExtractableResponse<Response> findStationByIdResponse = RestAssuredClient.get(path);
        assertThat(findStationByIdResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        return findStationByIdResponse;
    }

}
