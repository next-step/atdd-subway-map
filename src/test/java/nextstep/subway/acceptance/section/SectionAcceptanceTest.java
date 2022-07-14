package nextstep.subway.acceptance.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.util.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.line.LineStationAssuredTemplate.노선_생성;
import static nextstep.subway.acceptance.line.LineStationAssuredTemplate.노선_조회;
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
        // given : 지하철 노선을 생성하고
        ExtractableResponse<Response> 노선_생성 = 노선_생성(신분당선, "bg-red-600", "강남역", "분당역", 10);
        assertThat(노선_생성.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // when 구간을 등록하면
        String 신분당선_ID = 노선_생성.jsonPath().getString(ID);
        String 기존_하행종점역_ID = 노선_생성.jsonPath().getString(DOWN_STATION_ID);
        String 등록하는_하행종점역_ID = "4";

        ExtractableResponse<Response> 구간_등록 = SectionAcceptanceTemplate.구간_등록(신분당선_ID, 등록하는_하행종점역_ID, 기존_하행종점역_ID, 10);
        assertThat(구간_등록.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then 노선에서 등록한 구간을 조회할 수 있다.
        ExtractableResponse<Response> 노선_조회 = 노선_조회(신분당선_ID);
        assertThat(노선_조회.statusCode()).isEqualTo(HttpStatus.OK.value());

        assertThat(등록하는_하행종점역_ID)
            .isEqualTo(노선_조회.jsonPath().getString(DOWN_STATION_ID));
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

    }

    /**
     * Given : 지하철 노선을 생성하고
     * When : 노선 내부에 있는 역을 하행역으로 등록할 때
     * Then : 등록 실패한다.
     */
    @Test
    @DisplayName("기존 역을 등록하면 구간 등록이 실패된다.")
    void failedCreatedSection() {

    }

    /**
     * Given : 지하철 노선을 생성하고
     * When : 구간을 등록한 후
     * When : 하행 종점역이 아닌 다른 역을 제거하려 하면
     * Then : 제거 실패한다.
     */
    @Test
    @DisplayName("중간 역을 제거하려하면 실패한다.")
    void failedDeletedSection() {

    }
}
