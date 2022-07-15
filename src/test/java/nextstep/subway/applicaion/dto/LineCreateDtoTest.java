package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineCreateDtoTest {

    @Test
    @DisplayName("DTO -> Domain 변환이 정상적으로 수행된다.")
    void toDomainTest() {
        LineCreateDto dto = new LineCreateDto("4호선", "blue", 1L, 2L, 5);
        Line line = dto.toDomain();

        assertThat(line.getName()).isEqualTo(dto.getName());
        assertThat(line.getColor()).isEqualTo(dto.getColor());
    }

}