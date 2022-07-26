package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationService {
    private StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return createStationResponse(station);
    }

    // 모든 지하철역 조회
    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
                .map(this::createStationResponse)
                .collect(Collectors.toList());
    }


    // 상행 하행선 응답객체
    public List<StationResponse> getUpAndDownStationResponses(Long upStationId, Long downStationId) {
        StationResponse upStationResponse = stationRepository.findById(upStationId).map(StationResponse::new)
                .orElseThrow(() -> new EntityNotFoundException(upStationId + "번 id로 조회되는 역이 없습니다."));

        StationResponse downStationResponse = stationRepository.findById(downStationId).map(StationResponse::new)
                .orElseThrow(() -> new EntityNotFoundException(downStationId + "번 id로 조회되는 역이 없습니다."));

        List<StationResponse> stationResponses = new ArrayList<>();

        stationResponses.add(upStationResponse);
        stationResponses.add(downStationResponse);

        return stationResponses;
    }


    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }

    public List<StationResponse> createStationResponses(Set<Long> stationIds) {
        return stationRepository.findAllById(stationIds).stream().map(StationResponse::new).collect(Collectors.toList());
    }




}
