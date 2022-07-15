package nextstep.subway.section.application.dto;

import org.junit.jupiter.api.Test;

import static nextstep.subway.section.SectionTestSource.sectionRequest;
import static org.assertj.core.api.Assertions.assertThat;

class SectionResponseTest {

    @Test
    void 빌더() {
        final Long sectionId = 1L;
        final SectionRequest sectionRequest = sectionRequest();

        final SectionResponse result = SectionResponse.builder()
                .id(sectionId)
                .upStationId(sectionRequest.getUpStationId())
                .downStationId(sectionRequest.getDownStationId())
                .distance(sectionRequest.getDistance())
                .build();

        assertThat(result.getId()).isEqualTo(sectionId);
        assertThat(result.getUpStationId()).isEqualTo(sectionRequest.getUpStationId());
        assertThat(result.getDownStationId()).isEqualTo(sectionRequest.getDownStationId());
        assertThat(result.getDistance()).isEqualTo(sectionRequest.getDistance());
    }

}