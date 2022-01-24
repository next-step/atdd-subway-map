package nextstep.subway.line.application;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.domain.dto.SectionRequest;
import nextstep.subway.line.domain.model.Line;
import nextstep.subway.line.domain.model.Section;
import nextstep.subway.line.domain.repository.LineRepository;
import nextstep.subway.line.domain.repository.SectionRepository;
import nextstep.subway.station.domain.model.Station;
import nextstep.subway.station.domain.repository.StationRepository;

@Transactional
@Service
public class SectionService {
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public SectionService(LineRepository lineRepository,
                          SectionRepository sectionRepository,
                          StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    public Long addSection(Long lineId, SectionRequest request) {
        Line line = lineRepository.findByIdWithSections(lineId)
                                  .orElseThrow(EntityNotFoundException::new);
        Station upStation = stationRepository.findById(request.getUpStationId())
                                             .orElseThrow(EntityNotFoundException::new);
        Station downStation = stationRepository.findById(request.getDownStationId())
                                               .orElseThrow(EntityNotFoundException::new);

        Section createdSection = line.addSection(upStation, downStation, request.getDistance());
        sectionRepository.save(createdSection);
        return createdSection.getId();
    }

    public void deleteSection(Long lineId, Long sectionId) {
        Line line = lineRepository.findByIdWithSections(lineId)
                                  .orElseThrow(EntityNotFoundException::new);

        line.deleteSection(sectionId);
    }
}
