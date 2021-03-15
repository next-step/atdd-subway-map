package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.line.LineSteps.*;
import static nextstep.subway.station.StationSteps.지하철역_등록;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 내 구간 관련 기능")
public class LineSectionAcceptanceTest extends AcceptanceTest {
    private LineResponse 초록2호선;
    private StationResponse 강남역;
    private StationResponse 역삼역;
    private StationResponse 선릉역;
    private StationResponse 잠실역;

    @BeforeEach
   public void setUp() {
        super.setUp();
        // given
        강남역 = 지하철역_등록("강남역").as(StationResponse.class);
        역삼역 = 지하철역_등록("역삼역").as(StationResponse.class);
        선릉역 = 지하철역_등록("선릉역").as(StationResponse.class);
        잠실역 = 지하철역_등록("잠실역").as(StationResponse.class);

        Map<String, String> lineCreateParams;
        lineCreateParams = new HashMap<>();
        lineCreateParams.put("name", "2호선");
        lineCreateParams.put("color", "bg-green-600");
        lineCreateParams.put("upStationId", 강남역.getId().toString());
        lineCreateParams.put("downStationId", 역삼역.getId().toString());
        lineCreateParams.put("distance", "10");

        초록2호선 = 지하철_노선_등록(lineCreateParams).as(LineResponse.class);
    }

    @DisplayName("지하철 노선에 구간을 최초로 생성한다.")
    @Test
    void addLineSection() {
        //when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록(초록2호선,역삼역,선릉역,4);

        // then
        지하철_노선에_지하철역_등록_성공(response);
//        지하철_노선에_지하철역_포함_확인(response);
//        지하철_노선의_신규_종점역임을_확인(response);
    }

    @DisplayName("현재 하행 종점역이 아닌 역을 상행으로 구간을 추가한다.")
    @Test
    void addLineSectionWithStrangeUpStation() {

        //when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록(초록2호선,강남역,잠실역,10);

        // then --NotEqualsLastStationException
        지하철_노선에서_삭제_실패(response);
    }

    @DisplayName("기존에 등록돼 있는 역을 구간으로 추가한다.")
    @Test
    void addLineSectionWithDuplicateStation() {

        //when
        ExtractableResponse<Response> response = 지하철_노선에_지하철역_등록(초록2호선,역삼역,강남역,10);

        // then --DuplicateStationException
        지하철_노선에서_삭제_실패(response);
    }

    @DisplayName("지하철 노선에서 구간(종점역)을 제거한다.")
    @Test
    void deleteLineSection() {
        //given
        ExtractableResponse<Response> createLineSection = 지하철_노선에_지하철역_등록(초록2호선,역삼역,선릉역,4);
        SectionResponse section = 지하철_노선에서_마지막_구간_조회(초록2호선.getId()).as(SectionResponse.class);

        //when
        ExtractableResponse<Response> response = 지하철_노선에서_지하철역_제거(초록2호선.getId(),section.getDownStationId());

        // then
        지하철_노선에_지하철역_삭제_성공(response);
    }

    @DisplayName("지하철 노선에서 종점역이 아닌 구간을 제거한다.")
    @Test
    void deleteLineSectionWithNotFinalStation() {
        //given
        ExtractableResponse<Response> createLineSection = 지하철_노선에_지하철역_등록(초록2호선,역삼역,선릉역,4);

        //when
        ExtractableResponse<Response> response = 지하철_노선에서_지하철역_제거(초록2호선.getId(),강남역.getId());

        // then --IllegalArgumentException 종점역만 삭제가 가능합니다.
        지하철_노선에서_삭제_실패(response);
    }

    @DisplayName("지하철 노선에 남은 1개의 구간의 하행 삭제를 시도한다.")
    @Test
    void deleteLastLineSection() {
        //
        SectionResponse section = 지하철_노선에서_마지막_구간_조회(초록2호선.getId()).as(SectionResponse.class);

        //when
        ExtractableResponse<Response> response = 지하철_노선에서_지하철역_제거(초록2호선.getId(),section.getUpStationId());

        // then --마지막 구간은 삭제할 수 없습니다.
        지하철_노선에서_삭제_실패(response);
    }



    @DisplayName("지하철 노선에 등록된 구간을 조회한다")
    @Test
    void getLineWithSection() {
        //given
        LineResponse newSectionRequest = 지하철_노선에_지하철역_등록(초록2호선,역삼역,선릉역,4).as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_노선에_등록된역_조회_요청(newSectionRequest.getId());

        // then
        지하철_노선에_등록된역_조회_성공(response);
    }

    private void 지하철_노선_조회_성공(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_노선에_등록된역_조회_성공(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_노선에_지하철역_등록_성공(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void 지하철_노선에_지하철역_등록_실패(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 지하철_노선에_지하철역_삭제_성공(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 지하철_노선에서_삭제_실패(ExtractableResponse<Response> response){
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


}