package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.*;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
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
        given(stationRepository.findById(1L)).willReturn(Optional.of(모란역));
        given(stationRepository.findById(2L)).willReturn(Optional.of(암사역));
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

    @Test
    void 노선을_조회한다() {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");
        given(lineRepository.findById(1L))
                .willReturn(Optional.of(new Line("8호선", "bg-pink-500", 17L, 모란역, 암사역)));

        // when
        LineResponse lineResponse = lineService.findLine(1L);

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
    void 노선이_존재하지_않으면_예외를_발생시킨다() {
        // given
        given(lineRepository.findById(any())).willReturn(Optional.empty());

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> lineService.findLine(any()));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" ", ""})
    void 노선_이름_변경시_빈칸이거나_널이면_예외를_발생시킨다(String name) {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");
        given(lineRepository.findById(1L))
                .willReturn(Optional.of(new Line("8호선", "bg-pink-500", 17L, 모란역, 암사역)));

        // then
        assertThatIllegalArgumentException().isThrownBy(() ->
                lineService.updateLine(1L, new LineUpdateRequest(name, "bg-lime-400"))
        );
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {" ", ""})
    void 노선_색상_변경시_빈칸이거나_널이면_예외를_발생시킨다(String color) {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");
        given(lineRepository.findById(1L))
                .willReturn(Optional.of(new Line("8호선", "bg-pink-500", 17L, 모란역, 암사역)));

        // then
        assertThatIllegalArgumentException().isThrownBy(() ->
                lineService.updateLine(1L, new LineUpdateRequest("2호선", color))
        );
    }

    @Test
    void 구간을_추가한다() {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");
        final Line _8호선 = new Line("8호선", "bg-pink-500", 17L, 모란역, 암사역);
        final SectionRequest sectionRequest = new SectionRequest(2L, 3L, 10L);
        given(lineRepository.findById(1L)).willReturn(Optional.of(_8호선));

        final Station 송파역 = new Station(3L, "송파역");
        given(stationRepository.findById(2L)).willReturn(Optional.of(암사역));
        given(stationRepository.findById(3L)).willReturn(Optional.of(송파역));

        // when
        LineResponse lineResponse = lineService.addSection(1L, sectionRequest);

        //then
        assertAll(() -> {
            assertThat(lineResponse.getName()).isEqualTo("8호선");
            assertThat(lineResponse.getColor()).isEqualTo("bg-pink-500");
            assertThat(lineResponse.getStationResponses()).containsExactly(
                    new StationResponse(1L, "모란역"),
                    new StationResponse(2L, "암사역"),
                    new StationResponse(3L, "송파역")
            );
        });
    }

    @Test
    void 구간을_추가할_때_같은_구간이면_예외를_발생시킨다() {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");
        final Line _8호선 = new Line("8호선", "bg-pink-500", 17L, 모란역, 암사역);
        final SectionRequest sectionRequest = new SectionRequest(1L, 2L, 10L);
        given(lineRepository.findById(1L)).willReturn(Optional.of(_8호선));

        given(stationRepository.findById(1L)).willReturn(Optional.of(모란역));
        given(stationRepository.findById(2L)).willReturn(Optional.of(암사역));

        // then
        assertThatIllegalArgumentException().isThrownBy(() ->
                lineService.addSection(1L, sectionRequest)
        );
    }

    @Test
    void 새로운_구간의_상행역은_해당_노선에_등록되어있는_하행_종점역이_아니면_예외를_발생시킨다() {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");
        final Line _8호선 = new Line("8호선", "bg-pink-500", 17L, 모란역, 암사역);
        final SectionRequest sectionRequest = new SectionRequest(1L, 3L, 10L);
        given(lineRepository.findById(1L)).willReturn(Optional.of(_8호선));

        final Station 송파역 = new Station(3L, "송파역");
        given(stationRepository.findById(1L)).willReturn(Optional.of(모란역));
        given(stationRepository.findById(3L)).willReturn(Optional.of(송파역));

        // then
        assertThatIllegalArgumentException().isThrownBy(() ->
                lineService.addSection(1L, sectionRequest)
        );
    }

    @Test
    void 새로운_구간의_하행역은_해당_노선에_등록되어있는_역이면_예외를_발생시킨다() {
        // given
        final Station 모란역 = new Station(1L, "모란역");
        final Station 암사역 = new Station(2L, "암사역");
        final Line _8호선 = new Line("8호선", "bg-pink-500", 17L, 모란역, 암사역);
        final SectionRequest sectionRequest = new SectionRequest(2L, 1L, 10L);
        given(lineRepository.findById(1L)).willReturn(Optional.of(_8호선));

        given(stationRepository.findById(2L)).willReturn(Optional.of(암사역));
        given(stationRepository.findById(1L)).willReturn(Optional.of(모란역));

        // then
        assertThatIllegalArgumentException().isThrownBy(() ->
                lineService.addSection(1L, sectionRequest)
        );
    }

}