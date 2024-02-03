package subway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import subway.controller.dto.SectionCreateRequest;
import subway.domain.*;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public Long createSection(Long lineId, SectionCreateRequest request) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("노선이 존재하지 않습니다."));
        Sections sections = new Sections(sectionRepository.findByLine(line));

        Stations stations = new Stations(stationRepository.findByIdIn(request.stationIds()));
        Station upStation = stations.findBy(Long.parseLong(request.getUpStationId()));
        Station downStation = stations.findBy(Long.parseLong(request.getDownStationId()));
        sections.validateRegisterStationBy(upStation, downStation);
        sections.validateLineDistance(request.getDistance());

        Section section = sectionRepository.save(
                new Section(line, upStation, downStation, sections.calculateSectionDistance(request.getDistance()))
        );

        return section.id();
    }

}
