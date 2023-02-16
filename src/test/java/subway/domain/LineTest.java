package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.exception.LineModifyException;

@DisplayName("노선 관련 기능")
class LineTest {

    private Station 강남역;
    private Station 역삼역;
    private Station 정자역;
    private Station 미금역;

    @BeforeEach
    void setUp() {
        this.강남역 = new Station("강남역");
        this.역삼역 = new Station("역삼역");
        this.정자역 = new Station("정자역");
        this.미금역 = new Station("미금역");
    }


    @DisplayName("수정 관련 기능")
    @Nested
    class Modify {
        @DisplayName("노선 이름 수정")
        @Test
        void modifyEditName() {
            String expected = "수정 후 이름";
            Line line = new Line("수정 전 이름", "색깔", 강남역, 정자역, 10);

            line.modify(expected, "색깔", 5);

            assertThat(line.getName()).isEqualTo(expected);
        }

        @DisplayName("노선 이름 수정시 이름은 null일 수 없습니다.")
        @Test
        void modifyEditNameNull() {
            Line line = new Line("수정 전 이름", "색깔", 강남역, 정자역, 10);

            assertThatThrownBy(() -> line.modify(null, "색깔", 5))
                    .isInstanceOf(LineModifyException.class);
        }

        @DisplayName("노선 색깔 수정")
        @Test
        void modifyEditColor() {
            String expected = "수정 후 색깔";
            Line line = new Line("이름", "색깔", 강남역, 정자역, 10);

            line.modify("이름", expected, 5);

            assertThat(line.getColor()).isEqualTo(expected);
        }

        @DisplayName("노선 이름 수정시 색깔은 null일 수 없습니다.")
        @Test
        void modifyEditColorNull() {
            Line line = new Line("수정 전 이름", "색깔", 강남역, 정자역, 10);

            assertThatThrownBy(() -> line.modify("이름", null, 5))
                    .isInstanceOf(LineModifyException.class);
        }

        @DisplayName("노선 거리 수정")
        @Test
        void modifyEditDistance() {
            int expected = 15;
            Line line = new Line("이름", "색깔", 강남역, 정자역, 10);

            line.modify("이름", "색깔", expected);

            assertThat(line.getDistance()).isEqualTo(expected);
        }
    }

    @DisplayName("구간 추가시 노선의 총 길이가 늘어난다.")
    @Test
    void addSectionPlusLineDistance() {
        int expected = 15;
        Line line = new Line("이름", "색깔", 강남역, 정자역, 10);
        line.addSection(5, 정자역, 미금역);

        assertThat(line.getDistance()).isEqualTo(expected);
    }

    @DisplayName("구간 제거시 노선의 총 길이가 감소한다.")
    @Test
    void deleteSectionMinusDistance() {
        int expected = 10;
        Line line = new Line("이름", "색깔", 강남역, 정자역, expected);
        line.addSection(5, 정자역, 미금역);
        line.deleteBy(미금역);

        assertThat(line.getDistance()).isEqualTo(expected);
    }
}
