package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.NotExistedStationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        checkDuplicatedName(request);
        existsStation(request.getUpStationId());
        existsStation(request.getDownStationId());
        checkDistanceLessThanZero(request.getDistance());
        Station upStation = stationRepository.findById(request.getUpStationId())
                .orElseThrow(EntityNotFoundException::new);
        Station downStation = stationRepository.findById(request.getDownStationId())
                .orElseThrow(EntityNotFoundException::new);
        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        Section section = new Section(
                line,
                upStation,
                downStation,
                request.getDistance()
        );
        line.addSection(section);
        return createLineResponse(line);
    }

    private void checkDuplicatedName(LineRequest request) {
        if (lineRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("[duplication]:name");
        }
    }

    private void existsStation(Long upStationId) {
        if (upStationId == null || !stationRepository.existsById(upStationId)) {
            throw new NotExistedStationException("[notExist]:stationId");
        }
    }

    private void checkDistanceLessThanZero(int distance) {
        if (distance < 0) {
            throw new NotExistedStationException("[notValid]:distance");
        }
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse getLine(Long id) {
        Line line = lineRepository.getById(id);
        return createLineResponse(line);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.getById(id);
        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    public void deleteLine(Long id) {
        Line line = lineRepository.getById(id);
        lineRepository.deleteById(line.getId());
    }

    public void saveSection(Long id, SectionRequest request) {
        existsStation(request.getUpStationId());
        existsStation(request.getDownStationId());
        Station upStation = stationRepository.findById(request.getUpStationId())
                .orElseThrow(EntityNotFoundException::new);
        Station downStation = stationRepository.findById(request.getDownStationId())
                .orElseThrow(EntityNotFoundException::new);
        Line line = lineRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        if (!line.equalsLastDownStation(upStation)) { throw new InvalidParameterException(); }
        if (line.checkDuplicatedDownStation(downStation))  { throw new InvalidParameterException(); }
        Section section = new Section(
                line,
                upStation,
                downStation,
                request.getDistance()
        );
        line.addSection(section);
    }
}
