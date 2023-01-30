package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static subway.fixture.TestFixtureLine.노선_등록;
import static subway.fixture.TestFixtureLine.노선_추가_등록;

class SectionsTest {

    private static final String 신분당선 = "신분당선";
    private static final String 빨간색 = "bg-red-600";
    private static final Station 강남역 = new Station(1L, "강남역");
    private static final Station 양재역 = new Station(2L, "양재역");
    private static final Station 몽촌토성역 = new Station(3L, "몽촌토성역");

    @DisplayName("구간을 추가로 등록한다.")
    @Test
    void addSection() {

        final Section 첫번째_구간 = new Section(강남역, 양재역, 10);
        final Line 노선_신분당선 = 노선_등록(1L, 신분당선, 빨간색, 첫번째_구간);

        노선_신분당선.addSection(양재역, 몽촌토성역, 10);

        final List<Section> 노선_신분당선_구간_목록 = 노선_신분당선.getSections().getSections();

        assertAll(
                () -> assertThat(노선_신분당선.getSections().getSections()).hasSize(2),
                () -> assertThat(노선_신분당선_구간_목록.get(1).getUpStation()).isEqualTo(양재역),
                () -> assertThat(노선_신분당선_구간_목록.get(1).getDownStation()).isEqualTo(몽촌토성역)
        );
    }

    @DisplayName("기존 등록된 구간을 삭제한다.")
    @Test
    void removeSection() {

        final Section 첫번째_구간 = new Section(1L, 강남역, 양재역, 10);
        final Line 노선_신분당선 = 노선_등록(1L, 신분당선, 빨간색, 첫번째_구간);
        final Section 두번째_구간 = new Section(양재역, 몽촌토성역, 10);
        노선_추가_등록(노선_신분당선, 양재역, 몽촌토성역, 10);

        노선_신분당선.removeSection(몽촌토성역);

        final List<Section> 노선_신분당선_구간_목록 = 노선_신분당선.getSections().getSections();

        assertAll(
                () -> assertThat(노선_신분당선.getSections().getSections()).hasSize(1),
                () -> assertThat(노선_신분당선_구간_목록).contains(첫번째_구간),
                () -> assertThat(노선_신분당선_구간_목록).doesNotContain(두번째_구간)
        );
    }
}