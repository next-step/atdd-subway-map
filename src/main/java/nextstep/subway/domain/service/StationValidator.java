package nextstep.subway.domain.service;

import nextstep.subway.domain.entity.Station;
import nextstep.subway.domain.repository.StationRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class StationValidator implements Validator<Station> {

    private final StationRepository stationRepository;

    public StationValidator(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public void validate(final Station station) {
        validateName(station);
    }

    private void validateName(final Station station) {
        final List<Station> stations = stationRepository.findByName(station.getName()).stream()
                .filter(Predicate.not(station::equals))
                .collect(Collectors.toList());
        if (!stations.isEmpty()) {
            throw new DuplicateArgumentException("중복된 이름 입니다.");
        }
    }
}
