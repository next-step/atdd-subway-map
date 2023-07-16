package subway.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;
import subway.exception.AlreadyExistDownStation;
import subway.exception.CannotCreateSectionException;
import subway.exception.LineNotFoundException;
import subway.exception.NoMatchLineUpStationException;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository,
        StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = new Line(lineRequest, lineRequest.getUpStationId(), lineRequest.getDownStationId());
        Line savedLine = lineRepository.save(line);

        if (isSectionNotRegistered(savedLine)) {
            sectionRepository.save(new Section(savedLine.getId(), savedLine.getUpStationId(),
                savedLine.getDownStationId(), savedLine.getDistance()));
        }

        Station upStation = stationRepository.findById(savedLine.getUpStationId()).orElseThrow(IllegalArgumentException::new);
        Station downStation = stationRepository.findById(savedLine.getDownStationId()).orElseThrow(IllegalArgumentException::new);

        return LineResponse.from(savedLine, upStation, downStation);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
            .map(line -> {
                Station upStation = stationRepository.findById(line.getUpStationId())
                    .orElseThrow(IllegalArgumentException::new);
                Station downStation = stationRepository.findById(line.getDownStationId())
                    .orElseThrow(IllegalArgumentException::new);

                return LineResponse.from(line, upStation, downStation);
            }).collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        Station upStation = stationRepository.findById(line.getUpStationId()).orElseThrow(IllegalArgumentException::new);
        Station downStation = stationRepository.findById(line.getDownStationId()).orElseThrow(IllegalArgumentException::new);

        return LineResponse.from(line, upStation, downStation);
    }

    public LineResponse updateLine(Long id, LineRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        line.updateLine(request);

        Line updateLine = lineRepository.save(line);
        Station upStation = stationRepository.findById(updateLine.getUpStationId()).orElseThrow(IllegalArgumentException::new);
        Station downStation = stationRepository.findById(updateLine.getDownStationId()).orElseThrow(IllegalArgumentException::new);

        return LineResponse.from(updateLine, upStation, downStation);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public SectionResponse createSection(Long id, SectionRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);

        if (canCreateSection(line, request)) {
            Section section = sectionRepository.save(new Section(id, request));

            line.updateLineStation(section);
            lineRepository.save(line);

            return SectionResponse.from(section);
        }

        throw new CannotCreateSectionException();
    }

    private boolean canCreateSection(Line line, SectionRequest request) {
        if (noMatchLineDownStationAndSectionUpStation(line.getDownStationId(), request.getUpStationId())) {
            throw new NoMatchLineUpStationException();
        }

        List<Section> sections = sectionRepository.findAllByLineId(line.getId());
        Set<Long> lineStationIds = sections.stream()
            .flatMap(section -> Stream.of(section.getUpStationId(), section.getDownStationId()))
            .collect(Collectors.toSet());
        Long sectionDownStationId = request.getDownStationId();
        if (alreadyExistStation(lineStationIds, sectionDownStationId)) {
            throw new AlreadyExistDownStation();
        }

        return true;
    }

    private boolean isSectionNotRegistered(Line line) {
        List<Section> sections = sectionRepository.findAllByLineId(line.getId());

        return sections.stream()
            .noneMatch(section -> section.getLineId().equals(line.getId())
                && section.getUpStationId().equals(line.getUpStationId())
                && section.getDownStationId().equals(line.getDownStationId()));
    }

    private boolean noMatchLineDownStationAndSectionUpStation(Long lineDownStationId, Long sectionUpStationId) {
        return !(Objects.equals(lineDownStationId, sectionUpStationId));
    }

    private boolean alreadyExistStation(Set<Long> lineStationIds, Long sectionDownStationId) {
        return lineStationIds.contains(sectionDownStationId);
    }
}
