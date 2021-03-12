package nextstep.subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.LineHelper;
import nextstep.subway.section.domain.Section;
import nextstep.subway.section.dto.SectionRequest;
import nextstep.subway.section.dto.SectionResponse;
import nextstep.subway.station.StationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    Long stationId1;
    Long stationId2;
    Long lineId;

    @BeforeEach
    void 미리_역_노선_생성() {
        ExtractableResponse<Response> stationCreateResponse1 = StationHelper.지하철역_생성_요청("강남");
        ExtractableResponse<Response> stationCreateResponse2 = StationHelper.지하철역_생성_요청("역삼");

        stationId1 = StationHelper.생성된_지하철역_ID_가져오기(stationCreateResponse1);
        stationId2 = StationHelper.생성된_지하철역_ID_가져오기(stationCreateResponse2);

        ExtractableResponse<Response> lineCreateResponse = LineHelper.지하철_노선_생성_요청("1호선", "green", stationId1, stationId2, 10);

        lineId = LineHelper.생성된_Entity의_ID_가져오기(lineCreateResponse);
    }

    @DisplayName("구간 추가 기능")
    @Test
    void 구간_추가_및_확인() {
        //given
        SectionRequest sectionRequest = SectionHelper.Section_요청_만들기(stationId1, stationId2, 10);

        //노선이 추가 되어야 하고 id가 있어야 함
        ExtractableResponse<Response> createResponse =
                RestAssured.given().pathParam("lineId", lineId).body(sectionRequest).contentType(MediaType.APPLICATION_JSON_VALUE).log().all()
                        .when().post("/lines/{lineId}/sections")
                        .then().log().all().extract();
        //역 등록, 노선 등록
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("구간 리스트 불러오기")
    @Test
    void 구간_리스트_불러오기() {
        SectionRequest sectionRequest = SectionHelper.Section_요청_만들기(stationId1, stationId2, 10);

        RestAssured.given().pathParam("lineId", lineId).body(sectionRequest).contentType(MediaType.APPLICATION_JSON_VALUE).log().all()
                .when().post("/lines/{lineId}/sections")
                .then().log().all().extract();

        ExtractableResponse getResponse = RestAssured.given()
                .pathParam("lineId", lineId)
                .contentType(MediaType.APPLICATION_JSON_VALUE).log().all()
                .when().get("/lines/{lineId}/sections")
                .then().log().all().extract();

        assertThat(getResponse.jsonPath().getList(".", SectionResponse.class).size()).isEqualTo(1);

    }

}
