package subway.line;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.exception.NotFoundLineException;
import subway.line.section.LineSectionRepository;
import subway.line.section.LineSectionService;
import subway.section.SectionRepository;
import subway.line.station.LineStationRepository;
import subway.line.station.LineStationService;
import subway.station.StationRepository;

@Service
@RequiredArgsConstructor
public class LineService {
    private final LineRepository lineRepository;

    private final StationRepository stationRepository;

    private final LineStationRepository lineStationRepository;

    private final SectionRepository sectionRepository;

    private final LineSectionRepository lineSectionRepository;

    private final LineSectionService lineSectionService;

    private final LineStationService lineStationService;

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = createAndSaveLine(lineRequest);

        lineStationService.registerLineStations(line.getId(), lineRequest.getStationIds());

        lineSectionService.registerLineSection(
            line.getId(),
            lineRequest.getUpStationId(),
            lineRequest.getDownStationId(),
            lineRequest.getDistance()
        );

        return LineResponse.of(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        return lineRepository.findAllWithLineStations().stream()
            .map(LineResponse::of)
            .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public Optional<LineResponse> findById(Long id) {
        return lineRepository.findByIdWithLineStations(id)
            .map(LineResponse::of);
    }

    @Transactional
    @Modifying(clearAutomatically = true)
    public void modifyLine(Long id, LineModifyRequest lineModifyRequest) {
        Line line = lineRepository.findById(id).orElseThrow(NotFoundLineException::new);
        line.modify(lineModifyRequest);
    }

    @Transactional
    @Modifying(clearAutomatically = true)
    public void deleteLine(Long id) {
        Line findLine = lineRepository.findById(id).orElseThrow(NotFoundLineException::new);

        lineRepository.delete(findLine);
    }

    private Line createAndSaveLine(LineRequest lineRequest) {
        Line line = new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getDistance());

        return lineRepository.save(line);
    }

//    private List<LineStation> createAndSaveLineStations(Line line, LineRequest lineRequest) {
//        List<Station> stations = stationRepository.findByIdIn(lineRequest.getStationIds());
//
//        List<LineStation> lineStations = stations.stream()
//            .map(station -> new LineStation(line, station))
//            .collect(Collectors.toList());
//
//        List<LineStation> savedLineStations = lineStationRepository.saveAll(lineStations);
//
//        savedLineStations.forEach(line::addLineStation);
//
//        return savedLineStations;
//    }

//    private LineSection createAndSaveLineSection(Line line, LineRequest lineRequest) {
//        Section section = sectionRepository.findSectionByUpStationIdAndDownStationId(
//            lineRequest.getUpStationId(),
//            lineRequest.getDownStationId()
//        ).orElseThrow(NotFoundSectionException::new);
//
//        LineSection lineSection = new LineSection(line, section);
//
//        LineSection savedLineSection = lineSectionRepository.save(lineSection);
//
//        line.addLineSection(savedLineSection);
//
//        return savedLineSection;
//    }
}
