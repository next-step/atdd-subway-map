package nextstep.subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.LineRequestStep.*;
import static nextstep.subway.section.SectionRequestStep.*;
import static nextstep.subway.section.SectionVerificationStep.*;
import static nextstep.subway.station.StationRequestStep.*;

@DisplayName("지하철 구간 관련 테스트")
class SectionAcceptanceTest extends AcceptanceTest {

    private Long 신분당선, 강남역, 역삼역, 삼성역;
    private LineRequest 첫번째_라인_요청;

    @BeforeEach
    public void setUp() {
        super.beforeEach();
        신분당선 = 지하철_노선_등록되어_있음("신분당선", "red");
        강남역 = 지하철역_등록되어_있음("강남역");
        역삼역 = 지하철역_등록되어_있음("역삼역");
        삼성역 = 지하철역_등록되어_있음("삼성역");
        첫번째_라인_요청 = LineRequest.of(강남역, 역삼역, 10);
    }

    @DisplayName("지하철 구간을 등록한다")
    @Test
    void createSection() {
        ExtractableResponse<Response> response = 지하철_구간_등록요청(신분당선, 첫번째_라인_요청);

        지하철_구간_등록됨(response);
    }

    @DisplayName("새로운 구간의 하행역은 현재 등록된 역일 수 없다")
    @Test
    void createSectionWithoutDuplicateDownStation() {
        지하철_구간_등록되어_있음(신분당선, 첫번째_라인_요청);

        ExtractableResponse<Response> response = 지하철_구간_등록요청(신분당선, LineRequest.of(역삼역, 강남역, 10));

        지하철_구간_등록실패됨(response);
    }

    @DisplayName("새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 한다")
    @Test
    void createSectionNewUpStationMustBePreviousDownStation() {
        지하철_구간_등록되어_있음(신분당선, 첫번째_라인_요청);

        ExtractableResponse<Response> response = 지하철_구간_등록요청(신분당선, LineRequest.of(강남역, 삼성역, 10));

        지하철_구간_등록실패됨(response);
    }

    @DisplayName("지하철 구간 삭제")
    @Test
    void delete() {
        지하철_구간_등록되어_있음(신분당선, 첫번째_라인_요청);
        지하철_구간_등록되어_있음(신분당선, LineRequest.of(역삼역, 삼성역, 10));

        ExtractableResponse<Response> response = 지하철_구간_삭제요청(신분당선);

        지하철_구간_삭제됨(response);
    }

    @DisplayName("지하철 노선에 구간이 하나인 경우 역을 삭제할 수 없다")
    @Test
    void deleteNotAllowedWhenOneSectionLeft() {
        지하철_구간_등록되어_있음(신분당선, 첫번째_라인_요청);

        ExtractableResponse<Response> response = 지하철_구간_삭제요청(신분당선);

        지하철_구간_삭제실패됨(response);
    }
}