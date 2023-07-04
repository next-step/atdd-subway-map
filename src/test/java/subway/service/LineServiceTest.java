package subway.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import subway.jpa.Line;
import subway.jpa.LineRepository;
import subway.jpa.Station;
import subway.jpa.StationRepository;
import subway.vo.LineRequest;
import subway.vo.LineResponse;
import subway.vo.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class LineServiceTest {

    @Mock
    LineRepository lineRepository;

    @Mock
    StationRepository stationRepository;

    @InjectMocks
    LineService lineService;

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
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);

        // data
        Station 지하철역 = new Station(1L, "지하철역");
        Station 새로운지하철역 = new Station(2L, "새로운지하철역");
        StationResponse 지하철역_응답 = modelMapper.map(지하철역, StationResponse.class);
        StationResponse 새로운지하철역_응답 = modelMapper.map(새로운지하철역, StationResponse.class);

        LineRequest 노선_생성_요청 = new LineRequest("신분당선",
                                                    "bg-red-600", 1L, 2L, 10);
        LineResponse 노선_생성_응답 = new LineResponse(1L, "신분당선", "bg-red-600", List.of(지하철역_응답, 새로운지하철역_응답));

        Line resLine = modelMapper.map(노선_생성_요청, Line.class);
        resLine.setId(1L);

        // given
        Mockito.when(lineRepository.save(any())).thenReturn(resLine);
        Mockito.when(stationRepository.findAllByLineName(노선_생성_요청.getName())).thenReturn(List.of(지하철역, 새로운지하철역));

        // when
        LineResponse lineResponse = lineService.saveLine(노선_생성_요청);

        // then
        System.out.println(lineResponse);
        System.out.println(노선_생성_응답);
        assertThat(lineResponse).isEqualTo(노선_생성_응답);
    }
}