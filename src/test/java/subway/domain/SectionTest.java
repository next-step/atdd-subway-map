package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("구간 관련 기능")
class SectionTest {

    private Station 강남역;
    private Station 역삼역;
    private Station 정자역;
    private Line line;
    private Section 강남역_정자역;

    @BeforeEach
    void setUp() {
        this.강남역 = new Station("강남역");
        this.역삼역 = new Station("역삼역");
        this.정자역 = new Station("정자역");
        this.line = new Line("2호선", "색깔", 강남역, 역삼역, 10);
        this.강남역_정자역 = new Section(10, 강남역, 정자역, line);
    }

    @DisplayName("구간에 역이 포함하는지를 반환한다.")
    @Test
    void contain() {
        assertAll(
                () -> assertThat(강남역_정자역.contain(강남역)).isTrue(),
                () -> assertThat(강남역_정자역.contain(정자역)).isTrue(),
                () -> assertThat(강남역_정자역.contain(역삼역)).isFalse()
        );
    }

    @DisplayName("역이 구간의 상행역인지 반환한다")
    @Test
    void isEqualUpStation() {
        assertAll(
                () -> assertThat(강남역_정자역.isEqualUpStation(강남역)).isTrue(),
                () -> assertThat(강남역_정자역.isEqualUpStation(정자역)).isFalse()
        );
    }

    @DisplayName("역이 구간의 하행역인지 반환한다.")
    @Test
    void isEqualDownStation() {
        assertAll(
                () -> assertThat(강남역_정자역.isEqualDownStation(강남역)).isFalse(),
                () -> assertThat(강남역_정자역.isEqualDownStation(정자역)).isTrue()
        );
    }
}
