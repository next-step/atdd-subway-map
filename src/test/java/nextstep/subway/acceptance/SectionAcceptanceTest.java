package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.tool.TestObjectDestroyer;
import nextstep.subway.applicaion.section.dto.SectionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.tool.RequestTool.*;
import static nextstep.subway.acceptance.tool.SubwayFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private TestObjectDestroyer testObjectDestroyer;

    @BeforeEach
    public void beforeEach() {
        RestAssured.port = port;
        testObjectDestroyer.destroyAll();
    }

    /**
     * given: 노선 추가
     * when: 구간 등록
     * then: 해당 구간이 추가됨
     */
    @Test
    void 구간_추가() {
        //given
        Long 양재역 = 역_생성("양재역").jsonPath().getLong("id");
        Long 양재시민의숲역 = 역_생성("양재시민의숲역").jsonPath().getLong("id");
        Long 청계산입구역 = 역_생성("청계산입구역").jsonPath().getLong("id");
        String color = "bg-red-600";

        long 신분당선 = 노선_생성("신분당선", color, 양재역, 양재시민의숲역, 10).jsonPath().getLong("id");
        SectionRequest sectionRequest = new SectionRequest(양재시민의숲역, 청계산입구역, 10);

        //when
        ExtractableResponse<Response> response = post("/lines/" + 신분당선 + "/sections", sectionRequest);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        ExtractableResponse<Response> lineResponse = get("/lines/" + 신분당선);
        assertThat(lineResponse.jsonPath().getList("stations.id")).contains(양재역.intValue(), 양재시민의숲역.intValue(), 청계산입구역.intValue());
    }

    /**
     * when: 요청하는 상행역이 하행 종점역이 아닌 구간 등록 요청
     * then: 400에러 발생
     */
    @Test
    void 실패_400_요청하는_상행역이_현재_하행_종점이_아님_지하철구간_추가() {
        //given
        Long 양재역 = 역_생성("양재역").jsonPath().getLong("id");
        Long 양재시민의숲역 = 역_생성("양재시민의숲역").jsonPath().getLong("id");
        Long 청계산입구역 = 역_생성("청계산입구역").jsonPath().getLong("id");
        String color = "bg-red-600";

        long 신분당선 = 노선_생성("신분당선", color, 양재역, 양재시민의숲역, 10).jsonPath().getLong("id");
        SectionRequest sectionRequest = new SectionRequest(양재역, 청계산입구역, 10);

        //when
        ExtractableResponse<Response> response = post("/lines/" + 신분당선 + "/sections", sectionRequest);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * when: 요청하는 하행역이 이미 존재하는 역인 구간 등록 요청
     * then: 400에러 발생
     */
    @Test
    void 실패_400_요청하는_하행역이_이미_존재_지하철구간_추가() {
        //given
        Long 양재역 = 역_생성("양재역").jsonPath().getLong("id");
        Long 양재시민의숲역 = 역_생성("양재시민의숲역").jsonPath().getLong("id");
        String color = "bg-red-600";

        long 신분당선 = 노선_생성("신분당선", color, 양재역, 양재시민의숲역, 10).jsonPath().getLong("id");
        SectionRequest sectionRequest = new SectionRequest(양재시민의숲역, 양재역, 10);

        //when
        ExtractableResponse<Response> response = post("/lines/" + 신분당선 + "/sections", sectionRequest);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * given: 구간이 2개 있는 노선 추가
     * when: 구간 제거
     * then: 해당 구간이 제거됨
     */
    @Test
    void 구간_삭제() {
        //given
        Long 양재역 = 역_생성("양재역").jsonPath().getLong("id");
        Long 양재시민의숲역 = 역_생성("양재시민의숲역").jsonPath().getLong("id");
        Long 청계산입구역 = 역_생성("청계산입구역").jsonPath().getLong("id");
        String color = "bg-red-600";
        Long 신분당선 = 노선_생성("신분당선", color, 양재역, 양재시민의숲역, 10).jsonPath().getLong("id");
        구간_생성(신분당선, 양재시민의숲역, 청계산입구역, 10);

        //when
        ExtractableResponse<Response> response = delete("/lines/" + 신분당선 + "/sections?stationId=" + 청계산입구역);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        ExtractableResponse<Response> lineResponse = get("/lines/" + 신분당선);
        assertThat(lineResponse.jsonPath().getList("stations.id")).doesNotContain(청계산입구역.intValue());
        assertThat(lineResponse.jsonPath().getList("stations.id")).hasSize(2);
    }

    /**
     * given: 구간이 1개 있는 노선 추가
     * when: 구간 제거
     * then: 400에러 발생
     */
    @Test
    void 실패_현재구간하나여서_삭제불가() {
        //given
        Long 양재역 = 역_생성("양재역").jsonPath().getLong("id");
        Long 양재시민의숲역 = 역_생성("양재시민의숲역").jsonPath().getLong("id");
        String color = "bg-red-600";
        Long 신분당선 = 노선_생성("신분당선", color, 양재역, 양재시민의숲역, 10).jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> response = delete("/lines/" + 신분당선 + "/sections?stationId=" + 양재시민의숲역);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * given: 구간이 3개 있는 노선 추가
     * when: 맨 앞 구간 제거
     * then: 400에러 발생
     */
    @Test
    void 실패_시작구간은_제거불가() {
        //given
        Long 양재역 = 역_생성("양재역").jsonPath().getLong("id");
        Long 양재시민의숲역 = 역_생성("양재시민의숲역").jsonPath().getLong("id");
        Long 청계산입구역 = 역_생성("청계산입구역").jsonPath().getLong("id");
        String color = "bg-red-600";
        Long 신분당선 = 노선_생성("신분당선", color, 양재역, 양재시민의숲역, 10).jsonPath().getLong("id");
        구간_생성(신분당선, 양재시민의숲역, 청계산입구역, 10);

        //when
        ExtractableResponse<Response> response = delete("/lines/" + 신분당선 + "/sections?stationId=" + 양재역);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}