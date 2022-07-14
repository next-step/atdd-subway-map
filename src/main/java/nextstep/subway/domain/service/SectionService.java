package nextstep.subway.domain.service;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.entity.Line;
import nextstep.subway.domain.entity.Station;
import nextstep.subway.infra.SectionRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;

    public boolean existsByLineAndDownStation(final Line line, final Station downStation) {
        return sectionRepository.existsByLineAndDownStation(line, downStation);
    }

    public boolean existsByLineAndUpStation(final Line line, final Station upStation) {
        return sectionRepository.existsByLineAndUpStation(line, upStation);
    }

    public boolean existsByLineAnyStation(final Line line, final Station station) {
        return sectionRepository.existsByLineAndUpStationOrDownStation(line, station, station);
    }

}
