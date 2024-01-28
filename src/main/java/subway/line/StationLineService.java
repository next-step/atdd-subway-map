package subway.line;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.StationResponse;

@Service
@Transactional(readOnly = true)
public class StationLineService {
    private StationLineRepository stationLineRepository;

    public StationLineService(StationLineRepository stationLineRepository) {
        this.stationLineRepository = stationLineRepository;
    }

    @Transactional
    public StationLineResponse saveStationLine(StationLineRequest stationLineRequest) {
        StationLine stationLine = stationLineRepository.save(new StationLine(stationLineRequest.getName(), stationLineRequest.getColor(), stationLineRequest.getUpStationId(), stationLineRequest.getDownStationId(), stationLineRequest.getDistance()));
        return new StationLineResponse(
                stationLine.getId(),
                stationLine.getName(),
                stationLine.getColor(),
                List.of(
                        new StationResponse(1L, "지하철역"),
                        new StationResponse(2L, "새로운지하철역")
                )
        );
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
                        new StationResponse(1L, "지하철역"),
                        new StationResponse(2L, "새로운지하철역")
                )
        );
    }

    public List<StationLineResponse> findAllStationLine() {
        List<StationLine> stationLines = stationLineRepository.findAll();

        /*return stationLines.stream()  // 상행역, 하행역을 엔티티로 리팩토링하면 아래와 같은 로직으로 수행 가능할 것 같다.
                .map(stationLine -> new StationLineResponse(
                        stationLine.getId(),
                        stationLine.getName(),
                        stationLine.getColor(),
                        List.of(
                                stationLine.getUpStation(),
                                stationLine.getDownStation()
                        )
                ))
                .collect(Collectors.toList());*/

        return List.of(
                new StationLineResponse(stationLines.get(0).getId(), stationLines.get(0).getName(), stationLines.get(0).getColor(), List.of(
                        new StationResponse(1L, "지하철역"),
                        new StationResponse(2L, "새로운지하철역")
                )),
                new StationLineResponse(stationLines.get(1).getId(), stationLines.get(1).getName(), stationLines.get(1).getColor(), List.of(
                        new StationResponse(1L, "지하철역"),
                        new StationResponse(3L, "또다른지하철역")
                ))
        );
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

//    public List<StationResponse> findAllStationLines() {
//        return stationLineRepository.findAll().stream()
//                .map(this::createStationResponse)
//                .collect(Collectors.toList());

//    }
//    @Transactional
//    public void deleteStationById(Long id) {
//        stationLineRepository.deleteById(id);

//    }
//    private StationResponse createStationResponse(Station station) {
//        return new StationResponse(
//                station.getId(),
//                station.getName()
//        );

//    }
//    private StationLineResponse createStationLineResponse(StationLine stationLine) {
//        return new StationLineResponse(
//                stationLine.getId(),
//                stationLine.getName(),
//                stationLine.getColor(),
//                List.of()
//        );

//    }
}
