package subway.section.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.exception.LineNotFoundException;
import subway.section.domain.Section;
import subway.section.domain.SectionRepository;
import subway.section.dto.SectionRequest;
import subway.section.dto.SectionResponse;
import subway.section.exception.CanNotDeleteOnlyOneSectionException;
import subway.section.exception.DeleteOnlyTerminusStationException;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;
import subway.station.exception.StationNotFoundException;

@Transactional(readOnly = true)
@Service
public class SectionService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public SectionService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public SectionResponse registerSection(Long lineId, SectionRequest sectionRequest) {
        Station upStation = getStation(sectionRequest.getUpStationId());
        Station downStation = getStation(sectionRequest.getDownStationId());

        Section section = new Section(upStation, downStation, sectionRequest.getDistance());
        sectionRepository.save(section);

        Line line = getLine(lineId);
        line.addSection(section);

        return SectionResponse.of(section);
    }

    private Line getLine(Long lineId) {  //TODO: id로 찾는 로직이 중복된다. 해결방안은?
        return lineRepository.findById(lineId)
                .orElseThrow(LineNotFoundException::new);
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(StationNotFoundException::new);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = getLine(lineId);
        validateLineHasOnlyOneSection(line);

        Section lastSection = line.getLastSection();
        validateStationIsDownStationOfLastSection(stationId, lastSection);

        sectionRepository.delete(lastSection);
    }

    private void validateLineHasOnlyOneSection(Line line) {
        if (line.hasOnlyOneSection()) {
            throw new CanNotDeleteOnlyOneSectionException();
        }
    }

    private void validateStationIsDownStationOfLastSection(Long stationId, Section lastSection) {
        Station station = getStation(stationId);
        if (!lastSection.downStationEqualsTo(station)) {
            throw new DeleteOnlyTerminusStationException();
        }
    }
}
