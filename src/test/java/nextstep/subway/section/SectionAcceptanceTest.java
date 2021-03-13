package nextstep.subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.LineHelper;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.section.domain.Section;
import nextstep.subway.section.dto.SectionRequest;
import nextstep.subway.section.dto.SectionResponse;
import nextstep.subway.station.StationHelper;
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

    @Autowired
    LineService lineService;

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
        ExtractableResponse<Response> createResponse = SectionHelper.구간_추가_요청(lineId, stationId1, stationId2, 10);

        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        새로운_구간_노선_확인();
    }

    @DisplayName("하행_종점역이_아닌_다른_역에_구간_추가")
    @Test
    void 하행_종점역이_아닌_다른_역에_구간_추가() {
        //given
        SectionHelper.구간_추가_요청(lineId, stationId1, stationId2, 10);
        ExtractableResponse<Response> createResponse2 = SectionHelper.구간_추가_요청(lineId, stationId1, stationId3, 10);

        assertThat(createResponse2.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("추가하려는_상행역이_이미_등록되어_있음")
    @Test
    void 추가하려는_상행역이_이미_등록되어_있음() {
        //given
        SectionHelper.구간_추가_요청(lineId, stationId1, stationId2, 10);
        ExtractableResponse<Response> createResponse2 = SectionHelper.구간_추가_요청(lineId, stationId1, stationId3, 10);

        assertThat(createResponse2.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    void 새로운_구간_노선_확인(){
        //@OneToMany에서 fetch = FetchType.EAGER 설정을 해줘야 에러가 안남
        //failed to lazily initialize a collection of role could not initialize proxy - no session
        //then
        LineResponse lineResponse = LineHelper.지하철_노선_조회_요청(lineId).jsonPath().getObject(".", LineResponse.class);
        Line line = lineService.getLineById(lineId);
        assertThat(line.getSections().size()).isEqualTo(1);
    }

    @DisplayName("구간 리스트 불러오기")
    @Test
    void 구간_리스트_불러오기() {
        SectionHelper.구간_추가_요청(lineId, stationId1, stationId2, 10);
        SectionHelper.구간_추가_요청(lineId, stationId2, stationId3, 10);

        ExtractableResponse getResponse = RestAssured.given()
                .pathParam("lineId", lineId)
                .contentType(MediaType.APPLICATION_JSON_VALUE).log().all()
                .when().get("/lines/{lineId}/sections")
                .then().log().all().extract();
        List<SectionResponse> list = getResponse.jsonPath().getList(".", SectionResponse.class);
        assertThat(getResponse.jsonPath().getList(".", SectionResponse.class).size()).isEqualTo(2);
    }

    //TODO : 삭제 시 삭제 쿼리 날라가지 않음
    @DisplayName("구간이_2개_이상일_때_삭제")
    @Test
    void 구간_삭제(){
        SectionHelper.구간_추가_요청(lineId, stationId1, stationId2, 10);
        SectionHelper.구간_추가_요청(lineId, stationId2, stationId3, 10);

        ExtractableResponse<Response> deleteResponse = RestAssured.given()
                .pathParams("lineId", lineId, "stationId", stationId3)
                .log().all()
                .when().delete("/lines/{lineId}/sections?stationId={stationId}")
                .then().log().all().extract();

        LineResponse lineResponse = LineHelper.지하철_노선_조회_요청(lineId).jsonPath().getObject(".", LineResponse.class);
        Line line = lineService.getLineById(lineId);
        assertThat(line.getSections().size()).isEqualTo(1);
    }

    @DisplayName("구간이_1개일_때_삭제")
    @Test
    void 구간이_1개일_때_삭제(){
        SectionHelper.구간_추가_요청(lineId, stationId1, stationId2, 10);

        ExtractableResponse<Response> deleteResponse = RestAssured.given()
                .pathParams("lineId", lineId, "stationId", stationId2)
                .log().all()
                .when().delete("/lines/{lineId}/sections/{stationId}")
                .then().log().all().extract();

        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
