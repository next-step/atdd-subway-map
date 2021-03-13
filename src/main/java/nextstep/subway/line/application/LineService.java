package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.section.application.SectionService;
import nextstep.subway.section.domain.Section;
import nextstep.subway.section.dto.SectionRequest;
import nextstep.subway.section.dto.SectionResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private LineRepository lineRepository;

    @Autowired
    StationService stationService;

    @Autowired
    SectionService sectionService;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineRepository.save(request.toLine());
        sectionService.createSection(
                persistLine.getId(),
                new SectionRequest(
                        request.getUpStationId(),
                        request.getDownStationId(),
                        request.getDistance()
                )
        );

        return lineToLineResponse(persistLine);
    }

    public List<LineResponse> getLines() {
        return lineRepository.findAll()
                .stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse getLine(Long id) {
        Line line = getLineById(id);
        LineResponse lineResponse = lineToLineResponse(line);
        lineResponse.setStations(getStationListFromSections(id));

        return lineResponse;
    }

    public List<Station> getStationListFromSections(Long lineId){
        List<Station> stations = new ArrayList<>();
        findFirstSection(lineId).ifPresent(section -> {
            stations.add(section.getUpStation());
            while (true) {
                stations.add(section.getDownStation());
                Optional<Section> optNextSection =
                        findNextSection(lineId, section.getDownStation());

                if (optNextSection.isPresent()) {
                    section = optNextSection.get();
                } else {
                    break;
                }
            }
        });
        return stations;
    }

    public Optional<Section> findFirstSection(Long lineId) {
        return sectionService.getFirstSection(lineId);
    }

    public Optional<Section> findNextSection(Long lineId, Station downStation) {
        return sectionService.getNextSection(lineId, downStation);
    }


    public LineResponse updateLine(Long id, LineRequest request) {
        Line line = getLineById(id);
        line.update(request.toLine());
        return lineToLineResponse(line);
    }

    public Line getLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    private LineResponse lineToLineResponse(Line line) {
        return LineResponse.of(line);
    }
}
