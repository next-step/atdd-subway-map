package nextstep.subway.section.application.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SectionRequestTest {

    @Test
    void 빌더() {
        final SectionRequest result = sectionRequest();

        assertThat(result.getUpStationId()).isEqualTo(1L);
        assertThat(result.getDownStationId()).isEqualTo(2L);
        assertThat(result.getDistance()).isEqualTo(10L);
    }

    private SectionRequest sectionRequest() {
        return SectionRequest.builder()
                .upStationId(1L)
                .downStationId(2L)
                .distance(10L)
                .build();
    }

}