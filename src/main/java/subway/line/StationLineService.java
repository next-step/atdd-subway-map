package subway.line;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        StationLine stationLine = stationLineRepository.save(new StationLine(stationLineRequest.getName(), stationLineRequest.getColor(), upStation, downStation, stationLineRequest.getDistance()));
        return new StationLineResponse(
                stationLine.getId(),
                stationLine.getName(),
                stationLine.getColor(),
                List.of(
                        createStationResponse(stationLine.getUpStation()),
                        createStationResponse(stationLine.getDownStation()
                        )
                ));
    }

    private Station findStationById(long id) {
        return stationRepository.findById(id).stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 역이 존재하지 않습니다"));
    }

    public StationLineResponse findStationLineById(Long id) {
        StationLine stationLine = stationLineRepository.findById(id).stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("아이디에 맞는 노선이 존재하지 않습니다"));

        return new StationLineResponse(
                stationLine.getId(),
                stationLine.getName(),
                stationLine.getColor(),
                List.of(
                        createStationResponse(stationLine.getUpStation()),
                        createStationResponse(stationLine.getDownStation()
                        )
                ));
    }

    public List<StationLineResponse> findAllStationLine() {
        List<StationLine> stationLines = stationLineRepository.findAll();

        return stationLines.stream()  // 상행역, 하행역을 단순 id 값이 아닌 엔티티로 리팩토링하면 아래와 같은 로직으로 수행 가능할 것 같다.
                .map(stationLine -> new StationLineResponse(
                        stationLine.getId(),
                        stationLine.getName(),
                        stationLine.getColor(),
                        List.of(
                                createStationResponse(stationLine.getUpStation()),
                                createStationResponse(stationLine.getDownStation())
                        )
                )).collect(Collectors.toList());
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

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }
}
