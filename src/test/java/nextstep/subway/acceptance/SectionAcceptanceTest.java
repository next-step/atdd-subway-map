package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.LineTestFixtures.노선_생성;
import static nextstep.subway.acceptance.LineTestFixtures.노선_조회;
import static nextstep.subway.acceptance.SectionTestFixtures.구간_생성;
import static nextstep.subway.acceptance.StationTestFixtures.역_생성;
import static org.assertj.core.api.Assertions.*;

@DisplayName("지하철 구간 관리")
public class SectionAcceptanceTest extends AbstractAcceptanceTest{

    /**
     * 지하철 노선에 구간을 등록하는 기능을 구현
     * 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
     * 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
     * 새로운 구간 등록시 위 조건에 부합하지 않는 경우 에러처리한다.
     */
    @DisplayName("지하철 구간 등록")
    @Test
    void createStationSection() {
        String 신림역 = 역_생성("신림역");
        String 당곡역 = 역_생성("당곡역");

        String 노선_아이디 = 노선_생성("신림선", "bg-blue-300", 신림역, 당곡역, "10")
                                    .jsonPath()
                                    .getString("id");

        String 보라매역 = 역_생성("보라매역");

        구간_생성(노선_아이디, 당곡역, 보라매역, "5");
        ExtractableResponse<Response> 노선_조회_결과 = 노선_조회(노선_아이디);

        Assertions.assertAll(() -> {
            assertThat(노선_조회_결과.jsonPath().getList("stations.name").size()).isEqualTo(3);
            assertThat(노선_조회_결과.jsonPath().getList("stations.name")).contains("신림역", "당곡역", "보라매역");
        });
    }
}
