package subway.line.section;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.exception.NotFoundLineException;
import subway.exception.NotFoundStationException;
import subway.line.Line;
import subway.line.LineRepository;
import subway.station.Station;
import subway.station.StationRepository;

@Service
@RequiredArgsConstructor
public class SectionService {
    private final SectionRepository sectionRepository;

    private final LineRepository lineRepository;

    private final StationRepository stationRepository;

    @Transactional
    public SectionResponse registerSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findByIdWithSection(lineId).orElseThrow(NotFoundLineException::new);


        Station downStation = stationRepository.findById(sectionRequest.getDownStationId())
            .orElseThrow(NotFoundStationException::new);

        Station upStation = stationRepository.findById(sectionRequest.getUpStationId())
            .orElseThrow(NotFoundStationException::new);

        Section section = new Section(line, downStation, upStation, sectionRequest.getDistance());

        line.registerSection(section);

        Section savedSection = sectionRepository.save(section);

        return SectionResponse.of(savedSection);
    }

    @Transactional(readOnly = true)
    public List<SectionResponse> findAllSections(Long lineId) {
        return sectionRepository.findAllByLineId(lineId).stream()
            .map(SectionResponse::of)
            .collect(Collectors.toList());
    }
}
