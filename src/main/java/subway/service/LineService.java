package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import subway.dto.*;
import subway.entity.Line;
import subway.entity.Section;
import subway.entity.Station;
import subway.exception.BadRequestSectionException;
import subway.exception.NotFoundLineException;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;

import java.util.*;
import java.util.stream.Collectors;

import static subway.exception.BadRequestSectionException.*;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private StationService stationService;
    private SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationService stationService,
                       SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public LineResponse saveLine(final LineRequest request) {
        final Line line = lineRepository.save(toLine(request));
        final Station upStation = stationService.getStationById(request.getUpStationId());
        final Station downStation = stationService.getStationById(request.getDownStationId());
        sectionRepository.save(new Section(line, upStation, downStation, request.getDistance()));
        return toLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::toLineResponse)
                .collect(Collectors.toList());
    }

    public Line findLineById(final Long id) {
        final Optional<Line> line = lineRepository.findById(id);
        if (line.isPresent()) {
            return line.get();
        } else {
            throw new NotFoundLineException("id : " + id);
        }

    }

    public LineResponse getLineById(final Long id) {
        return toLineResponse(findLineById(id));
    }

    @Transactional
    public void updateLine(final Long id, final LineRequest request) {
        final Optional<Line> optionalLine = lineRepository.findById(id);
        if (optionalLine.isPresent()) {
            final Line line = updateLine(optionalLine.get(), request);
            lineRepository.save(line);
        }
    }

    @Transactional
    public void deleteLineById(final Long id) {
        final List<Section> sections = sectionRepository.findByLineId(id);
        sections.forEach(s -> sectionRepository.deleteById(s.getId()));
        lineRepository.deleteById(id);
    }

    public SectionResponse saveSection(final SectionRequest request, final Long lineId) {
        validateSaveSection(request, lineId);
        final Line line = findLineById(lineId);
        final Station upStation = stationService.getStationById(request.getUpStationId());
        final Station downStation = stationService.getStationById(request.getDownStationId());
        final Section section = sectionRepository.save(new Section(line, upStation, downStation,
                request.getDistance()));
        return toSectionResponse(section);
    }

    public void deleteSection(final Long lineId, final Long stationId) {
        validateDeleteSection(lineId, stationId);
        final Section section = sectionRepository.findByLineIdAndDownStationId(lineId, stationId);
        sectionRepository.delete(section);
    }

    private Line toLine(final LineRequest request) {
        return new Line(request.getName(), request.getColor());
    }

    private LineResponse toLineResponse(final Line line) {
        final List<Section> sections = sectionRepository.findByLineId(line.getId());
        final Set<Station> stations = new HashSet<>();
        for (Section section : sections) {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        }
        final List<StationResponse> stationResponses =
                stations.stream().map(it -> stationService.createStationResponse(it)).collect(Collectors.toList());
        return new LineResponse(line.getId(), line.getName(), line.getColor(), stationResponses);
    }

    private Line updateLine(final Line line, final LineRequest request) {
        return new Line(line.getId(), request.getName(), request.getColor());
    }

    private SectionResponse toSectionResponse(final Section section) {
        return new SectionResponse(section.getId(), section.getUpStation(), section.getDownStation(),
                section.getDistance());
    }

    private void validateSaveSection(final SectionRequest request, final Long lineId) {
        final List<Section> sections = sectionRepository.findByLineId(lineId);
        if (!isPossibleRegisterAtLast(sections, request)) {
            throw new BadRequestSectionException(UP_STATION_ID_NOT_EQUALS_DOWN_STATION_ID_OF_LAST_SECTION);
        }
        if (isAlreadyRegistered(sections, request.getDownStationId())) {
            throw new BadRequestSectionException(DOWN_STATION_ID_IS_ALREADY_REGISTERED);
        }
    }

    private boolean isPossibleRegisterAtLast(final List<Section> sections, final SectionRequest request) {
        final Section lastSection = Objects.requireNonNull(CollectionUtils.lastElement(sections));
        return Objects.equals(lastSection.getDownStation().getId(), request.getUpStationId());
    }

    private boolean isAlreadyRegistered(final List<Section> sections, final Long stationId) {
        for (Section section : sections) {
            if (section.getUpStation().getId().equals(stationId) ||
                    section.getDownStation().getId().equals(stationId)) {
                return true;
            }
        }

        return false;
    }

    private void validateDeleteSection(final Long lineId, final Long stationId) {
        final List<Section> sections = sectionRepository.findByLineId(lineId);
        if (isLast(sections)) {
            throw new BadRequestSectionException(SECTION_IS_LAST);
        }
        if (!isLastStation(sections, stationId)) {
            throw new BadRequestSectionException(STATION_ID_IS_NOT_LAST);
        }
    }

    private boolean isLast(final List<Section> sections) {
        return sections.size() == 1;
    }

    private boolean isLastStation(final List<Section> sections, final Long stationId) {
        final Section lastSection = Objects.requireNonNull(CollectionUtils.lastElement(sections));
        return lastSection.getDownStation().getId().equals(stationId);
    }
}
