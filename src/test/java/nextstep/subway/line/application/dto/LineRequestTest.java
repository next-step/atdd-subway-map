package nextstep.subway.line.application.dto;

import nextstep.subway.line.domain.Line;
import org.junit.jupiter.api.Test;

import static nextstep.subway.line.LineTestSource.lineRequest;
import static org.assertj.core.api.Assertions.assertThat;

class LineRequestTest {

    @Test
    void 빌더() {
        final LineRequest lineRequest = lineRequest();

        assertThat(lineRequest.getName()).isEqualTo("lineName");
        assertThat(lineRequest.getColor()).isEqualTo("bg-red-600");
        assertThat(lineRequest.getUpStationId()).isEqualTo(2L);
        assertThat(lineRequest.getDownStationId()).isEqualTo(3L);
        assertThat(lineRequest.getDistance()).isEqualTo(4L);
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

    @Test
    void Line으로변환_Id() {
        final long id = 1L;
        final LineRequest lineRequest = lineRequest();
        final Line line = lineRequest.toLine(id);

        assertThat(line.getId()).isEqualTo(id);
        assertThat(line.getName()).isEqualTo(lineRequest.getName());
        assertThat(line.getColor()).isEqualTo(lineRequest.getColor());
        assertThat(line.getUpStationId()).isEqualTo(lineRequest.getUpStationId());
        assertThat(line.getDownStationId()).isEqualTo(lineRequest.getDownStationId());
        assertThat(line.getDistance()).isEqualTo(lineRequest.getDistance());
    }

}