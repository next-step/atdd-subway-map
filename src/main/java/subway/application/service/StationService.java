package subway.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.NotFoundStationException;
import subway.domain.Station;
import subway.infrastructor.repository.StationJpaEntity;
import subway.infrastructor.repository.StationRepository;
import subway.web.request.StationRequest;
import subway.web.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        StationJpaEntity stationJpaEntity = stationRepository.save(new StationJpaEntity(stationRequest.getName()));
        return createStationResponse(stationJpaEntity);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
            .map(this::createStationResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    private StationResponse createStationResponse(StationJpaEntity stationJpaEntity) {
        return new StationResponse(
            stationJpaEntity.getId(),
            stationJpaEntity.getName()
        );
    }

    public Station findStation(Long stationId) {
        StationJpaEntity stationJpaEntity = stationRepository.findById(stationId).orElseThrow(NotFoundStationException::new);
        return new Station(stationJpaEntity.getId(), stationJpaEntity.getName());
    }

}
