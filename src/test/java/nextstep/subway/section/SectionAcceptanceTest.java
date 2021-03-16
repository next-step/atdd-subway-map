package nextstep.subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.LineHelper;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionResponse;
import nextstep.subway.station.StationHelper;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    Long stationId1;
    Long stationId2;
    Long stationId3;
    Long lineId;

    @BeforeEach
    void 미리_역_노선_생성() {
        ExtractableResponse<Response> stationCreateResponse1 = StationHelper.지하철역_생성_요청("강남");
        ExtractableResponse<Response> stationCreateResponse2 = StationHelper.지하철역_생성_요청("역삼");
        ExtractableResponse<Response> stationCreateResponse3 = StationHelper.지하철역_생성_요청("선릉");

        stationId1 = StationHelper.생성된_지하철역_ID_가져오기(stationCreateResponse1);
        stationId2 = StationHelper.생성된_지하철역_ID_가져오기(stationCreateResponse2);
        stationId3 = StationHelper.생성된_지하철역_ID_가져오기(stationCreateResponse3);

        ExtractableResponse<Response> lineCreateResponse = LineHelper.지하철_노선_생성_요청("1호선", "green", stationId1, stationId2, 10);

        lineId = LineHelper.생성된_Entity의_ID_가져오기(lineCreateResponse);
    }

    @DisplayName("구간 추가 기능")
    @Test
    void 구간_추가_및_확인() {
        ExtractableResponse<Response> createResponse = SectionHelper.구간_추가_요청(lineId, stationId2, stationId3, 10);

        SectionHelper.응답_201_확인(createResponse);

        새로운_구간_노선_확인();
    }

    @DisplayName("하행_종점역이_아닌_다른_역에_구간_추가")
    @Test
    void 하행_종점역이_아닌_다른_역에_구간_추가() {
        //given
        SectionHelper.구간_추가_요청(lineId, stationId1, stationId2, 10);
        ExtractableResponse<Response> createResponse2 = SectionHelper.구간_추가_요청(lineId, stationId1, stationId3, 10);
        SectionHelper.응답_400_확인(createResponse2);
    }

    @DisplayName("추가하려는_상행역이_이미_등록되어_있음")
    @Test
    void 추가하려는_상행역이_이미_등록되어_있음() {
        //given
        SectionHelper.구간_추가_요청(lineId, stationId1, stationId2, 10);
        ExtractableResponse<Response> createResponse2 = SectionHelper.구간_추가_요청(lineId, stationId1, stationId3, 10);
        SectionHelper.응답_400_확인(createResponse2);
    }

    void 새로운_구간_노선_확인(){
        //@OneToMany에서 fetch = FetchType.EAGER 설정을 해줘야 에러가 안남
        //failed to lazily initialize a collection of role could not initialize proxy - no session
        //then
        LineResponse lineResponse = LineHelper.지하철_노선_조회_요청(lineId).jsonPath().getObject(".", LineResponse.class);
        assertThat(lineResponse.getStationResponses().size()).isEqualTo(3);
    }

    @DisplayName("구간이_2개_이상일_때_삭제")
    @Test
    void 구간_삭제(){
        SectionHelper.구간_추가_요청(lineId, stationId2, stationId3, 10);
        ExtractableResponse<Response> deleteResponse = SectionHelper.구간_삭제(lineId, stationId3);

        LineResponse lineResponse = LineHelper.지하철_노선_조회_요청(lineId).jsonPath().getObject(".", LineResponse.class);
        assertThat(lineResponse.getStationResponses().size()).isEqualTo(2);
        SectionHelper.응답_200_확인(deleteResponse);
    }

    @DisplayName("구간이_1개일_때_삭제")
    @Test
    void 구간이_1개일_때_삭제(){
        ExtractableResponse<Response> deleteResponse = SectionHelper.구간_삭제(lineId, stationId2);
        SectionHelper.응답_400_확인(deleteResponse);
    }
}
