package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.jpa.Line;
import subway.jpa.LineRepository;
import subway.jpa.StationRepository;
import subway.utils.ModelMapperUtil;
import subway.vo.LineRequest;
import subway.vo.LineResponse;
import subway.vo.StationResponse;

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
        // Entity 객체로 변환
        Line lineEntity = ModelMapperUtil.modelMapper.map(lineRequest, Line.class);

        // 노선 저장
        Line savedLineEntity = lineRepository.save(lineEntity);

        // 노선의 지하철 조회
        List<StationResponse> stationResponses = stationRepository.findAllByLineName(lineEntity.getName()).stream()
                .map(s ->
                    {
                        StationResponse stationResponse = ModelMapperUtil.modelMapper.map(s, StationResponse.class);
                        return stationResponse;
                    })
                .collect(Collectors.toList());

        // Response 객체로 변환
        LineResponse lineResponse = ModelMapperUtil.modelMapper.map(lineEntity, LineResponse.class);
        lineResponse.setId(savedLineEntity.getId());
        lineResponse.setStations(stationResponses);
        return lineResponse;
    }
}
