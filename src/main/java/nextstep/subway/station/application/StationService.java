package nextstep.subway.station.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.exception.DuplicateLineException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.exception.DuplicateStationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StationService {
    private StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        try {
            Station persistStation = stationRepository.save(stationRequest.toStation());
            return StationResponse.of(persistStation);
        } catch (DataIntegrityViolationException e){
            throw new DuplicateStationException("Already registered Station");
        }
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStations() {
        List<Station> stations = stationRepository.findAll();
        return stations.stream()
                .map(station -> StationResponse.of(station))
                .collect(Collectors.toList());
    }

    public void deleteStationById(Long id) {
        Optional< Station > optionalStation = stationRepository.findById(id);
        if(optionalStation.isPresent()) {
            stationRepository.deleteById(id);
        }
    }
}
