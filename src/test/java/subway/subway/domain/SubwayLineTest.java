package subway.subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
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

        Long id1 = 1L;
        String name1 = "강남역";
        Station station1 = Mockito.mock(Station.class);
        given(station1.getId()).willReturn(id1);
        given(station1.getName()).willReturn(name1);

        Long id2 = 2L;
        String name2 = "역삼역";
        Station station2 = Mockito.mock(Station.class);
        given(station2.getId()).willReturn(id2);
        given(station2.getName()).willReturn(name2);

        BigDecimal number = BigDecimal.TEN;
        Kilometer kilometer = new Kilometer(number);

        SubwaySection subwaySection = SubwaySection.register(station1, station2, kilometer);
        SubwaySectionList subwaySectionList = new SubwaySectionList(subwaySection);

        //when

        SubwayLine subwayLine = SubwayLine.register(name, color, subwaySectionList);

        //then

        Assertions.assertThat(subwayLine.getName()).isEqualTo(name);
        Assertions.assertThat(subwayLine.getColor()).isEqualTo(color);
        Assertions.assertThat(subwayLine.getStations().size()).isEqualTo(1);
        Assertions.assertThat(subwayLine.getStations().stream().map(SubwaySection::getKilometer).collect(Collectors.toList()))
                .containsOnly(kilometer.getKilometer());
        Assertions.assertThat(subwayLine.getStations().stream().map(SubwaySection::getStartStationId).collect(Collectors.toList()))
                .containsOnly(id1);
        Assertions.assertThat(subwayLine.getStations().stream().map(SubwaySection::getEndStationId).collect(Collectors.toList()))
                .containsOnly(id2);

    }

}
