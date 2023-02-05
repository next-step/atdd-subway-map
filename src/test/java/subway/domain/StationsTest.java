package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class StationsTest {

    private Station upStation;
    private Station downStation;

    @BeforeEach
    void setUp() {
        upStation = new Station("강남역");
        downStation = new Station("양재역");
    }

    @DisplayName("지하철 노선 상행선과 하행선을 생성한다.")
    @ParameterizedTest(name = "upStationId : {0} dwonStationId : {1}")
    @CsvSource(value = {"1:2", "2:1"}, delimiter = ':')
    void createStations(final long upStationId, final long downStationId) throws Exception {
        Field id = getStationIdField(upStation);
        id.set(upStation, upStationId);
        id.set(downStation, downStationId);

        Stations stations = new Stations(List.of(upStation, downStation), upStationId, downStationId);

        Assertions.assertAll(
                () -> assertThat(upStation).isEqualTo(stations.getUpStation()),
                () -> assertThat(downStation).isEqualTo(stations.getDownStation())
        );
    }

    @DisplayName("지하철 노선 상행선시 ID정보가 일치하지 않으면 예외 처리한다.")
    @ParameterizedTest(name = "upStationId : {0} dwonStationId : {1}")
    @CsvSource(value = {"1:2:3", "2:1:0"}, delimiter = ':')
    void createStationsFailIdNotMatch(
            final long upStationId,
            final long downStationId,
            final long errorStationId
    ) throws Exception {
        Field id = getStationIdField(upStation);
        id.set(upStation, upStationId);
        id.set(downStation, downStationId);

        assertThatThrownBy(() -> new Stations(List.of(upStation, downStation), upStationId, errorStationId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Field getStationIdField(final Station upStation) throws NoSuchFieldException {
        Field id = upStation.getClass().getDeclaredField("id");
        id.setAccessible(true);
        return id;
    }
}
