package nextstep.subway.domain.service;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.entity.Station;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.infra.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StationService {

    private final StationRepository stationRepository;

    public Station findStation(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("station is not found"));
    }

    public Station save(Station station) {
        return stationRepository.save(station);
    }

    public List<Station> findAll() {
        return stationRepository.findAll();
    }

    public void deleteById(Long id) {
        stationRepository.deleteById(id);
    }
}
