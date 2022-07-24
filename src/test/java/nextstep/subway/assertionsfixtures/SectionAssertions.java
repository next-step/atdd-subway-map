package nextstep.subway.assertionsfixtures;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;

import static nextstep.subway.acceptance.SectionTestFixtures.라인_아이디;
import static org.assertj.core.api.Assertions.assertThat;

public class SectionAssertions {
    public static void 특정_구간_조회_사이즈_검증(ExtractableResponse<Response> 특정_구간_조회, int size) {
        Assertions.assertThat(특정_구간_조회.jsonPath().getList(".").size()).isEqualTo(size);
    }

    public static void 특정_구간_조회_구간_검증(ExtractableResponse<Response> 특정_구간_조회, String... 값) {
        assertThat(특정_구간_조회.jsonPath().getList("upStation.name")).containsExactly(값);
    }

    public static void 특정_구간_조회_라인_아이디_검증(ExtractableResponse<Response> 특정_구간_조회, ExtractableResponse<Response> 라인) {
        String 라인_아이디 = 라인_아이디(라인);
        assertThat(특정_구간_조회.jsonPath().getString("lineId")).contains(라인_아이디);
    }

    public static void 전체_구간_조회_사이즈_검증(ExtractableResponse<Response> 전체_구간_조회, int size) {
        assertThat(전체_구간_조회.jsonPath().getList(".").size()).isEqualTo(3);
    }

    public static void 전체_구간_조회_상행선_구간_검증(ExtractableResponse<Response> 전체_구간_조회, String... 값) {
        assertThat(전체_구간_조회.jsonPath().getList("upStation.name")).containsExactly(값);
    }

    public static void 전체_구간_조회_하행선_구간_검증(ExtractableResponse<Response> 전체_구간_조회, String... 값) {
        assertThat(전체_구간_조회.jsonPath().getList("downStation.name")).containsExactly(값);
    }

    public static void 노선_조회_구간_이름_검증(ExtractableResponse<Response> 노선_조회, String... 값) {
        assertThat(노선_조회.jsonPath().getList("stations.name")).contains("신림역", "당곡역", "보라매역");
    }

    public static void 노선_조회_구간_사이즈_검증(ExtractableResponse<Response> 노선_조회, int size) {
        assertThat(노선_조회.jsonPath().getList("stations.name").size()).isEqualTo(size);
    }
}
