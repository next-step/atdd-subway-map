package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.common.exception.AlreadyExistException;
import subway.common.exception.NoDeleteOneSectionException;
import subway.common.exception.NoRegisterStationException;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.ui.dto.LineSectionRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static subway.common.error.LineSectionError.*;
import static subway.fixture.TestFixtureSection.구간_복수_등록;
import static subway.fixture.TestFixtureSection.구간_신규_등록;

@DisplayName("지하철 구간 응용 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class LineSectionServiceTest {

    @Mock
    private StationService stationService;

    @Mock
    private LineService lineService;

    @InjectMocks
    private LineSectionService lineSectionService;

    @DisplayName("노선 구간을 추가 등록한다.")
    @Test
    void addSection() {

        final Station 상행종점_강남역 = new Station(1L, "강남역");
        final Station 하행종점_잠실역 = new Station(2L, "잠실역");
        final Section 첫번째_구간 = new Section(1L, 상행종점_강남역, 하행종점_잠실역, 10);
        final Line 이호선 = new Line(1L, "2호선", "bg-red-600", 구간_신규_등록(첫번째_구간));
        final Station 하행종점_몽총토성역 = new Station(4L, "몽촌토성역");

        when(stationService.findById(anyLong())).thenReturn(하행종점_잠실역)
                .thenReturn(하행종점_몽총토성역);
        when(lineService.findById(anyLong())).thenReturn(이호선);

        final Long 노선_요청_ID = 1L;
        final LineSectionRequest 구간_요청 = new LineSectionRequest(2L, 4L, 10);
        lineSectionService.addSection(노선_요청_ID, 구간_요청);

        assertAll(
                () -> assertThat(이호선.getSections().getSections()).hasSize(2)
        );
    }

    @DisplayName("노선 구간 추가 시 요청값인 상행종점역은 노선에 등록되어 있지 않아서 구간 등록이 불가능하다.")
    @Test
    void error_addSection() {

        final Station 상행종점_강남역 = new Station(1L, "강남역");
        final Station 하행종점_잠실역 = new Station(2L, "잠실역");
        final Section 첫번째_구간 = new Section(1L, 상행종점_강남역, 하행종점_잠실역, 10);
        final Line 이호선 = new Line(1L, "2호선", "bg-red-600", 구간_신규_등록(첫번째_구간));
        final Station 상행종점_역삼역 = new Station(3L, "역삼역");
        final Station 하행종점_몽총토성역 = new Station(4L, "몽촌토성역");

        when(stationService.findById(anyLong())).thenReturn(상행종점_역삼역)
                .thenReturn(하행종점_몽총토성역);
        when(lineService.findById(anyLong())).thenReturn(이호선);

        final Long 노선_요청_ID = 1L;
        final LineSectionRequest 구간_요청 = new LineSectionRequest(3L, 4L, 10);
        assertThatThrownBy(() -> lineSectionService.addSection(노선_요청_ID, 구간_요청))
                .isInstanceOf(NoRegisterStationException.class)
                .hasMessage(NO_REGISTER_UP_STATION.getMessage());
    }

    @DisplayName("노선 구간 추가 등록 시 하행종점역은 노선에 등록되어 있어서 구간 등록이 불가능하다.")
    @Test
    void error_addSection_2() {

        final Station 상행종점_강남역 = new Station(1L, "강남역");
        final Station 하행종점_잠실역 = new Station(2L, "잠실역");
        final Section 첫번째_구간 = new Section(1L, 상행종점_강남역, 하행종점_잠실역, 10);
        final Line 이호선 = new Line(1L, "2호선", "bg-red-600", 구간_신규_등록(첫번째_구간));

        when(stationService.findById(anyLong())).thenReturn(하행종점_잠실역)
                .thenReturn(상행종점_강남역);
        when(lineService.findById(anyLong())).thenReturn(이호선);

        final Long 노선_요청_ID = 1L;
        final LineSectionRequest 구간_요청 = new LineSectionRequest(1L, 2L, 10);
        assertThatThrownBy(() -> lineSectionService.addSection(노선_요청_ID, 구간_요청))
                .isInstanceOf(AlreadyExistException.class)
                .hasMessage(NO_REGISTER_DOWN_STATION.getMessage());
    }

    @DisplayName("노선 구간을 제거한다.")
    @Test
    void removeSection() {

        final Station 상행종점_강남역 = new Station(1L, "강남역");
        final Station 하행종점_잠실역 = new Station(2L, "잠실역");
        final Station 하행종점_몽총토성역 = new Station(4L, "몽촌토성역");
        final Section 첫번째_구간 = new Section(1L, 상행종점_강남역, 하행종점_잠실역, 10);
        final Section 두번째_구간 = new Section(2L, 하행종점_잠실역, 하행종점_몽총토성역, 10);
        final Line 이호선 = new Line(1L, "2호선", "bg-red-600", 구간_복수_등록(첫번째_구간, 두번째_구간));

        when(stationService.findById(anyLong())).thenReturn(하행종점_몽총토성역);
        when(lineService.findById(anyLong())).thenReturn(이호선);

        final Long 노선_요청_ID = 1L;
        final Long 역_요청_하행종점_ID = 하행종점_몽총토성역.getId();
        lineSectionService.removeSection(노선_요청_ID, 역_요청_하행종점_ID);

        assertAll(
                () -> assertThat(이호선.getSections().getSections()).hasSize(1)
        );
    }

    @DisplayName("노선 구간 제거 시 구간이 하나인 경우 삭제 불가로 구간 삭제가 불가능하다.")
    @Test
    void error_removeSection() {

        final Station 상행종점_강남역 = new Station(1L, "강남역");
        final Station 하행종점_잠실역 = new Station(2L, "잠실역");
        final Section 첫번째_구간 = new Section(1L, 상행종점_강남역, 하행종점_잠실역, 10);
        final Line 이호선 = new Line(1L, "2호선", "bg-red-600", 구간_신규_등록(첫번째_구간));

        when(stationService.findById(anyLong())).thenReturn(하행종점_잠실역);
        when(lineService.findById(anyLong())).thenReturn(이호선);

        final Long 노선_요청_ID = 1L;
        final Long 역_요청_하행종점_ID = 하행종점_잠실역.getId();
        assertThatThrownBy(() -> lineSectionService.removeSection(노선_요청_ID, 역_요청_하행종점_ID))
                .isInstanceOf(NoDeleteOneSectionException.class)
                .hasMessage(NO_DELETE_ONE_SECTION.getMessage());
    }

    @DisplayName("노선 구간을 제거 시 등록된 하행 종점역이 아니어서 구간 삭제가 불가능하다.")
    @Test
    void error_removeSection_2() {

        final Station 상행종점_강남역 = new Station(1L, "강남역");
        final Station 하행종점_잠실역 = new Station(2L, "잠실역");
        final Station 하행종점_몽총토성역 = new Station(4L, "몽촌토성역");
        final Section 첫번째_구간 = new Section(1L, 상행종점_강남역, 하행종점_잠실역, 10);
        final Section 두번째_구간 = new Section(2L, 하행종점_잠실역, 하행종점_몽총토성역, 10);
        final Line 이호선 = new Line(1L, "2호선", "bg-red-600", 구간_복수_등록(첫번째_구간, 두번째_구간));

        when(stationService.findById(anyLong())).thenReturn(상행종점_강남역);
        when(lineService.findById(anyLong())).thenReturn(이호선);

        final Long 노선_요청_ID = 1L;
        final Long 역_요청_상행종점_ID = 상행종점_강남역.getId();
        assertThatThrownBy(() -> lineSectionService.removeSection(노선_요청_ID, 역_요청_상행종점_ID))
                .isInstanceOf(NoRegisterStationException.class)
                .hasMessage(NO_REGISTER_LAST_LINE_STATION.getMessage());
    }
}
