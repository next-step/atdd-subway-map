package subway.unit.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.model.Line;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.unit.UnitTestFixture.*;

class LineTest {

    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        Line line = new Line("신분당선", "bg-red-600", GANG_NAM_STATION.getId(), YEOK_SAM_STATION.getId());

        // then
        assertThat(line.getName()).isEqualTo("신분당선");
        assertThat(line.getColor()).isEqualTo("bg-red-600");
        assertThat(line).isEqualTo(SHIN_BUN_DANG_LINE);
    }

    @DisplayName("지하철 노선 수정")
    @Test
    void modifyLine() {
        // given
        Line line = new Line("신분당선", "bg-red-600", GANG_NAM_STATION.getId(), YEOK_SAM_STATION.getId());

        // when
        line.modifyLine("당당선", "bg-red-700");

        // then
        assertThat(line.getName()).isEqualTo("당당선");
        assertThat(line.getColor()).isEqualTo("bg-red-700");
    }

}
