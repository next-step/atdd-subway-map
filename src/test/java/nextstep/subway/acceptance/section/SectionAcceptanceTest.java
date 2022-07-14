package nextstep.subway.acceptance.section;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.util.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.line.LineStationAssuredTemplate.노선_생성;
import static nextstep.subway.acceptance.line.LineStationAssuredTemplate.노선_조회;
import static nextstep.subway.acceptance.section.SectionAcceptanceTemplate.구간_등록;
import static nextstep.subway.acceptance.section.SectionAcceptanceTemplate.구간_제거;
import static nextstep.subway.acceptance.station.StationRestAssuredTemplate.지하철역_생성;
import static org.assertj.core.api.Assertions.assertThat;

public class SectionAcceptanceTest extends AcceptanceTest {
    private static final String 신분당선 = "신분당선";
    private static final String DOWN_STATION_ID = "stations.stations.downStationId[-1]";
    private static final String ID = "id";

    /**
     * Given : 지하철 노선을 생성하고
     * When : 구간을 등록하면
     * Then : 노선에서 등록한 구간을 조회할 수 있다.
     */
    @Test
    @DisplayName("구간을 등록한다.")
    void createdSection() {
        // given 지하철 노선을 생성하고
        ExtractableResponse<Response> 노선_생성 = 노선_생성(신분당선, "bg-red-600", "강남역", "분당역", 10);
        assertThat(노선_생성.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when 구간을 등록하면
        String 신분당선_ID = 노선_정보_조회(노선_생성, ID);
        String 기존_하행종점역_ID = 노선_정보_조회(노선_생성, DOWN_STATION_ID);
        String 등록하는_하행역_ID = 역_ID_조회("판교역");

        ExtractableResponse<Response> 구간_등록 = 구간_등록(신분당선_ID, 등록하는_하행역_ID, 기존_하행종점역_ID, 10);
        assertThat(구간_등록.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then 노선에서 등록한 구간을 조회할 수 있다.
        ExtractableResponse<Response> 노선_조회 = 노선_조회(신분당선_ID);
        assertThat(노선_조회.statusCode()).isEqualTo(HttpStatus.OK.value());

        assertThat(등록하는_하행역_ID)
            .isEqualTo(노선_정보_조회(노선_조회, DOWN_STATION_ID));
    }


    /**
     * Given : 지하철 노선을 생성하고
     * When : 구간을 등록한 후
     * When : 등록한 구간을 제거하면
     * Then : 하행 종점역이 변경된다.
     */
    @Test
    @DisplayName("구간을 제거한다.")
    void deletedSection() {
        // given 지하철 노선을 생성하고
        ExtractableResponse<Response> 노선_생성 = 노선_생성(신분당선, "bg-red-600", "강남역", "분당역", 10);
        assertThat(노선_생성.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when 구간을 등록한 후
        String 신분당선_ID = 노선_정보_조회(노선_생성, ID);
        String 기존_하행종점역_ID = 노선_정보_조회(노선_생성, DOWN_STATION_ID);
        String 등록하는_하행역_ID = 역_ID_조회("판교역");

        ExtractableResponse<Response> 구간_등록 = 구간_등록(신분당선_ID, 등록하는_하행역_ID, 기존_하행종점역_ID, 10);
        assertThat(구간_등록.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when 등록한 구간을 제거하면
        ExtractableResponse<Response> 구간_제거 = 구간_제거(신분당선_ID, 등록하는_하행역_ID);
        assertThat(구간_제거.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then 하행 종점역이 변경된다.
        ExtractableResponse<Response> 노선_조회 = 노선_조회(신분당선_ID);
        assertThat(노선_조회.statusCode()).isEqualTo(HttpStatus.OK.value());

        assertThat(등록하는_하행역_ID)
            .isNotEqualTo(노선_정보_조회(노선_조회, DOWN_STATION_ID));
    }


    /**
     * Given : 지하철 노선을 생성하고
     * When & Then : 노선 내부에 있는 역을 하행역으로 등록할 때 등록 실패한다.
     */
    @Test
    @DisplayName("기존 역을 등록하면 구간 등록이 실패된다.")
    void failedCreatedSection() {
        // given 지하철 노선을 생성하고
        ExtractableResponse<Response> 노선_생성 = 노선_생성(신분당선, "bg-red-600", "강남역", "분당역", 10);
        assertThat(노선_생성.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when & then 노선 내부에 있는 역을 하행역으로 등록할 때 등록 실패한다.
        String 신분당선_ID = 노선_정보_조회(노선_생성, ID);
        String 기존_하행종점역_ID = 노선_정보_조회(노선_생성, DOWN_STATION_ID);
        String 기존_상행종점역_ID = 노선_정보_조회(노선_생성, DOWN_STATION_ID);

        String 등록하는_하행역_ID = 기존_상행종점역_ID;

        ExtractableResponse<Response> 구간_등록 = 구간_등록(신분당선_ID, 등록하는_하행역_ID, 기존_하행종점역_ID, 10);
        assertThat(구간_등록.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given : 지하철 노선을 생성하고
     * When : 구간을 등록한 후
     * When & Then : 하행 종점역이 아닌 다른 역을 제거하려 하면 제거 실패한다.
     */
    @Test
    @DisplayName("중간 역을 제거하려하면 실패한다.")
    void failedDeletedSection() {
        // given 지하철 노선을 생성하고
        ExtractableResponse<Response> 노선_생성 = 노선_생성(신분당선, "bg-red-600", "강남역", "분당역", 10);
        assertThat(노선_생성.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when 구간을 등록한 후
        String 신분당선_ID = 노선_정보_조회(노선_생성, ID);
        String 기존_하행종점역_ID = 노선_정보_조회(노선_생성, DOWN_STATION_ID);

        String 등록하는_하행역_ID = 역_ID_조회("판교역");

        ExtractableResponse<Response> 구간_등록 = 구간_등록(신분당선_ID, 등록하는_하행역_ID, 기존_하행종점역_ID, 10);
        assertThat(구간_등록.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when & then 하행 종점역이 아닌 다른 역을 제거하려 하면 제거 실패한다.
        ExtractableResponse<Response> 구간_제거 = 구간_제거(신분당선_ID, 기존_하행종점역_ID);
        assertThat(구간_제거.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    private String 노선_정보_조회(ExtractableResponse<Response> 노선_생성, String data) {
        return 노선_생성.jsonPath().getString(data);
    }

    private String 역_ID_조회(String stationName) {
        ExtractableResponse<Response> 지하철역_생성 = 지하철역_생성(stationName);
        return 노선_정보_조회(지하철역_생성, "id");
    }
}
