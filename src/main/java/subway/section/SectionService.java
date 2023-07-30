package subway.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.common.exception.ErrorMessage;
import subway.line.Line;
import subway.line.LineRepository;
import subway.line.LineResponse;
import subway.line.LineService;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.*;

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
    public SectionResponse makeSections(Long lineId, Long upStationId, Long downStationId, Integer distance) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_FOUND_LINE.getMessage()));

        if(line.getDownStation().getId() != upStationId){
            throw new IllegalArgumentException(ErrorMessage.IS_NOT_DOWNSTAION.getMessage());
        }

        Station upStation = getStation(upStationId);

        Station downStation = getStation(downStationId);

        line.addDistance(distance);

        Section section = new Section(upStation, downStation, distance);
        Section sectionResult = sectionRepository.save(section);

        line.addSections(sectionResult);

        return new SectionResponse(sectionResult.getId(), upStation.getId(), downStation.getId(), sectionResult.getDistance());
    }

    @Transactional
    public void delete(Long lineId, Long stationId) {
        Line line = getLine(lineId);
        Station station = getStation(stationId);

        int lastIndex = line.getSections().size()-1;

        if(lastIndex < 0){
            throw new IllegalArgumentException(ErrorMessage.THERE_IS_NO_SECTIONS.getMessage());
        }

        if(!line.getSections().get(lastIndex).getDownStation().getId().equals(station.getId())){
            throw new IllegalArgumentException(ErrorMessage.IS_NOT_LAST_STATION.getMessage());
        }

        line.getSections().remove(lastIndex);

        return;
    }
    public Line getLine(Long lineId){
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_FOUND_LINE.getMessage()));
    }
    public Station getStation(Long stationId){
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_FOUND_STATION.getMessage()));
    }

    public Section getSection(Long sectionId){
        return sectionRepository.findById(sectionId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NOT_FOUND_SECTION.getMessage()));
    }
}
