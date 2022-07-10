package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse createLine(LineRequest request) {
        Line savedLine = lineRepository.save(request.toDomain());
        List<StationResponse> stations = findAllStationInLine(savedLine);
        return LineResponse.of(savedLine, stations);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAll() {
        return lineRepository.findAll()
                .stream()
                .map(line -> LineResponse.of(line, findAllStationInLine(line)))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findById(Long lineId) {
        Line findLine = lineRepository.findById(lineId)
                .orElseThrow(NoSuchElementException::new);

        List<StationResponse> stations = findAllStationInLine(findLine);
        return LineResponse.of(findLine, stations);
    }

    private List<StationResponse> findAllStationInLine(Line line) {
        return stationRepository.findAllById(List.of(line.getUpStationId(), line.getDownStationId()))
                .stream()
                .map(station -> StationResponse.of(station))
                .collect(Collectors.toList());
    }

    public void editLine(Long lineId, LineRequest request) {
        Line findLine = lineRepository.findById(lineId)
                .orElseThrow(NoSuchElementException::new);
        findLine.edit(request.toDomain());
    }

    public void deleteById(Long lineId) {
        lineRepository.deleteById(lineId);
    }
}

