package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.jpa.Line;
import subway.jpa.LineRepository;
import subway.jpa.StationRepository;
import subway.utils.ModelMapperUtil;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;

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
        Line lineEntity = new Line(
                lineRequest.getName(),
                lineRequest.getColor(),
                lineRequest.getUpStationId(),
                lineRequest.getDownStationId(),
                lineRequest.getDistance()
        );

        Line savedLineEntity = lineRepository.save(lineEntity);

        List<StationResponse> stationResponses = stationRepository.findAllByLineName(lineEntity.getName()).stream()
                .map(s -> ModelMapperUtil.modelMapper.map(s, StationResponse.class))
                .collect(Collectors.toList());

        LineResponse lineResponse = ModelMapperUtil.modelMapper.map(savedLineEntity, LineResponse.class);
        lineResponse.setId(savedLineEntity.getId());
        lineResponse.setStations(stationResponses);
        return lineResponse;
    }
}
