package subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.DownStationOfNewSectionMustNotExistingLineStationException;
import subway.exception.UpStationOfNewSectionMustBeDownStationOfExistingLineException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("노선 테스트")
public class LineTest {

    Line line;

    @BeforeEach
    void setUp() {
        line = new Line("테스트라인", "red", 1L, 2L, 10L);
    }

    @Test
    @DisplayName("노선 연장 정상 테스트")
    void extendStationLine() {
//        stationLine.extendLine(new StationLine(null, null, 2L, 3L, 20L));

        assertThat(line.getUpStationId()).isEqualTo(1L);
        assertThat(line.getDownStationId()).isEqualTo(3L);
        assertThat(line.getDistance()).isEqualTo(30L);
    }

    @Test
    @DisplayName("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.")
    void extendStationLineError1() {
        assertThatThrownBy(() -> {
//            stationLine.extendLine(new StationLine(null, null, 3L, 1L, 20L));
        }).isInstanceOf(UpStationOfNewSectionMustBeDownStationOfExistingLineException.class);
    }

    @Test
    @DisplayName("새로운 구간의 하행역은 해당 노선에 등록되어 있는 역일 수 없다")
    void extendStationLineError2() {
        assertThatThrownBy(() -> {
//            stationLine.extendLine(new StationLine(null, null, 3L, 1L, 20L));
        }).isInstanceOf(DownStationOfNewSectionMustNotExistingLineStationException.class);
    }

    @Test
    @DisplayName("노선 삭제 정상 테스트")
    void deleteStationLine() {

    }


}