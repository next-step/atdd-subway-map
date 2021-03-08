package nextstep.subway.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.section.dto.SectionRequest;
import nextstep.subway.section.dto.SectionResponse;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.line.LineSteps.*;
import static nextstep.subway.section.SectionSteps.*;
import static nextstep.subway.station.StationSteps.*;

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
        LineResponse lineResponse = 지하철_노선_생성_요청(분당선, "yello").as(LineResponse.class);
        StationResponse stationResponse1 = 지하철역_생성_요청(태평역).as(StationResponse.class);
        StationResponse stationResponse2 = 지하철역_생성_요청(가천대역).as(StationResponse.class);
        SectionRequest sectionRequest = SectionRequest.of(
            stationResponse1.getId(),
            stationResponse2.getId(),
            3
        );

        // when
        ExtractableResponse<Response> response = 지하철_구간_생성_요청(
            sectionRequest,
            lineResponse.getId()
        );

        // then
        지하철_구간_생성_됨(response, sectionRequest, lineResponse.getId());
    }


    public void 지하철_구간_생성_됨(
        ExtractableResponse<Response> response,
        SectionRequest expected,
        long expectedLineId
    ) {

        SectionResponse sectionResponse = response.as(SectionResponse.class);

        Assertions.assertThat(response.statusCode())
                  .isEqualTo(HttpStatus.CREATED.value());

        Assertions.assertThat(sectionResponse.getLine().getId())
                  .isEqualTo(expectedLineId);

        Assertions.assertThat(sectionResponse.getUpStation().getId())
                  .isEqualTo(expected.getUpStationId());

        Assertions.assertThat(sectionResponse.getDownStation().getId())
                  .isEqualTo(expected.getDownStationId());

        Assertions.assertThat(sectionResponse.getDistance())
                  .isEqualTo(expected.getDistance());

        Assertions.assertThat(sectionResponse.getDistance())
                  .isEqualTo(expected.getDistance());
    }

    @DisplayName("지하철 노선에서 구간을 제거하는 테스트: 정상 삭제 테스트")
    @Test
    public void removeSectionTest() {
        // given
        LineResponse lineResponse = 지하철_노선_생성_요청(분당선, "yello").as(LineResponse.class);
        StationResponse stationResponse1 = 지하철역_생성_요청(태평역).as(StationResponse.class);
        StationResponse stationResponse2 = 지하철역_생성_요청(가천대역).as(StationResponse.class);
        StationResponse stationResponse3 = 지하철역_생성_요청(복정역).as(StationResponse.class);

        지하철_구간_생성_요청(
            SectionRequest.of(stationResponse1.getId(), stationResponse2.getId(), 3),
            lineResponse.getId()
        ).as(SectionResponse.class);

        지하철_구간_생성_요청(
            SectionRequest.of(stationResponse2.getId(), stationResponse3.getId(), 4),
            lineResponse.getId()
        ).as(SectionResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_구간_제거_요청(
            lineResponse.getId(),
            stationResponse3.getId()
        );

        // then
        지하철_구간_제거_됨(response);
    }

    private void 지하철_구간_제거_됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode())
            .isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("지하철 노선에서 구간을 제거하는 테스트: 구간이 1개인 경우 역을 삭제할 수 없다.")
    @Test
    public void removeSectionFailTest_01() {
        // given
        LineResponse lineResponse = 지하철_노선_생성_요청(분당선, "yello").as(LineResponse.class);
        StationResponse stationResponse1 = 지하철역_생성_요청(태평역).as(StationResponse.class);
        StationResponse stationResponse2 = 지하철역_생성_요청(가천대역).as(StationResponse.class);

        지하철_구간_생성_요청(
            SectionRequest.of(stationResponse1.getId(), stationResponse2.getId(), 3),
            lineResponse.getId()
        ).as(SectionResponse.class);

        // when
        ExtractableResponse<Response> response = 지하철_구간_제거_요청(
            lineResponse.getId(),
            stationResponse2.getId()
        );

        // then
        지하철_구간_제거_실패_됨(response);
    }

    @DisplayName("지하철 노선에서 구간을 제거하는 테스트: 마지막 역(하행 종점역)만 제거할 수 있다.")
    @Test
    public void removeSectionFailTest_02() {
        // given
        LineResponse lineResponse = 지하철_노선_생성_요청(분당선, "yello").as(LineResponse.class);
        StationResponse stationResponse1 = 지하철역_생성_요청(태평역).as(StationResponse.class);
        StationResponse stationResponse2 = 지하철역_생성_요청(가천대역).as(StationResponse.class);
        StationResponse stationResponse3 = 지하철역_생성_요청(복정역).as(StationResponse.class);

        지하철_구간_생성_요청(
            SectionRequest.of(stationResponse1.getId(), stationResponse2.getId(), 3),
            lineResponse.getId()
        ).as(SectionResponse.class);

        지하철_구간_생성_요청(
            SectionRequest.of(stationResponse2.getId(), stationResponse3.getId(), 4),
            lineResponse.getId()
        ).as(SectionResponse.class);

        // when
        ExtractableResponse<Response> response1 = 지하철_구간_제거_요청(
            lineResponse.getId(),
            stationResponse1.getId()
        );
        ExtractableResponse<Response> response2 = 지하철_구간_제거_요청(
            lineResponse.getId(),
            stationResponse2.getId()
        );

        // then
        지하철_구간_제거_실패_됨(response1);
        지하철_구간_제거_실패_됨(response2);
    }

    private void 지하철_구간_제거_실패_됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode())
            .isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    // TODO: 지하철 노선에 등록된 구간을 통해 역 목록을 조회하는 기능 구현


}
