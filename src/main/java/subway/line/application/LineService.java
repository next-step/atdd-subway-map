package subway.line.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.dto.LineCreateRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.LineUpdateRequest;
import subway.line.exception.LineNotFoundException;
import subway.section.domain.Section;
import subway.section.domain.SectionRepository;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;
import subway.station.exception.StationNotFoundException;

@Transactional(readOnly = true)
@Service
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public LineResponse saveLine(LineCreateRequest lineCreateRequest) {
        Station upStation = getStation(lineCreateRequest.getUpStationId());
        Station downStation = getStation(lineCreateRequest.getDownStationId());

        Line line = new Line(lineCreateRequest.getName(), lineCreateRequest.getColor());
        lineRepository.save(line);

        //TODO: 지하철 노선을 처음 등록할 때, 굳이 Section이 한 개 등록될 필요가 있을까?
        Section section = new Section(line, upStation, downStation, lineCreateRequest.getDistance());
        sectionRepository.save(section);

        return LineResponse.of(line);
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(StationNotFoundException::new);
    }

    public List<LineResponse> findAllStation() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        Line line = getLine(id);
        return LineResponse.of(line);
    }

    private Line getLine(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new);
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
        Line line = getLine(id);
        line.update(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        Line line = getLine(id);
        List<Section> sections = line.getSections();

        sectionRepository.deleteAll(sections);
        lineRepository.delete(line);
    }
}
