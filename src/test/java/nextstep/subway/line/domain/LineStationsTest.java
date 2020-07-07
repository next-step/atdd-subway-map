package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LineStations 도메인 테스트")
public class LineStationsTest {

    @Test
    @DisplayName("기본 생성자로 생성한다")
    void create() {
        //when
        LineStations lineStations = new LineStations();

        //then
        assertThat(lineStations).isNotNull();
    }

    @Test
    @DisplayName("이전 지하철역이 같은 객체가 들어오면, 기존의 지하철역은 뒤로 밀려나고, 새로 들어온 지하철역이 그 자리를 잇는다")
    void addMiddle() {
        //given
        final LineStations lineStations = new LineStations();


        //when
        lineStations.add(new LineStation(null, 1L, 5, 5));
        lineStations.add(new LineStation(1L, 2L, 5, 5));
        lineStations.add(new LineStation(1L, 3L, 5, 5));
        final List<LineStation> lineStationsInOrder = lineStations.getLineStationsInOrder();

        //then
        assertThat(lineStationsInOrder).hasSize(3);
        final List<Long> stationIdsInOrder = lineStationsInOrder.stream()
                .map(LineStation::getStationId)
                .collect(Collectors.toList());
        assertThat(stationIdsInOrder)
                .containsExactly(1L, 3L, 2L);
    }

    @Test
    @DisplayName("preStation 이 NULL인 지하철역이 중복되서 등록되면, 기존의 지하철역은 뒤로 밀려나고, 새로 들어온 지하철역이 그 자리를 잇는다")
    void addFirst() {
        //given
        final LineStations lineStations = new LineStations();

        //when
        lineStations.add(new LineStation(null, 1L, 5, 5));
        lineStations.add(new LineStation(null, 2L, 5, 5));
        lineStations.add(new LineStation(1L, 3L, 5, 5));
        lineStations.add(new LineStation(3L, 4L, 5, 5));
        final List<LineStation> lineStationsInOrder = lineStations.getLineStationsInOrder();

        //then
        assertThat(lineStationsInOrder).hasSize(4);
        final List<Long> stationIdsInOrder = lineStationsInOrder.stream()
                .map(LineStation::getStationId)
                .collect(Collectors.toList());
        assertThat(stationIdsInOrder)
                .containsExactly(2L, 1L, 3L, 4L);
    }
}

    