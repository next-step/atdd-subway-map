package subway.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.jpa.Line;
import subway.jpa.LineRepository;
import subway.jpa.StationRepository;
import subway.vo.LineRequest;
import subway.vo.LineResponse;
import subway.vo.StationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest lineRequest) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);
        Line lineEntity = mapper.map(lineRequest, Line.class);

        // 노선 저장
        Line savedLineEntity = lineRepository.save(lineEntity);

        // 노선의 지하철 조회
        List<StationResponse> stationResponses = stationRepository.findAllByLineName(lineEntity.getName()).stream()
                .map(s ->
                    {
                        StationResponse stationResponse = mapper.map(s, StationResponse.class);
                        return stationResponse;
                    })
                .collect(Collectors.toList());

        // Response 객체로 변환
        LineResponse lineResponse = mapper.map(lineEntity, LineResponse.class);
        lineResponse.setId(savedLineEntity.getId());
        lineResponse.setStations(stationResponses);
        return lineResponse;
    }
}
