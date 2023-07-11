package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import subway.common.exception.ErrorMessage;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.line.LineStep.분당선;
import static subway.line.LineStep.신분당선;
import static subway.station.StationStep.*;
import static subway.util.Extractor.getId;

@Sql("/sql/all-table-truncate.sql")
@DisplayName("지하철노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {

        //given
        //when
        LineStep.지하철노선을_추가(Arrays.asList(신분당선));

        //then
        LineStep.지하철노선을_확인(Arrays.asList(신분당선));
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void searchLines() {
        //given
        LineStep.지하철노선을_추가(Arrays.asList(신분당선, 분당선));

        //when

        //then
        LineStep.지하철노선을_확인(Arrays.asList(신분당선, 분당선));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선을 조회한다.")
    @Test
    void searchLine() {

        //given
        Long id = getId(LineStep.지하철노선을_추가(Arrays.asList(신분당선)));

        //when

        //then
        LineStep.지하철노선을_확인(id, 신분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선을 수정한다.")
    @Test
    void modifyLine() {

        //given
        Long id = getId(LineStep.지하철노선을_추가(Arrays.asList(신분당선)));

        //when
        LineStep.지하철노선을_수정(id, 분당선);

        //then
        LineStep.지하철노선을_확인(id, 분당선);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선을 삭제한다.")
    @Test
    void deleteLine() {

        //given
        Long id = getId(LineStep.지하철노선을_추가(Arrays.asList(신분당선)));

        //when
        LineStep.지하철노선을_삭제(id);

        //then
        LineStep.지하철노선_삭제확인(id);
    }

    /**
     * Given 지하철역 및 지하철 노선을 생성하고
     * When 지하철 구간을 추가하면
     * Then 해당 지하철 노선의 구간의 정보를 확인할 수 있다.
     */
    @DisplayName("지하철 구간을 추가한다.")
    @Test
    void addSection() {

        //given
        Map<String, String> params = LineStep.지하철역_및_지하철_노선을_생성(강남역, 판교역, 신분당선);

        //when
        LineStep.지하철노선의_구간을_추가(params);

        //then
        LineStep.지하철노선의_구간_확인(params);
    }

    /**
     * Given 지하철역 및 지하철 노선을 생성하고 지하철 구간을 추가하고
     * When 새로운 구간을 연장하면
     * Then 해당 지하철 노선의 연장된 구간의 정보를 확인할 수 있다.
     */
    @DisplayName("지하철 구간을 연장한다.")
    @Test
    void addSections() {

        //given
        Map<String, String> params = LineStep.신분당선_등록();

        //when
        LineStep.지하철노선의_구간을_연장(params, 신논현역);

        //then
        LineStep.지하철노선의_구간_확인(params);
    }

    /**
     * Given 지하철역 및 지하철 노선을 생성하고 지하철 구간을 추가하고
     * When 새로운 구간을 등록할 때
     * Then 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이 아닐경우 에러가 발생한다
     */
    @DisplayName("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이 아닐경우 에러가 발생한다.")
    @Test
    void addSectionError() {

        //given
        Map<String, String> params = LineStep.신분당선_등록();

        //when
        ExtractableResponse<Response> response = LineStep.지하철_노선_하행_종점역과_상행역이_다른_구간_생성(params);

        //then
        LineStep.BAD_REQUEST_확인(response, ErrorMessage.SECTION_INTEGRITY_ADD_ERROR_MESSAGE);
    }

    /**
     * Given 지하철역 및 지하철 노선을 생성하고 지하철 구간을 추가하고
     * When 새로운 구간을 등록할 때
     * Then 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다
     */
    @DisplayName("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.")
    @Test
    void addSectionAlreadyError() {

        //given
        Map<String, String> params = LineStep.신분당선_등록();

        //when
        ExtractableResponse<Response> response = LineStep.등록되어_있는_하행역_생성(params);

        //then
        LineStep.BAD_REQUEST_확인(response, ErrorMessage.SECTION_INTEGRITY_ADD_ERROR_MESSAGE);
    }

    /**
     * Given 지하철역 및 지하철 노선을 생성, 지하철 구간을 2개를 추가 하고
     * When 구간을 축소 할 경우
     * Then 해당 구간은 삭제가 된다.
     */
    @DisplayName("지하철구간을 삭제한다")
    @Test
    void deleteSection() {

        //given
        Map<String, String> params = LineStep.신분당선_등록();
        LineStep.지하철노선의_구간을_연장(params, 신논현역);

        //when
        ExtractableResponse<Response> response = LineStep.지하철구간을_축소(Long.valueOf(params.get("lineId")),
                Long.valueOf(params.get("downStationId")));

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철역 및 지하철 노선을 생성, 지하철 구간을 2개를 추가 하고
     * When 구간을 축소 할 경우
     * Then 종점이 아닌 경우는 삭제가 안된다.
     */
    @DisplayName("종점이 아닌 경우는 삭제가 안된다")
    @Test
    void deleteNotEndStationSection() {

        //given
        Map<String, String> params = LineStep.신분당선_등록();
        LineStep.지하철노선의_구간을_연장(params, 신논현역);

        //when
        ExtractableResponse<Response> response = LineStep.지하철구간을_축소(Long.valueOf(params.get("lineId")),
                Long.valueOf(params.get("upStationId")));

        //then
        LineStep.BAD_REQUEST_확인(response, ErrorMessage.SECTION_INTEGRITY_REMOVE_ERROR_MESSAGE);
    }

    /**
     * Given 지하철역 및 지하철 노선을 생성, 지하철 구간을 추가 하고
     * When 구간을 축소 할 경우
     * Then 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다
     */
    @DisplayName("상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다")
    @Test
    void deleteNotOneSection() {

        //given
        Map<String, String> params = LineStep.신분당선_등록();

        //when
        ExtractableResponse<Response> response = LineStep.지하철구간을_축소(Long.valueOf(params.get("lineId")),
                Long.valueOf(params.get("downStationId")));

        //then
        LineStep.BAD_REQUEST_확인(response, ErrorMessage.SECTION_INTEGRITY_REMOVE_ERROR_MESSAGE);
    }
}
