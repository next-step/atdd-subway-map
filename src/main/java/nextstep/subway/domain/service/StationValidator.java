package nextstep.subway.domain.service;

import nextstep.subway.domain.entity.Station;
import nextstep.subway.domain.repository.StationRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StationValidator implements Validator<Station> {

    private final StationRepository stationRepository;

    public StationValidator(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public void validate(final Station station) {
        validateName(station.getName());
    }

    private void validateName(final String name) {
        final List<Station> stations = stationRepository.findByName(name);
        if (!stations.isEmpty()) {
            throw new DuplicateArgumentException("중복된 이름 입니다.");
        }
    }
}
