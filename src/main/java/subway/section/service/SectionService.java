package subway.section.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.entity.Line;
import subway.line.repository.LineRepository;
import subway.section.dto.SectionCreateRequest;
import subway.section.dto.SectionResponse;
import subway.section.entity.Section;
import subway.section.repository.SectionRepository;
import subway.station.entity.Station;
import subway.station.repository.StationRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    @Transactional
    public SectionResponse save(SectionCreateRequest request) {
        Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow();
        Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow();
        Line line = lineRepository.findById(request.getLineId()).orElseThrow();

        Section section = request.toEntity(line, upStation, downStation);
        Section saved = sectionRepository.save(section);
        return SectionResponse.from(saved);
    }
}
