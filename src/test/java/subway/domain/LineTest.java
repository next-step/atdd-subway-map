package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 도메인 기능 테스트")
class LineTest {

    private static final String 신분당선 = "신분당선";
    private static final String 이호선 = "2호선";
    private static final String 빨간색 = "bg-red-600";
    private static final Station 강남역 = new Station("강남역");
    private static final Station 양재역 = new Station("양재역");
    private static final String 녹색 = "bg-green-600";

    @DisplayName("호선을 생성한다.")
    @Test
    void createLine() {

        final Line 노선_신분당선 = new Line(1L, 신분당선, 빨간색, 강남역, 양재역, 10);

        assertThat(노선_신분당선).isEqualTo(new Line(1L, 신분당선, 빨간색, 강남역, 양재역, 10));
    }

    @DisplayName("호선 정보를 수정한다.")
    @Test
    void updateLine() {

        final Line 노선_신분당선 = new Line(1L, 신분당선, 빨간색, 강남역, 양재역, 10);
        노선_신분당선.updateLine(이호선, 녹색);

        assertAll(
                () -> assertThat(노선_신분당선.getId()).isEqualTo(1L),
                () -> assertThat(노선_신분당선.getName()).isEqualTo(이호선),
                () -> assertThat(노선_신분당선.getColor()).isEqualTo(녹색)
        );
    }
}
