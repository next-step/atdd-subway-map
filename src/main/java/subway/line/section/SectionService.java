package subway.line.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.Line;
import subway.line.LineRepository;
import subway.station.Station;
import subway.station.StationRepository;

@Service
public class SectionService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public SectionService(LineRepository lineRepository, SectionRepository sectionRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public SectionResponse saveSection(long lineId, SectionRequest request) {
        Line line = getLine(lineId);
        Station upStation = getStation(request.getUpStationId());
        Station downStation = getStation(request.getDownStationId());

        Section section = this.sectionRepository.save(new Section(upStation, downStation, request.getDistance(), line));
        return getSectionResponse(section);
    }

    private Line getLine(long id) {
        return this.lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));
    }

    private Station getStation(long id) {
        return this.stationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역입니다."));
    }


    private SectionResponse getSectionResponse(Section section) {
        return new SectionResponse(section.getId(), section.getUpStation().getId(), section.getDownStation().getId(), section.getDistance());
    }

    public SectionResponse getSection(long id) {
        return this.sectionRepository.findById(id)
                .map(this::getSectionResponse)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 구간입니다."));
    }


    public void deleteSection(Long lineId, Long stationId) {
        Line line = getLine(lineId);
        Section section = this.sectionRepository.findByDownStationId(stationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 구간입니다."));
        line.deleteSection(section);
    }

}
