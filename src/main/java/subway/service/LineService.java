package subway.service;

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
import subway.exception.impl.AlreadyExistDownStation;
import subway.exception.impl.CannotCreateSectionException;
import subway.exception.impl.LineNotFoundException;
import subway.exception.impl.NoMatchStationException;
import subway.exception.impl.NonLastStationDeleteNotAllowedException;
import subway.exception.impl.SingleSectionDeleteNotAllowedException;
import subway.exception.impl.StationNotFoundException;
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
        Line line = Line.from(lineRequest);
        Line savedLine = lineRepository.save(line);

        if (isSectionNotRegistered(savedLine)) {
            sectionRepository.save(Section.from(savedLine));
        }

        Station upStation = stationRepository.findById(savedLine.getUpStationId()).orElseThrow(
            StationNotFoundException::new);
        Station downStation = stationRepository.findById(savedLine.getDownStationId())
            .orElseThrow(StationNotFoundException::new);

        return LineResponse.from(savedLine, upStation, downStation);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
            .map(line -> {
                Station upStation = stationRepository.findById(line.getUpStationId())
                    .orElseThrow(StationNotFoundException::new);
                Station downStation = stationRepository.findById(line.getDownStationId())
                    .orElseThrow(StationNotFoundException::new);

                return LineResponse.from(line, upStation, downStation);
            }).collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
        Station upStation = stationRepository.findById(line.getUpStationId())
            .orElseThrow(StationNotFoundException::new);
        Station downStation = stationRepository.findById(line.getDownStationId())
            .orElseThrow(StationNotFoundException::new);

        return LineResponse.from(line, upStation, downStation);
    }

    @Transactional
    public LineResponse updateLine(Long id, LineRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
        line.update(request);

        Line updateLine = lineRepository.save(line);
        Station upStation = stationRepository.findById(updateLine.getUpStationId())
            .orElseThrow(StationNotFoundException::new);
        Station downStation = stationRepository.findById(updateLine.getDownStationId())
            .orElseThrow(StationNotFoundException::new);

        return LineResponse.from(updateLine, upStation, downStation);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public SectionResponse createSection(Long id, SectionRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);

        if (canCreateSection(line, request)) {
            Section section = sectionRepository.save(Section.from(id, request));

            line.updateByAddingSection(section);
            lineRepository.save(line);

            return SectionResponse.from(section);
        }

        throw new CannotCreateSectionException();
    }

    private boolean canCreateSection(Line line, SectionRequest request) {
        if (noMatchLineDownStationAndSectionUpStation(line.getDownStationId(), request.getUpStationId())) {
            throw new NoMatchStationException();
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

    @Transactional
    public void deleteSection(Long id, Long stationId) {
        Line line = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
        List<Section> sections = sectionRepository.findAllByLineId(id);

        if (hasSingleSection(sections)) {
            throw new SingleSectionDeleteNotAllowedException();
        }

        if (line.isNotLastStation(stationId)) {
            throw new NonLastStationDeleteNotAllowedException();
        }

        Section removeSection = sections.remove(getLastIndex(sections));
        Section prevSection = sections.get(getLastIndex(sections));
        line.updateByRemovingSection(removeSection, prevSection);
    }

    private boolean hasSingleSection(List<Section> sections) {
        return sections.size() == 1;
    }


    private int getLastIndex(List<Section> sections) {
        return sections.size() - 1;
    }

}
