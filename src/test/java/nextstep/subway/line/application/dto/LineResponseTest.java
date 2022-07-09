package nextstep.subway.line.application.dto;

import nextstep.subway.station.applicaion.dto.StationResponse;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class LineResponseTest {

    @Test
    void 빌더() {
        final LineResponse lineResponse = lineResponse();

        assertThat(lineResponse.getId()).isEqualTo(1L);
        assertThat(lineResponse.getName()).isEqualTo("lineName");
        assertThat(lineResponse.getColor()).isEqualTo("bg-red-600");
        assertThat(lineResponse.getStations()).hasSize(1);
    }

    private LineResponse lineResponse() {
        return LineResponse.builder()
                .id(1L)
                .name("lineName")
                .color("bg-red-600")
                .stations(Collections.singletonList(new StationResponse(2L, "강남역")))
                .build();
    }

}