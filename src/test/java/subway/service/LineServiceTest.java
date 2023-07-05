package subway.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import subway.jpa.Line;
import subway.jpa.LineRepository;
import subway.jpa.Station;
import subway.jpa.StationRepository;
import subway.utils.ModelMapperUtil;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    LineRepository lineRepository;

    @Mock
    StationRepository stationRepository;

    @InjectMocks
    LineService lineService;

    static Station 지하철역 = new Station(1L, "지하철역");
    static Station 새로운지하철역 = new Station(2L, "새로운지하철역");

    static Line 신분당선 = new Line( "신분당선", "bg-red-600", 지하철역.getId(), 새로운지하철역.getId(), 10);
    static {
        신분당선.setId(1L);
    }

    /**
     * Input = LineRequest
     * Output = LineResponse
     *
     * given DB에 노선 정보 저장 시, 성공적으로 저장된다.
     * given 노선에 포함된 역 목록을 DB에서 조회시, 역 리스트(역 두개)를 반환한다.
     * When LineService.saveLine() 호출 시
     * Then LineResponse를 반환한다.
     */
    @DisplayName("노선을 생성한다")
    @Test
    void saveLine() {
        // data
        StationResponse 지하철역_응답 = ModelMapperUtil.modelMapper.map(지하철역, StationResponse.class);
        StationResponse 새로운지하철역_응답 = ModelMapperUtil.modelMapper.map(새로운지하철역, StationResponse.class);
        LineRequest 노선_생성_요청 = new LineRequest(신분당선.getName(), 신분당선.getColor(), 신분당선.getUpStationId(), 신분당선.getDownStationId(), 신분당선.getDistance());
        LineResponse 노선_생성_응답 = new LineResponse(신분당선.getId(), 신분당선.getName(), 신분당선.getColor(), List.of(지하철역_응답, 새로운지하철역_응답));

        // given
        Mockito.when(lineRepository.save(any())).thenReturn(신분당선);
        Mockito.when(stationRepository.findAllByLineName(노선_생성_요청.getName())).thenReturn(List.of(지하철역, 새로운지하철역));

        // when
        LineResponse lineResponse = lineService.saveLine(노선_생성_요청);

        // then
        assertThat(lineResponse).isEqualTo(노선_생성_응답);
    }
}