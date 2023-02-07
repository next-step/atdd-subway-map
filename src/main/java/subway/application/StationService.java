package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.SectionRepository;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.exception.StationNotFoundException;

@Service
@Transactional(readOnly = true)
public class StationService {

    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public StationService(final StationRepository stationRepository, final SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public StationResponse saveStation(final StationRequest stationRequest) {
        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return StationResponse.by(station);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
                .map(StationResponse::by)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(final Long id) {
        Station station = stationRepository.findById(id)
                .orElseThrow(StationNotFoundException::new);

        sectionRepository.deleteAllByStation(station);

        stationRepository.delete(station);
    }
}
