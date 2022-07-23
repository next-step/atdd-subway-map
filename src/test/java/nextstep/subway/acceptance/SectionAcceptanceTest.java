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

import java.util.List;

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
     * When 지하철 노선에 구간을 등록하면
     * Then 지하철 구간 조회 시 생성한 구간을 찾을 수 있다.
     */
    @DisplayName("지하철 구간을 노선에 등록한다.")
    @Test
    void registerSection() {
        final String 구일역 = "구일역";
        final String 구로역 = "구로역";
        final String 신도림역 = "신도림역";

        final String 일호선 = "1호선";

        List<ExtractableResponse<Response>> stationCreationResponses = StationApiCall.createStations(구일역, 구로역, 신도림역);
        Long 구일역_아이디 = getId(stationCreationResponses.get(0));
        Long 구로역_아이디 = getId(stationCreationResponses.get(1));
        int 구일_구로_거리 = 10;

        ExtractableResponse<Response> lineCreationResponse = LineApiCall.createLine(new LineRequest(일호선, "bg-blue-600",구일역_아이디, 구로역_아이디, 구일_구로_거리));

        final Long 일호선_아이디 = lineCreationResponse.jsonPath().getLong("id");

        int 구로_신도림_거리 = 15;
        Long 신도림역_아이디 = getId(stationCreationResponses.get(2));

        ExtractableResponse<Response> sectionRegistrationResponse = LineApiCall.registerSection(일호선_아이디, new SectionRequest(구로역_아이디, 신도림역_아이디, 구로_신도림_거리));
        assertThat(sectionRegistrationResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
//
        ExtractableResponse<Response> getLineResponse = LineApiCall.getLine(일호선_아이디);
        List<String> stationNames = getLineResponse.jsonPath().getList("stations.name", String.class);

        assertThat(stationNames.get(0)).isEqualTo("구일역");
        assertThat(stationNames.get(1)).isEqualTo("구로역");
        assertThat(stationNames.get(2)).isEqualTo("신도림역");
    }

    // 응답객체 id 데이터 조회
    private Long getId(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }


}
