package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineDtoTest {


    @Test
    @DisplayName("DTO가 정상적으로 생성된다.")
    void createDtoTest() {
        Line line = LineFactory.getMockLine(1L, "4호선", "blue");

        LineDto dto = LineDto.of(line);

        assertThat(dto.getId()).isEqualTo(line.getId());
        assertThat(dto.getName()).isEqualTo(line.getName());
        assertThat(dto.getColor()).isEqualTo(line.getColor());

    }
}