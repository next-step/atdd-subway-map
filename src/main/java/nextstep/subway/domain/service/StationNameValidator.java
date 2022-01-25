package nextstep.subway.domain.service;

import nextstep.subway.domain.entity.Station;
import nextstep.subway.domain.repository.StationRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StationNameValidator {

    private final StationRepository stationRepository;

    public StationNameValidator(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public void validate(final String name) {
        final List<Station> stations = stationRepository.findByName(name);
        if (!stations.isEmpty()) {
            throw new DuplicateArgumentException("중복된 이름 입니다.");
        }
    }
}
