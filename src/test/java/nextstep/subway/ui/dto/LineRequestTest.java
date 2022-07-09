package nextstep.subway.ui.dto;

import nextstep.subway.applicaion.dto.LineCreateDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineRequestTest {

    @Test
    @DisplayName("DTO 변환이 정상적으로 수행된다.")
    void toDtoTest() {
        LineRequest lineRequest = new LineRequest("4호선", "blue", 1L, 2L, 5);
        LineCreateDto dto = lineRequest.toDto();

        assertThat(lineRequest.getName()).isEqualTo(dto.getName());
        assertThat(lineRequest.getColor()).isEqualTo(dto.getColor());
        assertThat(lineRequest.getUpStationId()).isEqualTo(dto.getUpStationId());
        assertThat(lineRequest.getDownStationId()).isEqualTo(dto.getDownStationId());
        assertThat(lineRequest.getDistance()).isEqualTo(dto.getDistance());
    }
}