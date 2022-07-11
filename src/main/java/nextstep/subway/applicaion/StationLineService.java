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

    public StationLineResponse findByStationLineId(Long id) {
        StationLine stationLine = stationLineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지하철 노선입니다."));

        return StationLineResponse.of(stationLine, findByUpStationAndDownStation(stationLine));
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
