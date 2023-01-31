package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import subway.common.exception.NoDeleteOneSectionException;
import subway.common.exception.NoRegisterStationException;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static subway.common.error.LineSectionError.*;
import static subway.fixture.TestFixtureLine.노선_등록;

@DisplayName("지하철 노선 구간 도메인 기능 테스트")
class SectionsTest {

    private static final String 신분당선 = "신분당선";
    private static final String 빨간색 = "bg-red-600";
    private static final Station 강남역 = new Station(1L, "강남역");
    private static final Station 양재역 = new Station(2L, "양재역");
    private static final Station 몽촌토성역 = new Station(3L, "몽촌토성역");
    private static final Station 검암역 = new Station(4L, "검암역");
    private static final Station 부평역 = new Station(4L, "부평역");

    @DisplayName("구간을 추가로 등록한다.")
    @Test
    void addSection() {

        final Section 첫번째_구간 = new Section(강남역, 양재역, 10);
        final Line 노선_신분당선 = 노선_등록(1L, 신분당선, 빨간색, 첫번째_구간);
        final Section 두번째_구간 = new Section(양재역, 몽촌토성역, 10);
        final Sections 구간들 = 노선_신분당선.getSections();
        구간들.addSection(노선_신분당선, 두번째_구간);

        assertAll(
                () -> assertThat(구간들.getSections()).hasSize(2),
                () -> assertThat(구간들.getSections().get(1).getUpStation()).isEqualTo(양재역),
                () -> assertThat(구간들.getSections().get(1).getDownStation()).isEqualTo(몽촌토성역)
        );
    }

    @DisplayName("노선 구간 추가 시 요청값의 상행역은 노선에 등록되어 있지 않아서 구간 등록이 불가능하다.")
    @Test
    void error_addSection() {

        final Section 첫번째_구간 = new Section(강남역, 양재역, 10);
        final Line 노선_신분당선 = 노선_등록(1L, 신분당선, 빨간색, 첫번째_구간);

        final Section 두번째_구간 = new Section(검암역, 몽촌토성역, 10);
        final Sections 구간들 = 노선_신분당선.getSections();
        assertThatThrownBy(() -> 구간들.addSection(노선_신분당선, 두번째_구간))
                .isInstanceOf(NoRegisterStationException.class)
                .hasMessage(NO_REGISTER_UP_STATION.getMessage());
    }

    @DisplayName("노선 구간 추가 시 요청값의 상행역이 노선의 하행종점역이 아니라서 구간 등록이 불가능하다.")
    @Test
    void error_addSection_2() {

        final Section 첫번째_구간 = new Section(1L, 강남역, 양재역, 10);
        final Line 노선_신분당선 = 노선_등록(1L, 신분당선, 빨간색, 첫번째_구간);
        final Section 두번째_구간 = new Section(2L, 양재역, 몽촌토성역, 10);
        final Sections 구간들 = 노선_신분당선.getSections();
        구간들.addSection(노선_신분당선, 두번째_구간);

        final Section 세번째_구간 = new Section(3L, 양재역, 검암역, 10);
        assertThatThrownBy(() -> 구간들.addSection(노선_신분당선, 세번째_구간))
                .isInstanceOf(NoRegisterStationException.class)
                .hasMessage(NO_REGISTER_UP_STATION.getMessage());
    }

    @DisplayName("기존 등록된 구간을 삭제한다.")
    @Test
    void removeSection() {

        final Section 첫번째_구간 = new Section(1L, 강남역, 양재역, 10);
        final Line 노선_신분당선 = 노선_등록(1L, 신분당선, 빨간색, 첫번째_구간);
        final Section 두번째_구간 = new Section(2L, 양재역, 몽촌토성역, 10);
        final Sections 구간들 = 노선_신분당선.getSections();
        구간들.addSection(노선_신분당선, 두번째_구간);

        구간들.removeSection(몽촌토성역);

        assertAll(
                () -> assertThat(구간들.getSections()).hasSize(1),
                () -> assertThat(구간들.getSections()).contains(첫번째_구간),
                () -> assertThat(구간들.getSections()).doesNotContain(두번째_구간)
        );
    }

    @DisplayName("노선 구간 제거 시 구간이 하나인 경우 삭제 불가로 구간 삭제가 불가능하다.")
    @Test
    void error_removeSection() {

        final Section 첫번째_구간 = new Section(1L, 강남역, 양재역, 10);
        final Line 노선_신분당선 = 노선_등록(1L, 신분당선, 빨간색, 첫번째_구간);
        final Sections 구간들 = 노선_신분당선.getSections();

        assertThatThrownBy(() -> 구간들.removeSection(양재역))
                .isInstanceOf(NoDeleteOneSectionException.class)
                .hasMessage(NO_DELETE_ONE_SECTION.getMessage());
    }

    @DisplayName("노선 구간 제거 시 등록된 하행 종점역이 아니어서 구간 삭제가 불가능하다.")
    @ParameterizedTest(name = "{0}은 노선의 하행종점역이 아닙니다.")
    @MethodSource("provideDeleteStation")
    void error_removeSection_2(final Station deleteStation) {

        final Section 첫번째_구간 = new Section(1L, 강남역, 양재역, 10);
        final Line 노선_신분당선 = 노선_등록(1L, 신분당선, 빨간색, 첫번째_구간);
        final Section 두번째_구간 = new Section(2L, 양재역, 몽촌토성역, 10);
        final Sections 구간들 = 노선_신분당선.getSections();
        구간들.addSection(노선_신분당선, 두번째_구간);

        assertThatThrownBy(() -> 구간들.removeSection(deleteStation))
                .isInstanceOf(NoRegisterStationException.class)
                .hasMessage(NO_REGISTER_LAST_LINE_STATION.getMessage());
    }

    private static Stream<Arguments> provideDeleteStation() {
        return Stream.of(
                Arguments.of(강남역.getName(), 강남역),
                Arguments.of(양재역.getName(), 양재역)
        );
    }
}