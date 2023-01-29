package subway.section.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.entity.Line;
import subway.line.repository.LineRepository;
import subway.section.entity.Section;
import subway.station.repository.StationRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SectionService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    @Transactional
    public Section save(Long lineId, Section section) {
        Line line = lineRepository.findById(lineId).orElseThrow();
        line.addSection(section);
        return line.getLastSection();
    }

    @Transactional
    public void delete(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("노선을 찾을 수 없습니다. id : " + lineId));
        line.removeSectionByStationId(stationId);
    }
}
