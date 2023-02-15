package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.exception.SectionConstraintException;
import subway.exception.StationNotFoundException;

@DisplayName("구간 목록 관련 기능")
class SectionsTest {

    private Station 강남역;
    private Station 역삼역;
    private Station 정자역;
    private Line line;
    private Section 역삼역_정자역;
    private Section 강남역_역삼역;

    @BeforeEach
    void setUp() {
        this.강남역 = new Station("강남역");
        this.역삼역 = new Station("역삼역");
        this.정자역 = new Station("정자역");
        this.line = new Line("2호선", "bg-red-500", 강남역, 역삼역, 10);
        this.역삼역_정자역 = new Section(10, 역삼역, 정자역, line);
        this.강남역_역삼역 = new Section(10, 강남역, 역삼역, line);
    }

    @DisplayName("구간을 추가한다.")
    @Test
    void add() {
        Sections sections = new Sections(역삼역_정자역);

        assertThat(sections.getSections()).contains(역삼역_정자역);
    }

    @DisplayName("구간 목록을 가져온다.")
    @Test
    void get() {
        Sections sections = new Sections(역삼역_정자역);

        assertThat(sections.getSections()).hasSize(1).contains(역삼역_정자역);
    }

    @DisplayName("구간 제거 관련 기능")
    @Nested
    class DeleteSection {
        @DisplayName("구간을 제거한다.")
        @Test
        void remove() {
            Sections sections = new Sections(강남역_역삼역);
            sections.add(역삼역_정자역);

            sections.remove(역삼역_정자역);

            assertThat(sections.getSections()).doesNotContain(역삼역_정자역);
        }

        @DisplayName("구간목록의 크기가 최소일때 제거를 요청할 경우 에러 처리한다.")
        @Test
        void removeSizeMin() {
            Sections sections = new Sections(역삼역_정자역);

            assertThatThrownBy(() -> sections.remove(역삼역_정자역)).isInstanceOf(SectionConstraintException.class);
        }

        @DisplayName("구간목록의 크기가 최소일때 제거를 요청할 경우 에러 처리한다.")
        @Test
        void removeSizeMinByStation() {
            Sections sections = new Sections(역삼역_정자역);

            assertThatThrownBy(() -> sections.deleteBy(정자역)).isInstanceOf(SectionConstraintException.class);
        }

        @DisplayName("구간목록에 포함되지 않은 역으로 제거를 요청할 경우 에러 처리한다.")
        @Test
        void removeNotExistsStation() {
            Sections sections = new Sections(강남역_역삼역);
            sections.add(역삼역_정자역);
            Station station = new Station("새로운역");

            assertThatThrownBy(() -> sections.deleteBy(station)).isInstanceOf(SectionConstraintException.class);
        }
    }

    @DisplayName("구간 목록 종점 관련 기능")
    @Nested
    class GetLineDownStation {
        @DisplayName("구간 목록의 상행역을 가져온다.")
        @Test
        void getUpStation() {
            Sections sections = new Sections();
            sections.add(강남역_역삼역);
            sections.add(역삼역_정자역);

            Station actual = sections.getLineUpStation();

            Assertions.assertAll(
                    () -> assertThat(actual).isEqualTo(강남역),
                    () -> assertThat(actual).isNotEqualTo(역삼역),
                    () -> assertThat(actual).isNotEqualTo(정자역)
            );
        }

        @DisplayName("빈 구간 목록의 상행역을 가져올 경우 에러 처리한다.")
        @Test
        void getUpStationEmptySections() {
            Sections sections = new Sections();

            assertThatThrownBy(() -> sections.getLineUpStation()).isInstanceOf(StationNotFoundException.class);
        }

        @DisplayName("구간 목록의 하행역을 가져온다.")
        @Test
        void getDownStation() {
            Sections sections = new Sections();
            sections.add(강남역_역삼역);
            sections.add(역삼역_정자역);

            Station actual = sections.getLineDownStation();

            Assertions.assertAll(
                    () -> assertThat(actual).isEqualTo(정자역),
                    () -> assertThat(actual).isNotEqualTo(역삼역),
                    () -> assertThat(actual).isNotEqualTo(강남역)
            );
        }

        @DisplayName("빈 구간 목록의 하행역을 가져올 경우 에러 처리한다.")
        @Test
        void getDownStationEmptySections() {
            Sections sections = new Sections();

            assertThatThrownBy(() -> sections.getLineDownStation()).isInstanceOf(StationNotFoundException.class);
        }
    }
}
