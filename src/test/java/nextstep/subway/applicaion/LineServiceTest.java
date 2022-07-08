package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @InjectMocks
    LineService lineService;

    @Mock
    LineRepository lineRepository;

    @Mock
    StationRepository stationRepository;


    @Test
    void 라인을_저장한다() {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");
        final LineRequest lineRequest = new LineRequest("8호선", "bg-pink-500", 1L, 2L, 17L);
        given(stationRepository.findById(any())).willReturn(Optional.of(모란역));
        given(stationRepository.findById(any())).willReturn(Optional.of(암사역));
        given(lineRepository.save(any())).willReturn(new Line("8호선", "bg-pink-500", 17L, 모란역, 암사역));
        // when
        LineResponse lineResponse = lineService.saveLine(lineRequest);

        // then
        assertAll(() -> {
            assertThat(lineResponse.getName()).isEqualTo("8호선");
            assertThat(lineResponse.getColor()).isEqualTo("bg-pink-500");
            assertThat(lineResponse.getStationResponses()).containsExactly(
                    new StationResponse(1L, "모란역"),
                    new StationResponse(2L, "암사역")
            );
        });
    }

    @Test
    void 상행종점역과_하행종점역이_같은_경우_예외를_발생시킨다() {
        // given
        final LineRequest lineRequest = new LineRequest("8호선", "bg-pink-500", 1L, 1L, 17L);

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> lineService.saveLine(lineRequest));
    }

    @Test
    void 역이_없는_경우_예외를_발생시킨다() {
        // given
        final LineRequest lineRequest = new LineRequest("8호선", "bg-pink-500", 1L, 2L, 17L);
        given(stationRepository.findById(any())).willReturn(Optional.empty());

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> lineService.saveLine(lineRequest));
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -2L})
    void 거리가_1_미만인_경우_예외를_발생시킨다(long distance) {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");
        final LineRequest lineRequest = new LineRequest("8호선", "bg-pink-500", 1L, 2L, distance);
        given(stationRepository.findById(any())).willReturn(Optional.of(모란역));
        given(stationRepository.findById(any())).willReturn(Optional.of(암사역));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> lineService.saveLine(lineRequest));
    }
}