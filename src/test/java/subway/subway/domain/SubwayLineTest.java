package subway.subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import subway.ParentTest;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.*;

/**
 * 지하철 노선에 대한 도메인 단위 테스트
 */
@DisplayName("지하철 노선에 대한 도메인 단위 테스트")
public class SubwayLineTest extends ParentTest {

    /**
     * given : 시작과 끝의 지하철 역이 두개 주어지고, 둘 사이의 거리와 이름이 주어지고<br>
     * when : 그 값으로 지하철 노선을 생성하면<br>
     * then : 지하철 노선이 생성된다.<br>
     */
    @Test
    @DisplayName("지하철 노선을 생성한다")
    void registerSubwayLine() {
        //given

        String name = "2호선";
        String color = "green";

        Station.Id id = new Station.Id(1L);
        String name1 = "강남역";
        Station station1 = Mockito.mock(Station.class);
        given(station1.getId()).willReturn(id);
        given(station1.getName()).willReturn(name1);

        Station.Id id2 = new Station.Id(2L);
        String name2 = "역삼역";
        Station station2 = Mockito.mock(Station.class);
        given(station2.getId()).willReturn(id2);
        given(station2.getName()).willReturn(name2);

        BigDecimal number = BigDecimal.TEN;
        Kilometer kilometer = Kilometer.of(number);

        //when

        SubwayLine subwayLine = SubwayLine.register(name, color, station1, station2, kilometer);

        //then

        Assertions.assertThat(subwayLine.getName()).isEqualTo(name);
        Assertions.assertThat(subwayLine.getColor()).isEqualTo(color);
        Assertions.assertThat(subwayLine.getSectionList().size()).isEqualTo(1);
        Assertions.assertThat(subwayLine.getSectionList().stream().map(SubwaySection::getDistance).collect(Collectors.toList()))
                .containsOnly(kilometer);
        Assertions.assertThat(subwayLine.getSectionList().stream().map(SubwaySection::getStartStationId).collect(Collectors.toList()))
                .containsOnly(id);
        Assertions.assertThat(subwayLine.getSectionList().stream().map(SubwaySection::getEndStationId).collect(Collectors.toList()))
                .containsOnly(id2);

    }

}
