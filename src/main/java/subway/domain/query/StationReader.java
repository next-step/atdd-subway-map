package subway.domain.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import subway.domain.exception.SubwayDomainException;
import subway.domain.exception.SubwayDomainExceptionType;
import subway.domain.view.StationView;
import subway.domain.entity.Station;
import subway.domain.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StationReader {

    private final StationRepository stationRepository;

    public StationView.Main getOneById(Long id) {
        return stationRepository
                .findById(id)
                .map(this::transform)
                .orElseThrow(() -> new SubwayDomainException(SubwayDomainExceptionType.NOT_FOUND_STATION));
    }

    public List<StationView.Main> getAll() {
        return stationRepository.findAll().stream()
                .map(this::transform)
                .collect(Collectors.toList());
    }

    private StationView.Main transform(Station station) {
        return new StationView.Main(
                station.getId(),
                station.getName()
        );
    }
}
