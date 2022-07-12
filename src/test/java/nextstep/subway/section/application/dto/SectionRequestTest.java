package nextstep.subway.section.application.dto;

import nextstep.subway.section.domain.Section;
import org.junit.jupiter.api.Test;

import static nextstep.subway.section.SectionTestSource.sectionRequest;
import static org.assertj.core.api.Assertions.assertThat;

class SectionRequestTest {

    @Test
    void 빌더() {
        final SectionRequest result = sectionRequest();

        assertThat(result.getUpStationId()).isEqualTo(6L);
        assertThat(result.getDownStationId()).isEqualTo(8L);
        assertThat(result.getDistance()).isEqualTo(10L);
    }

    @Test
    void Section으로변환() {
        final long lineId = 20L;
        final SectionRequest sectionRequest = sectionRequest();

        final Section result = sectionRequest.toSection(lineId);

        assertThat(result.getLineId()).isEqualTo(lineId);
        assertThat(result.getUpStationId()).isEqualTo(sectionRequest.getUpStationId());
        assertThat(result.getDownStationId()).isEqualTo(sectionRequest.getDownStationId());
        assertThat(result.getDistance()).isEqualTo(sectionRequest.getDistance());
    }

}