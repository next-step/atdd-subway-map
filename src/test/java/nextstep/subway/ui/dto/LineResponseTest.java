package nextstep.subway.ui.dto;

import nextstep.subway.applicaion.dto.LineDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class LineResponseTest {

    @Test
    @DisplayName("LineDTO를 통해 LineResponse가 정상적으로 생성된다.")
    void createTest() {
        LineDto lineDto = new LineDto(1L, "4호선", "blue", new ArrayList<>());
        LineResponse response = LineResponse.of(lineDto);

        assertThat(response.getId()).isEqualTo(lineDto.getId());
        assertThat(response.getName()).isEqualTo(lineDto.getName());
        assertThat(response.getColor()).isEqualTo(lineDto.getColor());
    }
}