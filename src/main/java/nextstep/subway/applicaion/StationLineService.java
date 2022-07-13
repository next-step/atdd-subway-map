package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.StationLineRequest;
import nextstep.subway.applicaion.dto.StationLineResponse;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationLine;
import nextstep.subway.domain.StationLineRepository;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationLineService {
    private final StationLineRepository stationLineRepository;
    private final StationRepository stationRepository;

    @Transactional
    public StationLineResponse createStationLine(StationLineRequest stationLineRequest) {
        List<StationResponse> stations = stationRepository.findAllById(List.of(
                stationLineRequest.getUpStationId(),
                stationLineRequest.getDownStationId()))
                .stream()
                .map(station -> StationResponse.of(station))
                .collect(Collectors.toList());

        StationLine stationLine = stationLineRepository.save(StationLine.of(stationLineRequest));

        return StationLineResponse.of(stationLine, stations);
    }

    public List<StationLineResponse> findAllStationLine() {
        return stationLineRepository.findAll()
                .stream()
                .map(stationLine -> StationLineResponse.of(stationLine, findByUpStationAndDownStation(stationLine)))
                .collect(Collectors.toList());
    }

    public StationLineResponse findByStationLineId(Long stationLineId) {
        StationLine stationLine = findByStationLine(stationLineId);
        return StationLineResponse.of(stationLine, findByUpStationAndDownStation(stationLine));
    }

    @Transactional
    public void updateByStationLineId(Long stationLineId, StationLineRequest stationLineRequest) {
        StationLine stationLine = findByStationLine(stationLineId);
        stationLine.update(stationLineRequest.getName(), stationLineRequest.getColor());
    }

    @Transactional
    public void deleteByStationLineId(Long stationLineId) {
        stationLineRepository.deleteById(stationLineId);
    }

    private StationLine findByStationLine(Long stationLineId) {
        return stationLineRepository.findById(stationLineId).get();
    }

    private List<StationResponse> findByUpStationAndDownStation(StationLine stationLine) {
        return stationRepository.findAllById(List.of(
                stationLine.getUpStationId(),
                stationLine.getDownStationId()))
                .stream()
                .map(station -> StationResponse.of(station))
                .collect(Collectors.toList());
    }


}
