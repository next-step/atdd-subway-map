package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionStation;
import nextstep.subway.domain.SectionStationRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class SectionStationCommandService {

    private final SectionStationRepository sectionStationRepository;

    public void saveSectionStation(Section section, Station upStation, Station downStation) {
        sectionStationRepository.save(new SectionStation(section, upStation));
        sectionStationRepository.save(new SectionStation(section, downStation));
    }
}
