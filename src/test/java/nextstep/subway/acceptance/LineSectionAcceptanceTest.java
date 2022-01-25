package nextstep.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("지하철 구간 관련 기능")
public class LineSectionAcceptanceTest extends AcceptanceTest{

    private LineResponse 일호선역;
    private StationResponse 일호선역1;
    private StationResponse 일호선역2;
    private StationResponse 일호선역3;
    private StationResponse 일호선역4;

    @BeforeEach
    public void setUp() {
        super.setUp();

        일호선역1 = StationAcceptanceTest.지하철_역_을_등록한다(StationRequest.of("1호선역1")).as(StationResponse.class);
        일호선역2 = StationAcceptanceTest.지하철_역_을_등록한다(StationRequest.of("1호선역2")).as(StationResponse.class);
        일호선역3 = StationAcceptanceTest.지하철_역_을_등록한다(StationRequest.of("1호선역3")).as(StationResponse.class);
        일호선역4 = StationAcceptanceTest.지하철_역_을_등록한다(StationRequest.of("1호선역4")).as(StationResponse.class);

        LineRequest lineRequest = LineRequest.of("신분당선", "bg-red-600", 일호선역1.getId(), 일호선역4.getId(), 10);
        일호선역 = LineAcceptanceTest.지하철_노선을_등록한다(lineRequest).as(LineResponse.class);
    }

    /**
     지하철 노선 생성 시 필요한 인자 추가하기
     지하철 노선에 구간을 등록하는 기능 구현
     지하철 노선에 구간을 제거하는 기능 구현
     지하철 노선에 등록된 구간을 통해 역 목록을 조회하는 기능 구현
     구간 등록 / 제거 시 예외 케이스에 대한 인수 테스트 작성
     */

    @DisplayName("지하철 노선에 구간을 추가한 후 구간을 조회해서 확인한다.")
    @Test
    void 지하철_노선에_구간을_등록하고_조회해서_확인한다() {
        // when
        ExtractableResponse<Response> 노선구간등록결과 = 지하철_노선에_구간을_등록한다(일호선역, 일호선역1, 일호선역2, 10);
        // then
        응답결과가_OK(노선구간등록결과);

        // when
        ExtractableResponse<Response> 노선역조회결과 = 지하철_노선의_역들을_조회한다(일호선역);
        // then
        원하는_역들이_들어있다(노선역조회결과, Arrays.asList(일호선역1, 일호선역2));
    }

    @DisplayName("지하철 노선에 추가하는 새로운 구간의 상행역이 기존 하행역과 맞지 않는다.")
    @Test
    void 지하철_노선에_구간을_등록_실패한다_1() {
        // given
        지하철_노선에_구간을_등록한다(일호선역, 일호선역1, 일호선역2, 10);
        지하철_노선에_구간을_등록한다(일호선역, 일호선역2, 일호선역3, 10);

        // when // then
        ExtractableResponse<Response> 구간등록결과 = 지하철_노선에_구간을_등록한다(일호선역, 일호선역1, 일호선역4, 5);
        응답결과가_BAD_REQUEST(구간등록결과);
    }

    @DisplayName("지하철 노선에 추가하는 새로운 구간의 하행역이 현재 등록되어 있는 역이다.")
    @Test
    void 지하철_노선에_구간을_등록_실패한다_2() {
        // given
        지하철_노선에_구간을_등록한다(일호선역, 일호선역1, 일호선역2, 10);
        지하철_노선에_구간을_등록한다(일호선역, 일호선역2, 일호선역3, 10);

        // when // then
        ExtractableResponse<Response> 구간등록결과 = 지하철_노선에_구간을_등록한다(일호선역, 일호선역3, 일호선역1, 5);
        응답결과가_BAD_REQUEST(구간등록결과);
    }

    public static void 원하는_역들이_들어있다(ExtractableResponse<Response> 노선역조회결과, List<StationResponse> 원하는결과) {
        응답결과가_OK(노선역조회결과);

        LineResponse lineResponse = 노선역조회결과.as(LineResponse.class);
        List<String> resultNames = lineResponse.getStations().stream()
            .map(StationResponse::getName)
            .collect(Collectors.toList());

        List<String> names = 원하는결과.stream()
            .map(StationResponse::getName)
            .collect(Collectors.toList());

        assertThat(resultNames).containsAll(names);
    }

    private static ExtractableResponse<Response> 지하철_노선의_역들을_조회한다(LineResponse 일호선역) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/lines/{lineId}", 일호선역.getId())
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선에_구간을_등록한다(LineResponse line, StationResponse upStation, StationResponse downStation, int distance) {
        SectionRequest sectionRequest = SectionRequest.of(upStation.getId(), downStation.getId(), distance);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(sectionRequest)
            .when().post("/lines/{lineId}/sections", line.getId())
            .then().log().all()
            .extract();
    }

}
