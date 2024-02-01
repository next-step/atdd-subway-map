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

        saveSectionValidation(request, line);

        Section section = this.sectionRepository.save(new Section(upStation, downStation, request.getDistance(), line));
        line.changeDownStation(downStation);
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

    private void saveSectionValidation(SectionRequest request, Line line) {
        if(request.getUpStationId() == request.getDownStationId()){
            throw new IllegalArgumentException(String.format("상행역과 하행역은 같을 수 없습니다. 상행역 : %d, 하행역 : %d", request.getUpStationId(), request.getDownStationId()));
        }

        if(line.getDownStation().getId() != request.getUpStationId()){
            throw new IllegalArgumentException(String.format("새로운 구간의 상행역은 해당 노선의 종점역이어야 합니다. 현재 종점역 : %d, 새로운 구간 상행역 : %d", line.getDownStation().getId(), request.getUpStationId()));
        }

        if(line.getSections().stream().anyMatch(section -> section.getUpStation().getId() == request.getDownStationId())){
            throw new IllegalArgumentException(String.format("이미 등록된 구간입니다. 상행역 : %d, 하행역 : %d", request.getUpStationId(), request.getDownStationId()));
        }
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
        Station station = getStation(stationId);

        Section section = this.sectionRepository.findByDownStationId(stationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 구간입니다."));
        deleteSectionValidation(line, station);

        line.changeDownStation(section.getUpStation());
        this.sectionRepository.delete(section);
    }

    private void deleteSectionValidation(Line line, Station station) {
        if(line.getDownStation() != station){
            throw new IllegalArgumentException("노선에 등록된 종점역은 삭제할 수 없습니다.");
        }

        if(line.getSections().size() == 1){
            throw new IllegalArgumentException("노선에 등록된 구간이 하나뿐이므로 삭제할 수 없습니다.");
        }
    }
}
