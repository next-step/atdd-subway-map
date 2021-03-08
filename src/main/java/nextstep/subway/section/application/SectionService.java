package nextstep.subway.section.application;

import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.exceptions.NotFoundLineException;
import nextstep.subway.section.domain.Section;
import nextstep.subway.section.domain.SectionRepository;
import nextstep.subway.section.dto.SectionRequest;
import nextstep.subway.section.dto.SectionResponse;
import nextstep.subway.section.exception.NotFoundSectionException;
import nextstep.subway.section.exception.NotLastSectionException;
import nextstep.subway.section.exception.OnlyOneSectionException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.exception.NotFoundStationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class SectionService {

    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public SectionService(SectionRepository sectionRepository, LineRepository lineRepository, StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public SectionResponse saveSection(SectionRequest request, long lineId) {
        Section section = new Section(
            request.getDistance(),
            lineRepository.findById(lineId)
                          .orElseThrow(NotFoundLineException::new),
            stationRepository.findById(request.getUpStationId())
                             .orElseThrow(NotFoundStationException::new),
            stationRepository.findById(request.getDownStationId())
                             .orElseThrow(NotFoundStationException::new)
        );

        return SectionResponse.of(sectionRepository.save(section));
    }

    public void removeSection(long stationId) {
        Station station = stationRepository.findById(stationId)
                                           .orElseThrow(NotFoundStationException::new);

        if (sectionRepository.existsByUpStation(station)) {
            throw new NotLastSectionException();
        }

        Section section = sectionRepository.findFirstByDownStation(station)
                                           .orElseThrow(NotFoundSectionException::new);

        if (!sectionRepository.existsByDownStation(section.getUpStation())) {
            throw new OnlyOneSectionException();
        }

        sectionRepository.delete(section);
    }
}
