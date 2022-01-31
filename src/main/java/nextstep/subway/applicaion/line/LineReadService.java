package nextstep.subway.applicaion.line;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.LineReadResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LineReadService {
    private final LineRepository lineRepository;

    public LineReadService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public List<LineReadResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(line -> makeLineReadResponse(line))
                .collect(Collectors.toList());
    }

    public LineReadResponse findSpecificLine(Long id) {
        return lineRepository
                .findById(id)
                .map(line -> makeLineReadResponse(line))
                .orElseThrow(NotFoundException::new);
    }

    private LineReadResponse makeLineReadResponse(Line line){
        return new LineReadResponse(line.getId(), line.getName(), line.getColor(), makeLineStationResponse(line.getSections()), line.getCreatedDate(), line.getModifiedDate());
    }

    private List<StationResponse> makeLineStationResponse(List<Section> sections){
        if(sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<StationResponse> stationResponses = new ArrayList<>();

        Station firstStation = sections.get(0).getUpStation();
        stationResponses.add(StationResponse.of(firstStation));

        for(Section section: sections){
            stationResponses.add(StationResponse.of(section.getDownStation()));
        }

        return Collections.unmodifiableList(stationResponses);
    }
}
