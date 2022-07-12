package nextstep.subway.ui.dto;

import nextstep.subway.applicaion.dto.StationCreateDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StationRequestTest {

    @Test
    @DisplayName("DTO 변환이 정상적으로 수행된다.")
    void toDtoTest() {
        StationRequest stationRequest = new StationRequest("사당역");
        StationCreateDto dto = stationRequest.toDto();

        assertThat(stationRequest.getName()).isEqualTo(dto.getName());
    }
}