package nextstep.subway.acceptance;


import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.api.LineApiCall;
import nextstep.subway.api.StationApiCall;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.util.DatabaseUitl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선 구간 관리")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SectionAcceptanceTest {
    @LocalServerPort
    int port;

    @Autowired
    DatabaseUitl databaseUitl;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseUitl.tableClear();
    }

    /**
     * Given 지하철 노선을 등록하고
     * When 노선에 구간을 등록하면
     * Then 지하철 구간 조회 시 생성한 구간을 찾을 수 있다.
     */
    @DisplayName("지하철 구간을 노선에 등록한다.")
    @Test
    void registerSection() {
        // given
        final String 구일 = "구일역";
        final String 구로 = "구로역";
        final String 신도림 = "신도림역";
        final String 영등포 = "영등포역";

        final String 일호선 = "1호선";

        List<ExtractableResponse<Response>> stationCreationResponses = StationApiCall.createStations(구일, 구로, 신도림, 영등포);
        Long 구일_아이디 = getId(stationCreationResponses.get(0));
        Long 구로_아이디 = getId(stationCreationResponses.get(1));
        Long 신도림_아이디 = getId(stationCreationResponses.get(2));
        Long 영등포_아이디 = getId(stationCreationResponses.get(3));

        int 구일_구로_거리 = 10;
        int 구로_신도림_거리 = 15;
        int 신도림_영등포_거리 = 13;

        ExtractableResponse<Response> lineCreationResponse = LineApiCall.createLine(new LineRequest(일호선, "bg-blue-600",구일_아이디, 구로_아이디, 구일_구로_거리));

        Long 일호선_아이디 = lineCreationResponse.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> sectionRegistrationResponse = LineApiCall.registerSection(일호선_아이디, new SectionRequest(구로_아이디, 신도림_아이디, 구로_신도림_거리));
        LineApiCall.registerSection(일호선_아이디, new SectionRequest(신도림_아이디, 영등포_아이디, 신도림_영등포_거리));

        // then
        ExtractableResponse<Response> getLineResponse = LineApiCall.getLine(일호선_아이디);
        List<String> stationNames = getLineResponse.jsonPath().getList("stations.name", String.class);

        assertThat(sectionRegistrationResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(stationNames).containsExactly("구일역", "구로역", "신도림역", "영등포역");

    }

    /**
     * Given 지하철 노선을 등록하고
     * When 새로운 구간 등록 시 해당 구간의 상행역이 하행종점역이 아닐 때
     * Then 예외가 발생한다.
     */
    @DisplayName("새로운 구간 등록 시 상행역이 하행종점역이 아닐 때 발생하는 예외 테스트")
    @Test
    void 새로운구간의_상행역이_하행종점역이_아님() {
        // given
        final String 구일 = "구일역";
        final String 구로 = "구로역";
        final String 신도림 = "신도림역";
        final String 영등포 = "영등포역";

        final String 일호선 = "1호선";

        List<ExtractableResponse<Response>> stationCreationResponses = StationApiCall.createStations(구일, 구로, 신도림, 영등포);

        Long 구일_아이디 = getId(stationCreationResponses.get(0));
        Long 구로_아이디 = getId(stationCreationResponses.get(1));
        Long 신도림_아이디 = getId(stationCreationResponses.get(2));
        Long 영등포_아이디 = getId(stationCreationResponses.get(3));

        int 구일_구로_거리 = 10;
        int 신도림_영등포_거리 = 15;

        ExtractableResponse<Response> lineCreationResponse = LineApiCall.createLine(new LineRequest(일호선, "bg-blue-600",구일_아이디, 구로_아이디, 구일_구로_거리));
        final Long 일호선_아이디 = lineCreationResponse.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> sectionRegistrationResponse = LineApiCall.registerSection(일호선_아이디, new SectionRequest(신도림_아이디, 영등포_아이디, 신도림_영등포_거리));

        // then
        assertThat(sectionRegistrationResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Given 지하철 노선을 등록하고
     * When 새로운 구간 등록 시 상행종점역이 기존 구간에 존재하면
     * Then 예외가 발생한다
     */
    @DisplayName("새로운 구간의 상행종점역이 기존 구간에 존재할 때 발생하는 예외 테스트")
    @Test
    void 새로운구간의_하행역이_기존구간에_존재함() {
        // given
        final String 구일 = "구일역";
        final String 구로 = "구로역";
        final String 신도림 = "신도림역";
        final String 영등포 = "영등포역";

        final String 일호선 = "1호선";

        List<ExtractableResponse<Response>> stationCreationResponses = StationApiCall.createStations(구일, 구로, 신도림, 영등포);
        Long 구일_아이디 = getId(stationCreationResponses.get(0));
        Long 구로_아이디 = getId(stationCreationResponses.get(1));

        int 구일_구로_거리 = 10;
        int 신도림_영등포_거리 = 15;

        ExtractableResponse<Response> lineCreationResponse = LineApiCall.createLine(new LineRequest(일호선, "bg-blue-600",구일_아이디, 구로_아이디, 구일_구로_거리));
        final Long 일호선_아이디 = lineCreationResponse.jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> sectionRegistrationResponse = LineApiCall.registerSection(일호선_아이디, new SectionRequest(구로_아이디, 구일_아이디, 신도림_영등포_거리));

        // then
        assertThat(sectionRegistrationResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


    /**
     * given 지하철 노선 및 구간을 등록하고
     * When 노선에 등록된 구간을 삭제하면
     * Then 지하철 노선 조회 시 해당 구간을 찾을 수 없다.
     */
    @DisplayName("노선에서 지하철 구간을 삭제한다.")
    @Test
    void deleteSection() {
        // given
        final String 구일 = "구일역";
        final String 구로 = "구로역";
        final String 신도림 = "신도림역";
        final String 영등포 = "영등포역";

        final String 일호선 = "1호선";

        List<ExtractableResponse<Response>> stationCreationResponses = StationApiCall.createStations(구일, 구로, 신도림, 영등포);
        Long 구일_아이디 = getId(stationCreationResponses.get(0));
        Long 구로_아이디 = getId(stationCreationResponses.get(1));
        Long 신도림_아이디 = getId(stationCreationResponses.get(2));
        Long 영등포_아이디 = getId(stationCreationResponses.get(3));

        int 구일_구로_거리 = 10;
        int 구로_신도림역_거리 = 15;
        int 신도림역_영등포역_거리 = 13;

        ExtractableResponse<Response> lineCreationResponse = LineApiCall.createLine(new LineRequest(일호선, "bg-blue-600",구일_아이디, 구로_아이디, 구일_구로_거리));

        final Long 일호선_아이디 = lineCreationResponse.jsonPath().getLong("id");

        LineApiCall.registerSection(일호선_아이디, new SectionRequest(구로_아이디, 신도림_아이디, 구로_신도림역_거리));
        LineApiCall.registerSection(일호선_아이디, new SectionRequest(신도림_아이디, 영등포_아이디, 신도림역_영등포역_거리));

        // when
        ExtractableResponse<Response> deleteSectionResponse = LineApiCall.deleteSection(일호선_아이디, new SectionRequest(신도림_아이디, 영등포_아이디));

        // then
        ExtractableResponse<Response> getLineResponse = LineApiCall.getLine(일호선_아이디);
        List<String> stationNames = getLineResponse.jsonPath().getList("stations.name", String.class);

        assertThat(deleteSectionResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        assertThat(stationNames).doesNotContain("영등포역");

    }


    /**
     * Given 지하철 노선 및 구간을 등록하고
     * When 노선에 등록된 구간 중 마지막 구간이 아닌 것을 삭제 시
     * Then 예외가 발생한다.
     */
    @DisplayName("노선에서 마지막 구간이 아닌 구간을 삭제한다.")
    @Test
    void 구간삭제시_마지막구간이_아닐때() {
        // given
        final String 구일 = "구일역";
        final String 구로 = "구로역";
        final String 신도림 = "신도림역";
        final String 영등포 = "영등포역";

        final String 일호선 = "1호선";

        List<ExtractableResponse<Response>> stationCreationResponses = StationApiCall.createStations(구일, 구로, 신도림, 영등포);
        Long 구일_아이디 = getId(stationCreationResponses.get(0));
        Long 구로_아이디 = getId(stationCreationResponses.get(1));
        Long 신도림_아이디 = getId(stationCreationResponses.get(2));
        Long 영등포_아이디 = getId(stationCreationResponses.get(3));

        int 구일_구로_거리 = 10;
        int 구로_신도림_거리 = 15;
        int 신도림_영등포_거리 = 13;

        ExtractableResponse<Response> lineCreationResponse = LineApiCall.createLine(new LineRequest(일호선, "bg-blue-600",구일_아이디, 구로_아이디, 구일_구로_거리));

        final Long 일호선_아이디 = lineCreationResponse.jsonPath().getLong("id");

        LineApiCall.registerSection(일호선_아이디, new SectionRequest(구로_아이디, 신도림_아이디, 구로_신도림_거리));
        LineApiCall.registerSection(일호선_아이디, new SectionRequest(신도림_아이디, 영등포_아이디, 신도림_영등포_거리));

        // when
        ExtractableResponse<Response> deleteSectionResponse = LineApiCall.deleteSection(일호선_아이디, new SectionRequest(구일_아이디, 구로_아이디));

        // then
        assertThat(deleteSectionResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());

    }


    /**
     * Given 지하철 구간 및 노선을 등록하고
     * When 구간이 하나밖에 없는 노선에서 구간을 삭제하면
     * Then 예외가 발생한다.
     */
    @DisplayName("구간이 하나뿐인 노선에서 구간을 삭제한다.")
    @Test
    void 구간이_하나남았을때_삭제() {
        // given
        final String 구일 = "구일역";
        final String 구로 = "구로역";
        final String 신도림 = "신도림역";
        final String 영등포 = "영등포역";

        final String 일호선 = "1호선";

        List<ExtractableResponse<Response>> stationCreationResponses = StationApiCall.createStations(구일, 구로, 신도림, 영등포);
        Long 구일_아이디 = getId(stationCreationResponses.get(0));
        Long 구로_아이디 = getId(stationCreationResponses.get(1));
        Long 신도림_아이디 = getId(stationCreationResponses.get(2));

        int 구일_구로_거리 = 10;
        int 구로_신도림_거리 = 15;

        ExtractableResponse<Response> lineCreationResponse = LineApiCall.createLine(new LineRequest(일호선, "bg-blue-600",구일_아이디, 구로_아이디, 구일_구로_거리));

        final Long 일호선_아이디 = lineCreationResponse.jsonPath().getLong("id");

        LineApiCall.registerSection(일호선_아이디, new SectionRequest(구로_아이디, 신도림_아이디, 구로_신도림_거리));

        // when
        LineApiCall.deleteSection(일호선_아이디, new SectionRequest(구로_아이디, 신도림_아이디));
        ExtractableResponse<Response> deleteSectionResponse = LineApiCall.deleteSection(일호선_아이디, new SectionRequest(구일_아이디, 구로_아이디));

        // then
        assertThat(deleteSectionResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


    // 응답객체 id 데이터 조회
    private Long getId(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }

}
