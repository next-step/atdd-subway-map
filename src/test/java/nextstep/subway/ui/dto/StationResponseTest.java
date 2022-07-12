package nextstep.subway.ui.dto;

import nextstep.subway.applicaion.dto.StationDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class StationResponseTest {

    @Test
    @DisplayName("StationDTO를 통해 StationResponse가 정상적으로 생성된다.")
    void createTest() {
        StationDto dto = new StationDto(1L, "사당역");
        StationResponse stationResponse = StationResponse.of(dto);

        assertThat(stationResponse.getId()).isEqualTo(dto.getId());
        assertThat(stationResponse.getName()).isEqualTo(dto.getName());
    }
}