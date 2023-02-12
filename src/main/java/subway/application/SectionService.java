package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.util.Finder;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Section;
import subway.domain.SectionRepository;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.dto.SectionRegisterRequest;
import subway.dto.SectionResponse;
import subway.exception.LineNotFoundException;
import subway.exception.SectionConstraintException;
import subway.exception.SectionNotFoundException;
import subway.exception.StationNotFoundException;

@Transactional(readOnly = true)
@Service
public class SectionService {

    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final Finder finder;

    public SectionService(
            final SectionRepository sectionRepository,
            final LineRepository lineRepository,
            final StationRepository stationRepository,
            final Finder finder
    ) {
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.finder = finder;
    }

    public SectionResponse getSection(final Long lineId, final Long stationId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(LineNotFoundException::new);

        Station station = stationRepository.findById(stationId)
                .orElseThrow(StationNotFoundException::new);

        Section section = sectionRepository.findByLineAndDownStation(line, station)
                .orElseThrow(SectionNotFoundException::new);

        return SectionResponse.by(section);
    }

    @Transactional
    public void registerSection(final Long lineId, final SectionRegisterRequest sectionRegisterRequest) {
        Line line = lineRepository.findByIdWithStation(lineId)
                .orElseThrow(LineNotFoundException::new);

        Section section = createSection(sectionRegisterRequest, line);

        line.plusDistance(section.getDistance());

        line.addSection(section);
    }

    private Section createSection(final SectionRegisterRequest sectionRegisterRequest, final Line line) {
        List<Station> stations = stationRepository.findAllById(
                List.of(sectionRegisterRequest.getDownStationId(), sectionRegisterRequest.getUpStationId())
        );

        Station downStation = finder.findStationById(stations, sectionRegisterRequest.getDownStationId());
        Station upStation = finder.findStationById(stations, sectionRegisterRequest.getUpStationId());

        validateRegisterSection(line, downStation, upStation);

        return new Section(sectionRegisterRequest.getDistance(), upStation, downStation, line);
    }

    private void validateRegisterSection(final Line line, final Station downStation, final Station upStation) {
        if (line.isNotEqualDownStation(upStation) || isLineStationsContainDownStation(line, downStation)) {
            throw new SectionConstraintException();
        }
    }

    private boolean isLineStationsContainDownStation(final Line line, final Station downStation) {
        List<Station> lineStations = getLineStations(line);
        return sectionRepository.findByUpStation(downStation).stream()
                .filter(section -> section.getDownStation().isPresent())
                .map(section -> section.getDownStation().get())
                .anyMatch(lineStations::contains);
    }

    private List<Station> getLineStations(final Line line) {
        List<Station> lineStations = sectionRepository.findAllByLine(line).stream()
                .filter(section -> section.getDownStation().isPresent())
                .map(section -> section.getDownStation().get())
                .collect(Collectors.toList());

        lineStations.add(line.getUpStation());

        return lineStations;
    }

    @Transactional
    public void deleteSection(final Long lineId, final Long stationId) {
        Line line = lineRepository.findByIdWithStation(lineId).orElseThrow(LineNotFoundException::new);
        Station station = stationRepository.findById(stationId).orElseThrow(StationNotFoundException::new);
        List<Section> sections = sectionRepository.findAllByLine(line);

        Section section = sections.stream()
                .filter(s -> s.isEqualDownStation(station))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);

        line.setDeleteSection(section, sections, station);

        sectionRepository.delete(section);
    }
}
