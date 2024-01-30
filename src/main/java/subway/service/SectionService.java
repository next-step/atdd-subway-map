package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.SectionAddRequest;
import subway.controller.dto.SectionResponse;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Section;
import subway.domain.SectionRepository;
import subway.domain.Station;
import subway.domain.StationRepository;

import javax.persistence.EntityNotFoundException;

@Transactional(readOnly = true)
@Service
public class SectionService {

    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public SectionService(SectionRepository sectionRepository, LineRepository lineRepository, StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public SectionResponse addSection(Long lineId, SectionAddRequest request) {
        Line line = findLineById(lineId);

        Station upStation = findStationById(request.getUpStationId());
        Station downStation = findStationById(request.getDownStationId());

        Section section = Section.of(
            line,
            upStation,
            downStation,
            request.getDistance()
        );

        line.verifyAddableSection(section);

        return SectionResponse.ofEntity(sectionRepository.save(section));
    }


    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = findLineById(lineId);
        Station station = findStationById(stationId);

        Section section = sectionRepository.findByLineIdAndDownStationId(lineId, stationId)
            .orElseThrow(EntityNotFoundException::new);

        line.verifyDeletableStation(station);
        line.removeSection(section);

        sectionRepository.deleteById(section.getId());
    }

    private Line findLineById(Long lindId) {
        return lineRepository.findById(lindId)
            .orElseThrow(EntityNotFoundException::new);
    }

    private Station findStationById(Long stationId) {
        return stationRepository.findById(stationId)
            .orElseThrow(EntityNotFoundException::new);
    }
}
