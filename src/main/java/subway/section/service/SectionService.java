package subway.section.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.entity.Line;
import subway.line.repository.LineRepository;
import subway.section.dto.SectionCreateRequest;
import subway.section.entity.Section;
import subway.station.entity.Station;
import subway.station.repository.StationRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SectionService {
    
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    @Transactional
    public Section save(SectionCreateRequest request) {
        Station upStation = stationRepository.findById(request.getUpStationId()).orElseThrow();
        Station downStation = stationRepository.findById(request.getDownStationId()).orElseThrow();
        Line line = lineRepository.findById(request.getLineId()).orElseThrow();

        Section section = request.toEntity(upStation, downStation);
        line.addSection(section); // 더티 체킹으로 쿼리가 나가기 전까지 sectionId가 세팅되지 않으니 주의

        return section;
    }

    @Transactional
    public void delete(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("노선을 찾을 수 없습니다. id : " + lineId));
        line.removeSectionByStationId(stationId);
    }
}
