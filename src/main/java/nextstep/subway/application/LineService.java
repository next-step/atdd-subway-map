package nextstep.subway.application;

import nextstep.subway.application.dto.LineRequest;
import nextstep.subway.application.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
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

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor()));
        line.registerStation(lineRequest.getUpStationId(), lineRequest.getDownStationId());
        return createLineResponse(line);

    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findByLine(Long lineId) {
        Line findLine = lineRepository.findById(lineId).orElseThrow(() -> new EntityNotFoundException("해당하는 노선이 없습니다."));
        return createLineResponse(findLine);

    }

    @Transactional
    public void updateLine(Long lineId, Map<String, Object> params) {
        Line findLine = lineRepository.findById(lineId).orElseThrow(() -> new EntityNotFoundException("해당하는 노선이 없습니다."));
        findLine.changeName((String) params.get("name"));
        findLine.changeColor((String) params.get("color"));
    }

    @Transactional
    public void deleteLine(Long lineId) {
        lineRepository.deleteById(lineId);
    }

    private LineResponse createLineResponse(Line line) {
        List<Station> stationList = stationRepository.findAllById(List.of(line.getUpStationId(), line.getDownStationId()));
        return new LineResponse(line, stationList);

    }
}
