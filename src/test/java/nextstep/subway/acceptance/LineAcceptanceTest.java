package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.client.LineClient;
import nextstep.subway.acceptance.client.StationClient;
import nextstep.subway.acceptance.client.dto.LineCreationRequest;
import nextstep.subway.acceptance.util.GivenUtils;
import nextstep.subway.acceptance.util.HttpStatusValidator;
import nextstep.subway.acceptance.util.JsonResponseConverter;
import nextstep.subway.exception.SectionRegistrationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static nextstep.subway.acceptance.util.GivenUtils.GREEN;
import static nextstep.subway.acceptance.util.GivenUtils.TEN;
import static nextstep.subway.acceptance.util.GivenUtils.YELLOW;
import static nextstep.subway.acceptance.util.GivenUtils.분당선_이름;
import static nextstep.subway.acceptance.util.GivenUtils.신분당선_이름;
import static nextstep.subway.acceptance.util.GivenUtils.이호선_이름;
import static nextstep.subway.acceptance.util.GivenUtils.이호선역_이름들;
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
    
    @Autowired
    GivenUtils givenUtils;

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
        ExtractableResponse<Response> response = lineClient.createLine(givenUtils.이호선_생성_요청());

        // then
        statusValidator.validateCreated(response);
        assertThat(responseConverter.convertToNames(lineClient.fetchLines()))
                .contains(이호선_이름);
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
        ExtractableResponse<Response> firstCreationResponse = lineClient.createLine(givenUtils.이호선_생성_요청());

        // then
        statusValidator.validateCreated(firstCreationResponse);

        // when
        ExtractableResponse<Response> response = lineClient.createLine(givenUtils.이호선_생성_요청());

        // then
        statusValidator.validateBadRequest(response);
        assertThat(responseConverter.convertToError(response))
                .contains(SectionRegistrationException.class.getName());
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
                이호선_이름,
                GREEN,
                upStationId,
                downStationId,
                TEN
        );

        // when
        ExtractableResponse<Response> response = lineClient.createLine(lineRequest);

        // then
        statusValidator.validateBadRequest(response);
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
        lineClient.createLine(givenUtils.이호선_생성_요청());
        lineClient.createLine(givenUtils.신분당선_생성_요청());

        // when
        ExtractableResponse<Response> linesResponse = lineClient.fetchLines();

        // then
        statusValidator.validateOk(linesResponse);
        assertThat(responseConverter.convertToList(linesResponse, "stations"))
                .allMatch(fetchStations -> fetchStations.size() == stationSize);
        assertThat(responseConverter.convertToNames(linesResponse))
                .hasSize(lineSize)
                .containsExactly(이호선_이름, 신분당선_이름);
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
        long lineId = responseConverter.convertToId(lineClient.createLine(givenUtils.이호선_생성_요청()));

        // when
        ExtractableResponse<Response> response = lineClient.fetchLine(lineId);

        // then
        statusValidator.validateOk(response);
        assertThat(responseConverter.convert(response, "stations", List.class))
                .hasSize(이호선역_이름들.size());
        assertThat(responseConverter.convertToName(response)).isEqualTo(이호선_이름);
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
        long lineId = responseConverter.convertToId(lineClient.createLine(givenUtils.이호선_생성_요청()));

        // when
        ExtractableResponse<Response> response = lineClient.modifyLine(lineId, 분당선_이름, YELLOW);

        // then
        statusValidator.validateOk(response);
        assertThat(responseConverter.convertToNames(lineClient.fetchLines()))
                .containsExactly(분당선_이름);
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
        long notExistsLineId = 1L;

        // when
        ExtractableResponse<Response> response =
                lineClient.modifyLine(notExistsLineId, 분당선_이름, YELLOW);

        // then
        statusValidator.validateBadRequest(response);
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
        long lineId = responseConverter.convertToId(lineClient.createLine(givenUtils.이호선_생성_요청()));

        // when
        ExtractableResponse<Response> response = lineClient.deleteLine(lineId);

        // then
        statusValidator.validateNoContent(response);
        assertThat(responseConverter.convertToNames(lineClient.fetchLines()))
                .doesNotContain(이호선_이름);
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
        long notExistsLineId = 1L;

        // when
        ExtractableResponse<Response> response = lineClient.deleteLine(notExistsLineId);

        // then
        statusValidator.validateBadRequest(response);
        assertThat(responseConverter.convertToError(response))
                .contains(EmptyResultDataAccessException.class.getName());
    }

}