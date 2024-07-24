package subway.section;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import subway.common.dto.ErrorResponse;
import subway.line.dto.LineResponse;
import subway.section.dto.SectionRequest;
import subway.station.dto.StationResponse;

import static subway.common.constant.ErrorCode.*;
import static subway.util.LineStep.지하철_노선_생성;
import static subway.util.LineStep.지하철_노선_조회;
import static subway.util.SectionStep.*;
import static subway.util.StationStep.지하철_역_등록;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    private StationResponse 강남역;
    private StationResponse 선릉역;
    private StationResponse 삼성역;
    private LineResponse 신분당선;

    @BeforeEach
    @Sql(scripts = "classpath:truncate-tables.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void setup() {
        강남역 = 지하철_역_등록("강남역");
        선릉역 = 지하철_역_등록("선릉역");
        삼성역 = 지하철_역_등록("삼성역");
        신분당선 = 지하철_노선_생성("신분당선", "Red", 강남역.getId(), 선릉역.getId(), 10L);
    }

    /**
     * User Story : 관리자로서, 지하철 노선도 관리를 하기 위해 지하철 역을 노선에 등록한다.
     **/

    /* Given: 지하철 역과 지하철 노선이 등록되어 있고,
       When: 관리자가 지하철 노선에 새로운 구간을 등록 요청하면,
       Then: 지하철 노선에 관리자가 등록 요청한 새로운 구간이 추가된다. */
    @DisplayName("지하철 구간을 등록한다.")
    @Test
    public void addSection_success() {
        // given ...

        // when
        지하철_구간_등록(신분당선.getId(), SectionRequest.of(선릉역.getId(), 삼성역.getId(), 10L));
        LineResponse 구간이_등록된_신분당선 = 지하철_노선_조회(신분당선.getId());

        // then
        Assertions.assertTrue(구간이_등록된_신분당선.getStations().contains(삼성역));

    }

    /* Given: 지하철 역과 지하철 노선이 등록되어 있고,
       When: 관리자가 지하철 노선에 "기존의 하행 종점역과 불일치하는 상행역을 가진 새로운 구간"을 등록 요청하면,
       Then: 관리자의 구간 추가 요청은 실패한다. */
    @DisplayName("지하철 구간 등록에 실패한다. 새로운 구간의 상행역은 등록되어 있는 하행 종점역이어야 한다.")
    @Test
    public void addSection_fail() {
        // given ...

        // when & then
        ErrorResponse errorResponse = 지하철_구간_등록_실패(신분당선.getId(), SectionRequest.of(강남역.getId(), 삼성역.getId(), 10L));
        Assertions.assertEquals(errorResponse.getCode(), SECTION_NOT_MATCH.getCode());

    }

    /* Given: 지하철 역과 지하철 노선이 등록되어 있고,
    When: 관리자가 지하철 노선에 "기존의 지하철 노선에 등록되어 있는 역을 하행역으로 가진 새로운 구간"을 등록 요청하면,
    Then: 관리자의 구간 추가 요청은 실패한다. */
    @DisplayName("지하철 구간 등록에 실패한다. 이미 노선에 등록되어 있는 지하철 역은 새로운 구간의 하행역이 될 수 없다.")
    @Test
    public void addSection_fail_2() {
        // given ...

        // when & then
        ErrorResponse errorResponse = 지하철_구간_등록_실패(신분당선.getId(), SectionRequest.of(강남역.getId(), 삼성역.getId(), 10L));
        Assertions.assertEquals(errorResponse.getCode(), SECTION_ALREADY_EXIST.getCode());

    }

    /* Given: 지하철 역과 지하철 노선이 등록되어 있고,
       When: 관리자가 지하철 노선의 마지막 구간을 삭제 요청하면,
       Then: 관리자가 삭제 요청한 구간이 삭제된다. */
    @DisplayName("지하철 구간을 삭제한다.")
    @Test
    public void deleteSection_success() {
        // given ...
        지하철_구간_등록(신분당선.getId(), SectionRequest.of(선릉역.getId(), 삼성역.getId(), 10L));
        LineResponse 구간이_등록된_신분당선 = 지하철_노선_조회(신분당선.getId());
        Assertions.assertTrue(구간이_등록된_신분당선.getStations().contains(삼성역));

        // when
        지하철_구간_삭제(구간이_등록된_신분당선.getId(), 삼성역.getId());

        // then
        LineResponse 구간이_삭제된_신분당선 = 지하철_노선_조회(신분당선.getId());
        Assertions.assertFalse(구간이_삭제된_신분당선.getStations().contains(삼성역));

    }

    /* Given: 지하철 역과 지하철 노선이 등록되어 있고,
       When: 관리자가 지하철 노선의 하행 종점역이 아닌 역을 삭제 요청하면,
       Then: 관리자가 구간 삭제 요청은 실패한다. */
    @DisplayName("지하철 구간 삭제를 실패한다. 하행 종점역만 삭제 가능하다.")
    @Test
    public void deleteSection_fail() {
        // given ...
        지하철_구간_등록(신분당선.getId(), SectionRequest.of(선릉역.getId(), 삼성역.getId(), 10L));
        LineResponse 구간이_등록된_신분당선 = 지하철_노선_조회(신분당선.getId());
        Assertions.assertTrue(구간이_등록된_신분당선.getStations().contains(삼성역));

        // when & then
        ErrorResponse errorResponse = 지하철_구간_삭제_실패(구간이_등록된_신분당선.getId(), 선릉역.getId());
        Assertions.assertEquals(errorResponse.getCode(), SECTION_NOT_PERMISSION_NOT_LAST_DESCENDING_STATION.getCode());

    }

    /* Given: 지하철 역과 지하철 노선이 등록되어 있고,
        When: 관리자가 지하철 노선의 구간이 1개인 경우,
        Then: 관리자가 삭제 요청은 실패한다. */
    @DisplayName("지하철 구간 삭제 실패한다. 지하철 노선 구간이 1개 이상이어야 삭제가능하다.")
    @Test
    public void deleteSection_fail_2() {
        // given ...
        LineResponse 구간이_등록된_신분당선 = 지하철_노선_조회(신분당선.getId());
        Assertions.assertTrue(구간이_등록된_신분당선.getStations().contains(강남역));

        // when & then
        ErrorResponse errorResponse = 지하철_구간_삭제_실패(구간이_등록된_신분당선.getId(), 선릉역.getId());
        Assertions.assertEquals(errorResponse.getCode(), SECTION_NOT_PERMISSION_COUNT_TOO_LOW.getCode());

    }

}
