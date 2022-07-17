package nextstep.subway.applicaion;

import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.SectionCreateRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Distance;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionRepository;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SectionService {

    private final SectionRepository sectionRepository;
    private final LineService lineService;
    private final StationService stationService;

    public SectionService(final SectionRepository sectionRepository, final LineService lineService,
        final StationService stationService) {
        this.sectionRepository = sectionRepository;
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @Transactional
    public SectionResponse addSections(Long id, final SectionCreateRequest request) {
        Line line = lineService.findById(id);

        Station beforeDownStation = line.beforeDownStation(request.getUpStationId());

        Station downStation = stationService.findById(request.getDownStationId());

        Section section = new Section(beforeDownStation, downStation,
            new Distance(request.getDistance()));

        Section savedSection = sectionRepository.save(section);

        line.addSection(savedSection);

        return new SectionResponse(savedSection.getLine().getId(),
            savedSection.getStations().toList().stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList()));
    }

    @Transactional
    public void removeSections(Long id, long stationId) {
        Line line = lineService.findById(id);
        line.removeLastSection(stationId);
    }
}
