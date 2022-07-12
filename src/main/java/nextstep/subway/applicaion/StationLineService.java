package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.StationLineRequest;
import nextstep.subway.applicaion.dto.StationLineResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationLine;
import nextstep.subway.domain.StationLineRepository;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
        StationLine stationLine = stationLineRepository.save(stationLineRequest.toEntity());
        return createStationLineResponse(stationLine);
    }

    public StationLineResponse findStationLine(Long id) {
        StationLine stationLine = getStationLineOrThrow(id);
        return createStationLineResponse(stationLine);
    }

    private StationLine getStationLineOrThrow(Long id) {
        return stationLineRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("해당 %d의 id 값을 가진 StationLine은 존재하지 않습니다.", id)));
    }

    public List<StationLineResponse> findAllStationLines() {
        return stationLineRepository.findAll().stream()
                .map(this::createStationLineResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationLineById(Long id) {
        stationLineRepository.deleteById(id);
    }

    @Transactional
    public void updateStationLine(Long id, StationLineRequest stationLineRequest) {
        StationLine existedStationLine = getStationLineOrThrow(id);
        existedStationLine.updateByStationLineRequest(stationLineRequest);
    }

    private StationLineResponse createStationLineResponse(StationLine stationLine) {
        List<Station> stations = findRelatedStations(stationLine);

        return new StationLineResponse(
                stationLine.getId(),
                stationLine.getName(),
                stationLine.getColor(),
                stations
        );
    }

    private List<Station> findRelatedStations(StationLine stationLine) {
        List<Station> stations = new ArrayList<>();
        Station upStation = getStationOrThrow(stationLine.getUpStationId());
        Station downStation = getStationOrThrow(stationLine.getDownStationId());
        stations.add(upStation);
        stations.add(downStation);
        return stations;
    }

    private Station getStationOrThrow(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("해당 %d의 id 값을 가진 Station은 존재하지 않습니다.", id)));
    }
}
