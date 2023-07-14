package subway.acceptancetest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static subway.acceptancetest.AcceptanceTestHelper.*;

@DisplayName("지하철구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    private int stationIdsSize = 4;
    private List<Long> stationIds = new ArrayList<>(stationIdsSize);

    @BeforeEach
    void beforeEach() {
        for (int i = 0; i < stationIdsSize; i++) {
            stationIds.add(아이디_추출(지하철역_생성("지하철역" + i)));
        }
    }

    /**
     * Given 지하철역 3개와 지하철라인을 생성하고
     * When 생성한 지하철라인에 해당하는 지하철구간의 하행역을 상행역으로 하는 지하철구간을 추가로 생성하면
     * Then 성공 응답을 받는다.
     */
    @DisplayName("지하철구간 생성")
    @Test
    void createSection() {
        // given
        final Long lineId = 아이디_추출(지하철노선_생성("신분당선", "bg-red-600", stationIds.get(0), stationIds.get(1), 1));

        // when
        final ExtractableResponse<Response> response = 지하철구간_생성(lineId, stationIds.get(1), stationIds.get(2), 2);

        // then
        상태_코드_확인(response, HttpStatus.CREATED.value());
    }

    /**
     * Given 지하철역 2개와 지하철라인을 생성하고
     * When 생성한 지하철라인에 해당하는 지하철구간의 하행역이 아닌 상행역으로 지하철구간을 추가로 생성하면
     * Then 실패 응답을 받는다.
     */
    @DisplayName("잘못된 상행역으로 지하철구간 생성")
    @Test
    void createSectionWithWrongUpStationId() {
        // given
        final Long lineId = 아이디_추출(지하철노선_생성("신분당선", "bg-red-600", stationIds.get(0), stationIds.get(1), 1));

        // when
        final ExtractableResponse<Response> response = 지하철구간_생성(lineId, stationIds.get(2), stationIds.get(3), 3);

        // then
        상태_코드_확인(response, HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철역 2개, 지하철라인을 생성하고
     * When 이미 등록되어 있는 역을 하행역으로 지하철구간을 추가로 생성하면
     * Then 실패 응답을 받는다.
     */
    @DisplayName("이미 등록된 하행역으로 지하철구간 생성")
    @Test
    void createSectionWithAlreadyRegisteredDownStationId() {
        // given
        final Long lineId = 아이디_추출(지하철노선_생성("신분당선", "bg-red-600", stationIds.get(0), stationIds.get(1), 1));

        // when
        final ExtractableResponse<Response> response = 지하철구간_생성(lineId, stationIds.get(1), stationIds.get(0), 2);

        // then
        상태_코드_확인(response, HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철역 2개, 지하철역, 지하철구간을 생성하고
     * When 마지막으로 생성한 지하철구간을 삭제하면
     * Then 성공 응답을 받는다.
     */
    @DisplayName("지하철구간 삭제")
    @Test
    void removeSection() {
        // given
        final Long lineId = 아이디_추출(지하철노선_생성("신분당선", "bg-red-600", stationIds.get(0), stationIds.get(1), 1));
        지하철구간_생성(lineId, stationIds.get(1), stationIds.get(2), 2);

        // when
        final ExtractableResponse<Response> response = 지하철구간_삭제(lineId, stationIds.get(2));

        // then
        상태_코드_확인(response, HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철역 2개, 지하철역, 지하철구간을 생성하고
     * When 마지막이 아닌 지하철구간 삭제하면
     * Then 실패 응답을 받는다.
     */
    @DisplayName("마지막이 아닌 지하철구간 삭제")
    @Test
    void removeSectionWithoutLastSection() {
        // given
        final Long lineId = 아이디_추출(지하철노선_생성("신분당선", "bg-red-600", stationIds.get(0), stationIds.get(1), 1));
        지하철구간_생성(lineId, stationIds.get(1), stationIds.get(2), 2);

        // when
        final ExtractableResponse<Response> response = 지하철구간_삭제(lineId, stationIds.get(1));

        // then
        상태_코드_확인(response, HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철역 2개, 지하철역을 생성하고
     * When 마지막 하나 남은 지하철구간 삭제하면
     * Then 실패 응답을 받는다.
     */
    @DisplayName("마지막 하나 남은 지하철구간 삭제")
    @Test
    void removeSectionWithLastOneSection() {
        // given
        final Long lineId = 아이디_추출(지하철노선_생성("신분당선", "bg-red-600", stationIds.get(0), stationIds.get(1), 1));

        // when
        final ExtractableResponse<Response> response = 지하철구간_삭제(lineId, stationIds.get(1));

        // then
        상태_코드_확인(response, HttpStatus.BAD_REQUEST.value());
    }
}
