package nextstep.subway.applicaion.section;
import nextstep.subway.applicaion.common.DuplicatedDownStationException;
import nextstep.subway.applicaion.common.LineNotFoundException;
import nextstep.subway.applicaion.common.UnappropriateUpStationException;
import nextstep.subway.applicaion.line.domain.Line;
import nextstep.subway.applicaion.line.domain.LineRepository;
import nextstep.subway.applicaion.section.domain.Section;
import nextstep.subway.applicaion.section.domain.SectionRepository;
import nextstep.subway.applicaion.section.dto.SectionRequest;
import nextstep.subway.applicaion.station.StationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class SectionService {

    private final StationService stationService;
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public SectionService(StationService stationService, LineRepository lineRepository, SectionRepository sectionRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public void saveSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(lineId).orElse(null);

        if (line == null) {
            throw new LineNotFoundException();
        }

        Long upStationId = sectionRequest.getUpStationId();
        Long downStationId = sectionRequest.getDownStationId();
        checkStationExists(downStationId);
        checkUpStation(line, upStationId);
        checkDownStation(line, downStationId);

        sectionRepository.save(new Section(downStationId, downStationId, sectionRequest.getDistance(), line.getId()));
        line.updateLineStation(upStationId, downStationId);
    }

    private void checkStationExists(Long stationId) {
        stationService.getStationThrowExceptionIfNotExists(stationId);
    }

    private void checkUpStation(Line line, Long upStationId) {
        if (!Objects.equals(line.getDownStationId(), upStationId)) {
            throw new UnappropriateUpStationException();
        }
    }

    private void checkDownStation(Line line, Long downStationId) {
        List<Section> sections = sectionRepository.findAllByLineId(line.getId());
        sections.forEach(it -> {
            if (Objects.equals(it.id, downStationId)) {
                throw new DuplicatedDownStationException();
            }
        });
    }
}
