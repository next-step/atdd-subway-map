package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.LineSteps.*;
import static nextstep.subway.station.StationSteps.지하철역_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private StationResponse 강남역;
    private StationResponse 광교역;
    private StationResponse 추가역;
    private LineRequest lineRequest;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // given
        강남역 = 지하철역_생성_요청("강남역").as(StationResponse.class);
        광교역 = 지하철역_생성_요청("광교역").as(StationResponse.class);
        추가역 = 지하철역_생성_요청("추가역").as(StationResponse.class);
        lineRequest = new LineRequest("신분당선", "red", 강남역.getId(), 광교역.getId(), 10);
    }

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest);

        // then
        지하철_노선_생성됨(response);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성할 경우 에러 발생")
    @Test
    void createLineAboutExisting() {
        // given
        지하철_노선_생성_요청(lineRequest);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(lineRequest);

        // then
        지하철_노선_생성_실패됨(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        LineResponse line1 = 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
        LineResponse line2 = 지하철_노선_생성_요청(
            new LineRequest(
                "분당선",
                "yello",
                지하철역_생성_요청("태평역").as(StationResponse.class).getId(),
                지하철역_생성_요청("가천대역").as(StationResponse.class).getId(),
                10
            )
        ).as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_응답됨(response);
        지하철_노선_목록_포함됨(response, Arrays.asList(line1, line2));
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        LineResponse line = 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(line.getId());

        // then
        지하철_노선_응답됨(response, line);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        LineResponse line = 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(line.getId(), "노선수정", "색상수정");

        // then
        지하철_노선_수정됨(response, "노선수정", "색상수정");
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        LineResponse line = 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_노선_제거_요청(line.getId());

        // then
        지하철_노선_삭제됨(response);
    }

    @DisplayName("지하철 노선에 구간을 등록한다.")
    @Test()
    public void createSectionTest() {
        // given
        LineResponse 신분당선 = 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
        SectionRequest sectionRequest = SectionRequest.of(광교역.getId(), 추가역.getId(), 3);

        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(sectionRequest, 신분당선.getId());

        // then
        지하철_구간_생성_됨(response);
    }

    @DisplayName("지하철 노선에서 구간을 제거하는 테스트: 정상 삭제 테스트")
    @Test
    public void removeSectionTest() {
        // given
        LineResponse 신분당선 = 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
        SectionRequest sectionRequest = SectionRequest.of(광교역.getId(), 추가역.getId(), 3);

        지하철_구간_생성_요청(sectionRequest, 신분당선.getId());

        // when
        ExtractableResponse<Response> response = 지하철_구간_제거_요청(
            신분당선.getId(),
            추가역.getId()
        );

        // then
        지하철_구간_제거_됨(response);
    }

    @DisplayName("지하철 노선에서 구간을 제거하는 테스트: 구간이 1개인 경우 역을 삭제할 수 없다.")
    @Test
    public void removeSectionFailTest_01() {
        // given
        LineResponse 신분당선 = 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_구간_제거_요청(신분당선.getId(), 광교역.getId());

        // then
        지하철_구간_제거_실패_됨(response);
    }

    @DisplayName("지하철 노선에서 구간을 제거하는 테스트: 마지막역(하행 종점역)만 제거할 수 있다.")
    @Test
    public void removeSectionFailTest_02() {
        // given
        LineResponse 신분당선 = 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
        SectionRequest sectionRequest = SectionRequest.of(광교역.getId(), 추가역.getId(), 3);

        지하철_구간_생성_요청(sectionRequest, 신분당선.getId());

        // when
        ExtractableResponse<Response> response1 = 지하철_구간_제거_요청(
            신분당선.getId(),
            강남역.getId()
        );
        ExtractableResponse<Response> response2 = 지하철_구간_제거_요청(
            신분당선.getId(),
            광교역.getId()
        );

        // then
        지하철_구간_제거_실패_됨(response1);
        지하철_구간_제거_실패_됨(response2);
    }


    @DisplayName("지하철 노선 조회시 등록된 구간을 조회하는 테스트")
    @Test
    public void getLineWithSectionsTest() {
        // given
        LineResponse 신분당선 = 지하철_노선_생성_요청(lineRequest).as(LineResponse.class);
        SectionRequest sectionRequest = SectionRequest.of(광교역.getId(), 추가역.getId(), 3);

        지하철_구간_생성_요청(sectionRequest, 신분당선.getId());

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(신분당선.getId());

        // then
        지하철_노선_조회_됨(response);
        지하철_노선에_구간_포함_됨(response, Arrays.asList(강남역, 광교역, 추가역));
    }

    private void 지하철_노선_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 지하철_노선_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 지하철_노선_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_노선_목록_포함됨(
        ExtractableResponse<Response> response,
        List<LineResponse> expected
    ) {
        List<LineResponse> lines = response.body().jsonPath().getList(".", LineResponse.class);
        assertThat(lines.get(0).getName()).isEqualTo(expected.get(0).getName());
        assertThat(lines.get(0).getColor()).isEqualTo(expected.get(0).getColor());
        assertThat(lines.get(1).getName()).isEqualTo(expected.get(1).getName());
        assertThat(lines.get(1).getColor()).isEqualTo(expected.get(1).getColor());
    }

    private void 지하철_노선_응답됨(ExtractableResponse<Response> response, LineResponse expected) {
        LineResponse line = response.as(LineResponse.class);

        assertThat(line.getId()).isEqualTo(expected.getId());assertThat(line.getName()).isEqualTo(expected.getName());assertThat(line.getColor()).isEqualTo(expected.getColor()); }

    private void 지하철_노선_수정됨(
        ExtractableResponse<Response> response,
        String expectedName,
        String expectedColor
    ) {
        LineResponse line = response.as(LineResponse.class);

        assertThat(line.getName()).isEqualTo(expectedName);assertThat(line.getColor()).isEqualTo(expectedColor); }

    private void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


    public void 지하철_구간_생성_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 지하철_구간_제거_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 지하철_구간_제거_실패_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 지하철_노선_조회_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_노선에_구간_포함_됨(
        ExtractableResponse<Response> response,
        List<StationResponse> expected
    ) {
        List<Long> resultStationIds = response.as(LineResponse.class)
            .getStations()
            .stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        List<Long> expectedStationIds = expected.stream()
            .map(StationResponse::getId)
            .collect(Collectors.toList());

        assertThat(resultStationIds).containsAll(expectedStationIds);
    }
}
