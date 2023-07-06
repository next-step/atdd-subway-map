package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.dto.LineCreateRequest;
import subway.dto.LineResponse;
import subway.dto.LineUpdateRequest;
import subway.dto.SectionCreateRequest;
import subway.exception.LineDuplicationNameException;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;

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
    public LineResponse save(LineCreateRequest lineCreateRequest) {
        validateDuplicationLineName(lineCreateRequest.getName());
        Line line = lineRepository.save(lineCreateRequest.toLine());
        addSection(line, lineCreateRequest.getUpStationId(), lineCreateRequest.getDownStationId(),
            lineCreateRequest.getDistance());
        return LineResponse.from(line);
    }

    private void validateDuplicationLineName(String name) {
        if (lineRepository.existsByName(name)) {
            throw new LineDuplicationNameException();
        }
    }

    private void addSection(Line line, Long upStationId, Long DownStationId, Integer distance) {
        Station upStation = getStation(upStationId);
        Station downStation = getStation(DownStationId);
        line.addSection(new Section(line, upStation, downStation, distance));
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
            .orElseThrow(StationNotFoundException::new);
    }

    public List<LineResponse> findAll() {
        return lineRepository.findAll()
            .stream()
            .map(LineResponse::from)
            .collect(Collectors.toUnmodifiableList());
    }

    public LineResponse findById(Long id) {
        return LineResponse.from(lineRepository.findById(id)
            .orElseThrow(LineNotFoundException::new));
    }

    @Transactional
    public void update(Long id, LineUpdateRequest lineUpdateRequest) {
        Line line = lineRepository.findById(id)
            .orElseThrow(LineNotFoundException::new);
        line.update(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
    }

    @Transactional
    public void delete(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public LineResponse createSection(Long id, SectionCreateRequest sectionCreateRequest) {
        Line line = lineRepository.findById(id)
            .orElseThrow(LineNotFoundException::new);
        addSection(line, sectionCreateRequest.getUpStationId(), sectionCreateRequest.getDownStationId(),
            sectionCreateRequest.getDistance());
        return LineResponse.from(line);
    }

    @Transactional
    public void deleteSection(Long id, Long stationId) {
        Line line = lineRepository.findById(id)
            .orElseThrow(LineNotFoundException::new);
        line.removeLastSection(stationId);
    }
}
