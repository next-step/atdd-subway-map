package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.StationReadAllResponse;
import nextstep.subway.applicaion.dto.StationReadResponse;
import nextstep.subway.applicaion.dto.StationSaveRequest;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StationService {
    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationReadResponse saveStation(final StationSaveRequest stationRequest) {
        validateDuplicatedStationName(stationRequest.getName());
        final Station station = stationRepository.save(new Station(stationRequest.getName()));
        return new StationReadResponse(station);
    }

    @Transactional(readOnly = true)
    public List<StationReadAllResponse> findAllStations() {
        final List<Station> stations = stationRepository.findAll();

        return stations.stream()
                .map(StationReadAllResponse::new)
                .collect(Collectors.toList());
    }

    public void deleteStationById(final Long id) {
        stationRepository.deleteById(id);
    }

    private void validateDuplicatedStationName(final String stationName) {
        if (stationRepository.existsByName(stationName)) {
            throw new EntityExistsException("duplicate station stationName occurred");
        }
    }
}
