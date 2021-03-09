package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.support.LineSteps;
import nextstep.subway.line.support.SectionSteps;
import nextstep.subway.line.support.SectionVerifier;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.support.StationSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선에 역 등록 관련 기능")
public class LineSectionAcceptanceTest extends AcceptanceTest {

    private LineResponse 이호선;
    private StationResponse 강남역;
    private StationResponse 방배역;
    private StationResponse 역삼역;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강남역 = StationSteps.지하철역_등록됨(new StationRequest("강남역"));
        역삼역 = StationSteps.지하철역_등록됨(new StationRequest("역삼역"));
        방배역 = StationSteps.지하철역_등록됨(new StationRequest("방배역"));

        이호선 = LineSteps.지하철_노선이_등록됨(new LineRequest("2호선", "green"));
    }


    @DisplayName("지하철 노선에 구간을 등록한다.")
    @Test
    public void addSection() {
        //When
        SectionSteps.지하철_노선에_지하철역_등록요청(이호선.getId(), new SectionRequest(강남역.getId(), 역삼역.getId(), 6));
        SectionSteps.지하철_노선에_지하철역_등록요청(이호선.getId(), new SectionRequest(역삼역.getId(), 방배역.getId(), 5));

        //Then
        ExtractableResponse<Response> expected = LineSteps.지하철_특정노선_찾기_요청(이호선.getId());
        SectionVerifier.지하철_노선에_지하철역_등록됨(expected);
        SectionVerifier.지하철_노선에_지하철역_정렬됨(expected, Arrays.asList(강남역.getId(), 역삼역.getId(), 방배역.getId()));
    }

    @DisplayName("지하철 노선에 하행역과 새롭게 등록될 상행역이 다른역인 조건에서 등록한다.")
    @Test
    public void addSectionAlreadyStation() {
        //Given
        SectionSteps.지하철_노선에_지하철역_등록요청(이호선.getId(), new SectionRequest(강남역.getId(), 역삼역.getId(), 6));

        //When
        ExtractableResponse<Response> response = SectionSteps.지하철_노선에_지하철역_등록요청(이호선.getId(), new SectionRequest(강남역.getId(), 역삼역.getId(), 6));

        //Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("지하철 노선의 하행역과 새롭게 등록될 하행역이 같은역인 조건에서 등록한다.")
    @Test
    public void addSectionMatchDownStation() {
        //Given
        SectionSteps.지하철_노선에_지하철역_등록요청(이호선.getId(), new SectionRequest(강남역.getId(), 역삼역.getId(), 6));

        //When
        ExtractableResponse<Response> response = SectionSteps.지하철_노선에_지하철역_등록요청(이호선.getId(), new SectionRequest(역삼역.getId(), 역삼역.getId(), 6));

        //Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("지하철 노선 구간에 등록된 역을 제거한다.")
    @Test
    public void removeSection() {
        //Given
        SectionSteps.지하철_노선에_지하철역_등록요청(이호선.getId(), new SectionRequest(강남역.getId(), 역삼역.getId(), 6));
        SectionSteps.지하철_노선에_지하철역_등록요청(이호선.getId(), new SectionRequest(역삼역.getId(), 방배역.getId(), 5));

        //When
        ExtractableResponse<Response> response = SectionSteps.지하철_노선에_지하철역_구간_제외요청(이호선.getId(), 방배역.getId());
        ExtractableResponse<Response> expected = LineSteps.지하철_특정노선_찾기_요청(이호선.getId());

        //Then
        SectionVerifier.지하철_노선에_지하철역_제외됨(response);
        SectionVerifier.지하철_노선에_지하철역_정렬됨(expected, Arrays.asList(강남역.getId(), 역삼역.getId()));
    }

    @DisplayName("지하철 노선 구간이 1개일 때 등록된 역을 제거한다.")
    @Test
    public void removeWhenSectionLessThenOne() {
        //Given
        SectionSteps.지하철_노선에_지하철역_등록요청(이호선.getId(), new SectionRequest(강남역.getId(), 역삼역.getId(), 6));

        //When
        ExtractableResponse<Response> response = SectionSteps.지하철_노선에_지하철역_구간_제외요청(이호선.getId(), 방배역.getId());

        //Then
        SectionVerifier.지하철_노선에_역_제거실패(response);
    }
}
