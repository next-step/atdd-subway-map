package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.client.LineClient;
import nextstep.subway.acceptance.client.StationClient;
import nextstep.subway.acceptance.client.dto.LineCreationRequest;
import nextstep.subway.acceptance.util.HttpStatusValidator;
import nextstep.subway.acceptance.util.JsonResponseConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static nextstep.subway.acceptance.fixture.ColorFixtures.GREEN;
import static nextstep.subway.acceptance.fixture.ColorFixtures.RED;
import static nextstep.subway.acceptance.fixture.ColorFixtures.YELLOW;
import static nextstep.subway.acceptance.fixture.DistanceFixtures.FIVE;
import static nextstep.subway.acceptance.fixture.DistanceFixtures.TEN;
import static nextstep.subway.acceptance.fixture.LineFixtures.분당선;
import static nextstep.subway.acceptance.fixture.LineFixtures.신분당선;
import static nextstep.subway.acceptance.fixture.LineFixtures.이호선;
import static nextstep.subway.acceptance.fixture.StationFixtures.신분당선역_이름들;
import static nextstep.subway.acceptance.fixture.StationFixtures.이호선역_이름들;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @Autowired
    StationClient stationClient;

    @Autowired
    LineClient lineClient;

    @Autowired
    HttpStatusValidator statusValidator;

    @Autowired
    JsonResponseConverter responseConverter;

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    @DirtiesContext
    void createLine() {
        // given

        // when
        statusValidator.validateCreated(lineClient.createLine(이호선_생성_요청()));

        // then
        assertThat(responseConverter.convertToNames(lineClient.fetchLines()))
                .contains(이호선.getValue());
    }

    /**
     * When 동일한 지하철 노선을 중복해서 생성하면
     * Then 기존에 저장된 노선을 반환한다
     */
    @DisplayName("동일한 지하철 노선을 중복 생성한다.")
    @Test
    @DirtiesContext
    void createLineTwice() {
        // given

        // when
        statusValidator.validateCreated(lineClient.createLine(이호선_생성_요청()));
        ExtractableResponse<Response> duplicatedResponse =
                statusValidator.validateCreated(lineClient.createLine(이호선_생성_요청()));

        // then
        assertThat(responseConverter.convert(duplicatedResponse, "stations", List.class))
                .hasSize(이호선역_이름들.size());
        assertThat(responseConverter.convertToName(duplicatedResponse)).isEqualTo(이호선.getValue());
    }

    /**
     * When 존재하지 않는 역의 id로 노선을 생성하면
     * Then 오류(EntityNotFoundException) 객체를 반환한다
     */
    @DisplayName("존재하지 않는 역의 id로 지하철 노선을 생성한다.")
    @Test
    @DirtiesContext
    void createLineByInvalidStationIds() {
        // given
        long upStationId = 1L;
        long downStationId = 2L;
        LineCreationRequest lineRequest = new LineCreationRequest(
                이호선.getValue(),
                GREEN.getValue(),
                upStationId,
                downStationId,
                TEN.getValue()
        );

        // when
        ExtractableResponse<Response> response =
                statusValidator.validateBadRequest(lineClient.createLine(lineRequest));

        // then
        assertThat(responseConverter.convertToError(response))
                .contains(EntityNotFoundException.class.getName());
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    @DirtiesContext
    void getLines() {
        // given
        int stationSize = 2;
        int lineSize = 2;
        lineClient.createLine(이호선_생성_요청());
        lineClient.createLine(신분당선_생성_요청());

        // when
        ExtractableResponse<Response> linesResponse =
                statusValidator.validateOk(lineClient.fetchLines());

        // then
        assertThat(responseConverter.convertToList(linesResponse, "stations"))
                .allMatch(fetchStations -> fetchStations.size() == stationSize);
        assertThat(responseConverter.convertToNames(linesResponse))
                .hasSize(lineSize)
                .containsExactly(이호선.getValue(), 신분당선.getValue());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    @DirtiesContext
    void getLine() {
        // given
        long lineId = responseConverter.convertToId(lineClient.createLine(이호선_생성_요청()));

        // when
        ExtractableResponse<Response> response =
                statusValidator.validateOk(lineClient.fetchLine(lineId));

        // then
        assertThat(responseConverter.convert(response, "stations", List.class))
                .hasSize(이호선역_이름들.size());
        assertThat(responseConverter.convertToName(response)).isEqualTo(이호선.getValue());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    @DirtiesContext
    void modifyLine() {
        // given
        long lineId = responseConverter.convertToId(lineClient.createLine(이호선_생성_요청()));

        // when
        statusValidator.validateOk(lineClient.modifyLine(lineId, 분당선.getValue(), YELLOW.getValue()));

        // then
        assertThat(responseConverter.convertToNames(lineClient.fetchLines()))
                .containsExactly(분당선.getValue());
    }

    /**
     * When 존재하지 않는 지하철 노선을 수정하면
     * Then 오류(EntityNotFoundException) 객체를 반환한다
     */
    @DisplayName("존재하지 않는 지하철 노선을 수정한다.")
    @Test
    @DirtiesContext
    void modifyNoneExistentLine() {
        // given
        long lineId = 1L;

        // when
        ExtractableResponse<Response> response =
                statusValidator.validateBadRequest(lineClient.modifyLine(lineId, 분당선.getValue(), YELLOW.getValue()));

        // then
        assertThat(responseConverter.convertToError(response))
                .contains(EntityNotFoundException.class.getName());
    }


    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 제거한다.")
    @Test
    @DirtiesContext
    void deleteLine() {
        // given
        long lineId = responseConverter.convertToId(lineClient.createLine(이호선_생성_요청()));

        // when
        statusValidator.validateNoContent(lineClient.deleteLine(lineId));

        // then
        assertThat(responseConverter.convertToNames(lineClient.fetchLines()))
                .doesNotContain(이호선.getValue());
    }

    /**
     * When 존재하지 않는 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("존재하지 않는 지하철 노선을 제거한다.")
    @Test
    @DirtiesContext
    void deleteNonExistentLine() {
        // given
        long lineId = 1L;

        // when
        ExtractableResponse<Response> response = statusValidator.validateBadRequest(lineClient.deleteLine(lineId));

        // then
        assertThat(responseConverter.convertToError(response))
                .contains(EmptyResultDataAccessException.class.getName());
    }

    private LineCreationRequest 이호선_생성_요청() {
        List<Long> stationIds = responseConverter.convertToIds(stationClient.createStations(이호선역_이름들));
        return new LineCreationRequest(
                이호선.getValue(),
                GREEN.getValue(),
                stationIds.get(0),
                stationIds.get(1),
                TEN.getValue()
        );
    }

    private LineCreationRequest 신분당선_생성_요청() {
        List<Long> stationIds = responseConverter.convertToIds(stationClient.createStations(신분당선역_이름들));
        return new LineCreationRequest(
                신분당선.getValue(),
                RED.getValue(),
                stationIds.get(0),
                stationIds.get(1),
                FIVE.getValue()
        );
    }

}