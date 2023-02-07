package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.util.Finder;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Section;
import subway.domain.SectionRepository;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.dto.RegisterSectionRequest;
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

    @Transactional
    public void registerSection(final Long lineId, final RegisterSectionRequest registerSectionRequest) {
        Line line = lineRepository.findByIdWithStation(lineId)
                .orElseThrow(LineNotFoundException::new);

        List<Station> stations = stationRepository.findAllById(
                List.of(registerSectionRequest.getDownStationId(), registerSectionRequest.getUpStationId())
        );

        Station station = finder.findStationById(stations, registerSectionRequest.getDownStationId());
        Station upStation = finder.findStationById(stations, registerSectionRequest.getUpStationId());

        validateRegisterStation(line, station);

        Section section = sectionRepository.findByStationAndLine(line.getDownStation(), line)
                .orElseThrow(SectionNotFoundException::new)
                .updateDownStation(station, upStation);

        sectionRepository.save(section);
    }

    private void validateRegisterStation(final Line line, final Station downStation) {
        sectionRepository.findByStation(downStation).flatMap(Section::getDownStation)
                .flatMap(station -> sectionRepository.findByStationAndLine(station, line))
                .ifPresent(station -> {
                    throw new SectionConstraintException();
                });
    }

    @Transactional
    public void deleteSection(final Long lineId, final Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(LineNotFoundException::new);
        Station station = stationRepository.findById(stationId).orElseThrow(StationNotFoundException::new);
        List<Section> sections = sectionRepository.findAllByLine(line);

        line.canDeleteSection(sections, station);

        Section section = sections.stream()
                .filter(s -> s.equalStation(station))
                .findAny()
                .orElseThrow(SectionNotFoundException::new);

        line.updateDownStation(section.getUpStation().orElseThrow(SectionConstraintException::new));

        sectionRepository.delete(section);
    }
}
