package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor(),
                lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance()));
        List<StationResponse> stations = stationService.findStationsById(List.of(line.getUpStationId(), line.getDownStationId()));
        return LineResponse.of(line, stations);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(line -> LineResponse.of(
                        line,
                        stationService.findStationsById(List.of(line.getUpStationId(), line.getDownStationId()))))
                .collect(Collectors.toList());
    }
}
