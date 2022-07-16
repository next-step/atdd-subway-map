package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class StationTest {


    @Test
    @DisplayName("Station이 생성된다.")
    void createTest() {
        assertDoesNotThrow(() -> getMockStation(1L, "사당역"));
    }

    public static Station getMockStation(Long id, String name) {
        return Station.builder()
                .id(id)
                .name(name)
                .build();
    }

}