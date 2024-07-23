package subway.service.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.domain.station.StationRepository;
import subway.dto.line.LineRequest;
import subway.dto.line.LineResponse;

import java.util.ArrayList;
import java.util.List;



@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {

        Line line = lineRepository.save(
                new Line(lineRequest.getName(), lineRequest.getColor()));

        line.addSection(new Section(line, lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance()));

        return LineResponse.createResponse(line, getLineStations(line));
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();
        List<LineResponse> lineResponses = new ArrayList<>();

        for (Line line : lines) {
            lineResponses.add(LineResponse.createResponse(line, getLineStations(line)));
        }

        return lineResponses;
    }

    public LineResponse findLine(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        return LineResponse.createResponse(line, getLineStations(line));
    }
    @Transactional
    public void editLineById(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        line.updateLine(lineRequest.getName(), lineRequest.getColor());
        lineRepository.save(line);

    }
    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }


    public List<Station> getLineStations(Line line) {
        return stationRepository.findByIdIn(line.getStationIds());
    }



}
