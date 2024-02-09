package subway.line;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.section.StationSection;
import subway.section.StationSectionRequest;
import subway.station.Station;
import subway.station.StationRepository;
import subway.station.StationResponse;

@Service
@Transactional(readOnly = true)
public class StationLineService {
    private StationLineRepository stationLineRepository;
    private StationRepository stationRepository;

    public StationLineService(StationLineRepository stationLineRepository, StationRepository stationRepository) {
        this.stationLineRepository = stationLineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationLineResponse saveStationLine(StationLineRequest stationLineRequest) {
        Station upStation = findStationById(stationLineRequest.getUpStationId());
        Station downStation = findStationById(stationLineRequest.getDownStationId());
        StationLine stationLine = new StationLine(stationLineRequest.getName(), stationLineRequest.getColor(), stationLineRequest.getDistance());
        StationSection stationSection = new StationSection(stationLine, upStation, downStation, stationLineRequest.getDistance());

        stationLine.addStationSection(stationSection);
        StationLine result = stationLineRepository.save(stationLine);
        return new StationLineResponse(result);
    }

    private Station findStationById(long id) {
        return stationRepository.findById(id).stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 역이 존재하지 않습니다"));
    }

    public StationLine findStationLineById(Long id) {
        return stationLineRepository.findById(id).stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("아이디에 맞는 노선이 존재하지 않습니다"));
    }

    public List<StationLineResponse> findAllStationLine() {
        List<StationLine> stationLines = stationLineRepository.findAll();

        return stationLines.stream()  // 상행역, 하행역을 단순 id 값이 아닌 엔티티로 리팩토링하면 아래와 같은 로직으로 수행 가능할 것 같다.
                .map(StationLineResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateStationLineById(Long id, StationLineRequest stationLineRequest) {
        StationLine stationLine = stationLineRepository.findById(id).stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("아이디에 맞는 노선이 존재하지 않습니다"));

        stationLine.updateStationLine(stationLineRequest);
    }

    @Transactional
    public void deleteStationLineById(Long id) {
        stationLineRepository.deleteById(id);
    }

    public void registerStationSection(StationLine stationLine, StationSection stationSection) {
        stationLine.addStationSection(stationSection);
    }

    public void deleteStationSection(StationLine stationLine, Station station) {
        stationLine.deleteStationSection(station);
    }

    public boolean isSingleSection(StationLine stationLine) {
        return stationLine.isSingleSection();
    }

    public boolean isExistSection(StationLine stationLine, StationSection stationSection) {
        return stationLine.isExistSection(stationSection);
    }

    public boolean isConnectedSection(StationLine stationLine, StationSection stationSection) {
        return stationLine.isConnectedSection(stationSection);
    }
}
