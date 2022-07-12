package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.LineFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class LineDtoTest {


    @Test
    @DisplayName("DTO가 정상적으로 생성된다.")
    void createDtoTest() {
        Line line = LineFactory.getMockLine(1L, "4호선", "blue", 5);

        LineDto dto = LineDto.of(line, Collections.emptyList());

        assertThat(dto.getId()).isEqualTo(line.getId());
        assertThat(dto.getName()).isEqualTo(line.getName());
        assertThat(dto.getColor()).isEqualTo(line.getColor());
        assertThat(dto.getDistance()).isEqualTo(line.getDistance());

    }
}