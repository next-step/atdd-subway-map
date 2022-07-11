package nextstep.subway.station.applicaion;

import nextstep.subway.station.applicaion.dto.StationResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    @InjectMocks
    private StationService target;

    @Mock
    private StationRepository stationRepository;

    @Test
    void id로역조회() {
        final List<Long> idList = List.of(1L);
        doReturn(List.of(new Station("강남역")))
                .when(stationRepository)
                .findAllById(idList);

        final List<StationResponse> result = target.findStations(idList);
        assertThat(result).hasSize(1);
    }

}