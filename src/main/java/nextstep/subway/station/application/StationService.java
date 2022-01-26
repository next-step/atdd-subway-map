package nextstep.subway.station.application;

import nextstep.subway.line.application.manager.LineStationManager;
import nextstep.subway.line.application.manager.StationData;
import nextstep.subway.station.application.dto.StationRequest;
import nextstep.subway.station.application.dto.StationResponse;
import nextstep.subway.station.application.manager.StationLineManager;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static nextstep.subway.common.ErrorMessages.DUPLICATE_STATION_NAME;

@Service
@Transactional
public class StationService implements LineStationManager {
    private final StationRepository stationRepository;
    private final StationLineManager stationLineManager;

    public StationService(StationRepository stationRepository, StationLineManager stationLineManager) {
        this.stationRepository = stationRepository;
        this.stationLineManager = stationLineManager;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        checkExistsStationName(stationRequest.getName());

        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return StationResponse.of(station);
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStations() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteStationById(Long id) {
        Assert.isTrue(!stationLineManager.isExistsByStationId(id), "구간에 사용중인 지하철역은 삭제할 수 없습니다.");
        stationRepository.deleteById(id);
    }


    @Override
    public List<StationData> getAllInStations(Set<Long> stationIds) {
        return stationRepository.findAllByIdIn(stationIds).stream()
                .map(StationData::of).collect(Collectors.toList());
    }

    @Override
    public boolean isExistInStations(Long upStationId, Long downStationId) {
        Set<Long> ids = new HashSet();
        ids.add(upStationId);
        ids.add(downStationId);
        return stationRepository.findAllByIdIn(ids).size() == 2;
    }

    private void checkExistsStationName(String name) {
        Assert.isTrue(!stationRepository.existsByName(name), DUPLICATE_STATION_NAME.getMessage());
    }
}
