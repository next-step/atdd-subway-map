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
import subway.section.exception.AlreadyRegisteredStationException;
import subway.section.exception.InvalidSectionRegistrationException;
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
        Line line = getLine(lineId);
        Section lastSection = line.getLastSection();

        Station upStation = getStation(sectionRequest.getUpStationId());
        validateLastStationEqualToNewUpStation(lastSection, upStation);

        Station downStation = getStation(sectionRequest.getDownStationId());
        validateDuplicationOfStationInLine(line, downStation);

        Section section = new Section(line, upStation, downStation, sectionRequest.getDistance());
        sectionRepository.save(section);

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

    private void validateLastStationEqualToNewUpStation(Section lastSection, Station upStation) {
        if (!lastSection.downStationEqualsTo(upStation)) {
            throw new InvalidSectionRegistrationException();
        }
    }

    private void validateDuplicationOfStationInLine(Line line, Station downStation) {
        if (line.hasStation(downStation)) {
            throw new AlreadyRegisteredStationException();
        }
    }
}
