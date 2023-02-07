package subway.station.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.application.dto.request.StationRequest;
import subway.station.domain.Station;
import subway.station.domain.StationCommandRepository;
import subway.station.domain.StationQueryRepository;
import subway.station.exception.StationNotFoundException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StationService {

    private final StationQueryRepository stationQueryRepository;
    private final StationCommandRepository stationCommandRepository;

    public StationService(final StationQueryRepository stationQueryRepository,
                          final StationCommandRepository stationCommandRepository) {
        this.stationQueryRepository = stationQueryRepository;
        this.stationCommandRepository = stationCommandRepository;
    }

    /**
     * 지하철 역 목록 정보를 조회합니다.
     * 
     * @return 지하철 역 목록 정보
     */
    public List<Station> findAllStations() {
        return stationQueryRepository.findAll();
    }

    /**
     * 지하철 역 상세 정보를 조회합니다.
     * 
     * @param stationId 지하철 역 고유 번호
     * @return 지하철 역 상세 정보
     */
    public Station findStationById(final Long stationId) {
        return stationQueryRepository.findById(stationId)
                .orElseThrow(StationNotFoundException::new);
    }

    /**
     * 지하철 역 정보를 등록합니다.
     * 
     * @param stationRequest 등록할 지하철 역 정보
     * @return 등록된 지하철 역 고유 번호
     */
    @Transactional
    public Long saveStation(final StationRequest stationRequest) {
        Station station = stationCommandRepository.save(stationRequest.toEntity());

        return station.getId();
    }

    /**
     * 지하철 역 정보를 삭제합니다.
     * 
     * @param stationId 삭제할 지하철 역 고유 번호
     */
    @Transactional
    public void deleteStationById(final Long stationId) {
        stationCommandRepository.deleteById(stationId);
    }
}
