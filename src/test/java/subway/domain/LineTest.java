package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

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

    @DisplayName("노선을 생성한다.")
    @Test
    void createLine() {

        final Section 첫번째_구간 = new Section(강남역, 양재역, 10);
        final Line 노선_신분당선 = new Line(1L, 신분당선, 빨간색, Sections.from(List.of(첫번째_구간)));

        assertThat(노선_신분당선).isEqualTo(new Line(1L, 신분당선, 빨간색, Sections.from(List.of(첫번째_구간))));
    }

    @DisplayName("노선 정보를 수정한다.")
    @Test
    void updateLine() {

        final Section 첫번째_구간 = new Section(강남역, 양재역, 10);
        final Line 노선_신분당선 = new Line(1L, 신분당선, 빨간색, Sections.from(List.of(첫번째_구간)));
        노선_신분당선.updateLine(이호선, 녹색);

        assertAll(
                () -> assertThat(노선_신분당선.getId()).isEqualTo(1L),
                () -> assertThat(노선_신분당선.getName()).isEqualTo(이호선),
                () -> assertThat(노선_신분당선.getColor()).isEqualTo(녹색)
        );
    }
}
