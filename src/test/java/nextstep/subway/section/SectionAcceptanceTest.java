package nextstep.subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.dto.SectionResponse;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.LineSteps.*;
import static nextstep.subway.section.SectionSteps.*;
import static nextstep.subway.station.StationSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest {

    private static final String 분당선 = "분당선";
    private static final String 태평역 = "태평역";
    private static final String 가천대역 = "가천대역";
    private static final String 복정역 = "복정역";

    @DisplayName("지하철 노선에 구간을 등록한다.")
    @Test()
    public void createSectionTest() {
        // given
        LineResponse line = 지하철_노선_생성_요청(분당선, "yello").as(LineResponse.class);
        StationResponse station1 = 지하철역_생성_요청(태평역).as(StationResponse.class);
        StationResponse station2 = 지하철역_생성_요청(가천대역).as(StationResponse.class);
        SectionRequest sectionRequest = SectionRequest.of(
            station1.getId(),
            station2.getId(),
            3
        );

        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(
            sectionRequest,
            line.getId()
        );

        // then
        지하철_구간_생성_됨(response, sectionRequest, line.getId());
    }


    public void 지하철_구간_생성_됨(
        ExtractableResponse<Response> response,
        SectionRequest expected,
        long expectedLineId
    ) {

        SectionResponse sectionResponse = response.as(SectionResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(sectionResponse.getLine().getId()).isEqualTo(expectedLineId);
        assertThat(sectionResponse.getUpStation().getId()).isEqualTo(expected.getUpStationId());
        assertThat(sectionResponse.getDownStation().getId()).isEqualTo(expected.getDownStationId());
        assertThat(sectionResponse.getDistance()).isEqualTo(expected.getDistance());
        assertThat(sectionResponse.getDistance()).isEqualTo(expected.getDistance());
    }

    @DisplayName("지하철 노선에서 구간을 제거하는 테스트: 정상 삭제 테스트")
    @Test
    public void removeSectionTest() {
        // given
        LineResponse line = 지하철_노선_생성_요청(분당선, "yello").as(LineResponse.class);
        StationResponse station1 = 지하철역_생성_요청(태평역).as(StationResponse.class);
        StationResponse station2 = 지하철역_생성_요청(가천대역).as(StationResponse.class);
        StationResponse station3 = 지하철역_생성_요청(복정역).as(StationResponse.class);

        지하철_구간_생성_요청(
            SectionRequest.of(station1.getId(), station2.getId(), 3),
            line.getId()
        ).as(SectionResponse.class);

        지하철_구간_생성_요청(
            SectionRequest.of(station2.getId(), station3.getId(), 4),
            line.getId()
        ).as(SectionResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_구간_제거_요청(
            line.getId(),
            station3.getId()
        );

        // then
        지하철_구간_제거_됨(response);
    }

    private void 지하철_구간_제거_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("지하철 노선에서 구간을 제거하는 테스트: 구간이 1개인 경우 역을 삭제할 수 없다.")
    @Test
    public void removeSectionFailTest_01() {
        // given
        LineResponse line = 지하철_노선_생성_요청(분당선, "yello").as(LineResponse.class);
        StationResponse station1 = 지하철역_생성_요청(태평역).as(StationResponse.class);
        StationResponse station2 = 지하철역_생성_요청(가천대역).as(StationResponse.class);

        지하철_구간_생성_요청(
            SectionRequest.of(station1.getId(), station2.getId(), 3),
            line.getId()
        ).as(SectionResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_구간_제거_요청(
            line.getId(),
            station2.getId()
        );

        // then
        지하철_구간_제거_실패_됨(response);
    }

    @DisplayName("지하철 노선에서 구간을 제거하는 테스트: 마지막 역(하행 종점역)만 제거할 수 있다.")
    @Test
    public void removeSectionFailTest_02() {
        // given
        LineResponse line = 지하철_노선_생성_요청(분당선, "yello").as(LineResponse.class);
        StationResponse station1 = 지하철역_생성_요청(태평역).as(StationResponse.class);
        StationResponse station2 = 지하철역_생성_요청(가천대역).as(StationResponse.class);
        StationResponse station3 = 지하철역_생성_요청(복정역).as(StationResponse.class);

        지하철_구간_생성_요청(
            SectionRequest.of(station1.getId(), station2.getId(), 3),
            line.getId()
        ).as(SectionResponse.class);

        지하철_구간_생성_요청(
            SectionRequest.of(station2.getId(), station3.getId(), 4),
            line.getId()
        ).as(SectionResponse.class);

        // when
        ExtractableResponse<Response> response1 = 지하철_구간_제거_요청(
            line.getId(),
            station1.getId()
        );
        ExtractableResponse<Response> response2 = 지하철_구간_제거_요청(
            line.getId(),
            station2.getId()
        );

        // then
        지하철_구간_제거_실패_됨(response1);
        지하철_구간_제거_실패_됨(response2);
    }

    private void 지하철_구간_제거_실패_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    @DisplayName("지하철 노선 조회시 등록된 구간을 조회하는 테스트")
    @Test
    public void getLineWithSectionsTest() {

        // given
        LineResponse line = 지하철_노선_생성_요청(분당선, "yello").as(LineResponse.class);
        StationResponse station1 = 지하철역_생성_요청(태평역).as(StationResponse.class);
        StationResponse station2 = 지하철역_생성_요청(가천대역).as(StationResponse.class);
        StationResponse station3 = 지하철역_생성_요청(복정역).as(StationResponse.class);

        지하철_구간_생성_요청(
            SectionRequest.of(station1.getId(), station2.getId(), 3),
            line.getId()
        ).as(SectionResponse.class);

        지하철_구간_생성_요청(
            SectionRequest.of(station2.getId(), station3.getId(), 4),
            line.getId()
        ).as(SectionResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(line.getId());

        // then
        지하철_노선_조회_됨(response);
        지하철_노선에_구간_포함_됨(response, Arrays.asList(
            station1, station2, station3
        ));
    }

    private void 지하철_노선_조회_됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode())
                  .isEqualTo(HttpStatus.OK.value());
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
