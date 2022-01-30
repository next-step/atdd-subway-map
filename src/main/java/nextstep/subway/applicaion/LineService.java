package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.exception.LineNameDuplicatedException;
import nextstep.subway.exception.LineNotFoundException;
import nextstep.subway.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest request) {
        String name = request.getName();
        if (lineRepository.existsByName(name)) {
            throw new LineNameDuplicatedException(name);
        }

        Section section = createSection(request.getUpStationId(), request.getDownStationId(), request.getDistance());

        Line line = request.toEntity();
        line.addSection(section);

        Line savedLine = lineRepository.save(line);
        return LineResponse.fromEntity(savedLine);
    }

    public void addSection(Long lineId, SectionRequest sectionRequest) {
        findLineById(lineId).addSection(
                createSection(
                        sectionRequest.getUpStationId(),
                        sectionRequest.getDownStationId(),
                        sectionRequest.getDistance())
        );
    }

    private Section createSection(Long upStationId, Long downStationId, int distance) {
        Station upStation = stationService.findStation(upStationId);
        Station downStation = stationService.findStation(downStationId);
        return Section.of(upStation, downStation, distance);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAll() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(LineResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(Long lineId) throws NotFoundException {
        Line line = findLineById(lineId);
        return LineResponse.fromEntity(line);
    }

    public void updateLine(Long lineId, LineRequest request) {
        Line line = findLineById(lineId);
        line.change(request.getName(), request.getColor());
    }

    private Line findLineById(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException(lineId));
    }

    public void deleteLine(Long lineId) {
        lineRepository.deleteById(lineId);
    }

    public void removeSection(Long lineId, Long stationId) {
        findLineById(lineId).removeSection(stationId);
    }
}
