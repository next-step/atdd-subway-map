package nextstep.subway.line.application.dto;

import nextstep.subway.line.domain.Line;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineRequestTest {

    @Test
    void 빌더() {
        final LineRequest lineRequest = lineRequest();

        assertThat(lineRequest.getName()).isEqualTo("lineName");
        assertThat(lineRequest.getColor()).isEqualTo("bg-red-600");
        assertThat(lineRequest.getUpStationId()).isEqualTo(1L);
        assertThat(lineRequest.getDownStationId()).isEqualTo(2L);
        assertThat(lineRequest.getDistance()).isEqualTo(10L);
    }

    @Test
    void Line으로변환() {
        final LineRequest lineRequest = lineRequest();
        final Line line = lineRequest.toLine();

        assertThat(line.getName()).isEqualTo(lineRequest.getName());
        assertThat(line.getColor()).isEqualTo(lineRequest.getColor());
        assertThat(line.getUpStationId()).isEqualTo(lineRequest.getUpStationId());
        assertThat(line.getDownStationId()).isEqualTo(lineRequest.getDownStationId());
        assertThat(line.getDistance()).isEqualTo(lineRequest.getDistance());
    }

    private LineRequest lineRequest() {
        return LineRequest.builder()
                .name("lineName")
                .color("bg-red-600")
                .upStationId(1L)
                .downStationId(2L)
                .distance(10L)
                .build();
    }

}