package subway.section.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.exception.NotFoundException;
import subway.line.entity.Line;
import subway.line.repository.LineRepository;
import subway.section.entity.Section;
import subway.section.repository.SectionRepository;
import subway.section.request.SectionCreateRequest;
import subway.section.response.SectionResponse;
import subway.station.entity.Station;
import subway.station.repository.StationRepository;
import subway.station.response.StationResponse;

@Service
@Transactional(readOnly = true)
public class SectionService {

    private SectionRepository sectionRepository;
    private StationRepository stationRepository;
    private LineRepository lineRepository;

    public SectionService(SectionRepository sectionRepository, StationRepository stationRepository, LineRepository lineRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public SectionResponse saveSection(Long lineId, SectionCreateRequest sectionCreateRequest) {
        Station upStation = getStationById(Long.valueOf(sectionCreateRequest.getUpStationId()));
        Station downStation = getStationById(Long.valueOf(sectionCreateRequest.getDownStationId()));

        Line line = getLineById(lineId);

        Section section = sectionRepository.save(Section.builder()
                .line(line)
                .upStation(upStation)
                .downStation(downStation)
                .distance(sectionCreateRequest.getDistance())
                .build());

        return SectionResponse.builder()
                .id(section.getId())
                .lineId(section.getLine().getId())
                .upStations(StationResponse.builder()
                        .id(section.getUpStation().getId())
                        .name(section.getUpStation().getName())
                        .build())
                .downStations(StationResponse.builder()
                        .id(section.getDownStation().getId())
                        .name(section.getDownStation().getName())
                        .build())
                .distance(section.getDistance())
                .build();
    }

    private Station getStationById(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new NotFoundException("지하철역이 존재하지 않습니다."));
    }

    private Line getLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("지하철 노선이 존재하지 않습니다."));
    }
}
