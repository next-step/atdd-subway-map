package subway.section;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.Line;
import subway.line.LineService;
import subway.station.Station;
import subway.station.StationService;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final LineService lineService;
    private final StationService stationService;
    private final SectionRepository sectionRepository;

    @Transactional
    public SectionResponse saveSection(Long lineId, SectionRequest request) {
        Line line = lineService.getLineById(lineId);
        Station upStation = stationService.getStationById(request.getUpStationId());
        Station downStation = stationService.getStationById(request.getDownStationId());

        Section section = sectionRepository.save(request.toEntity(line, upStation, downStation));

        return SectionResponse.of(section, line);
    }

    @Transactional(readOnly = true)
    public List<SectionResponse> findAllSectionsByLineId(Long lineId) {
        Line line = lineService.getLineById(lineId);
        return sectionRepository.findAllByLine(line)
            .stream()
            .map(section -> SectionResponse.of(section, line))
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteSection(Long id, Long stationId) {
        Line line = lineService.getLineById(id);
        line.deleteSection(stationId);
    }
}
