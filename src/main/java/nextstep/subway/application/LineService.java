package nextstep.subway.application;

import nextstep.subway.application.dto.LineRequest;
import nextstep.subway.application.dto.LineResponse;
import nextstep.subway.application.dto.UpdateLineRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor()));
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow(EntityNotFoundException::new);
        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow(EntityNotFoundException::new);
        line.registerStation(upStation.getId(), downStation.getId());
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
    public void updateLine(Long lineId, UpdateLineRequest updateLineRequest) {
        Line findLine = lineRepository.findById(lineId).orElseThrow(() -> new EntityNotFoundException("해당하는 노선이 없습니다."));
        findLine.changeName(updateLineRequest.getName());
        findLine.changeColor(updateLineRequest.getColor());
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
