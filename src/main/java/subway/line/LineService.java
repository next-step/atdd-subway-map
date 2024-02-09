package subway.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.section.Section;
import subway.section.SectionRepository;
import subway.station.Station;
import subway.station.StationRepository;
import subway.station.StationResponse;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;
    private SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {

        Station station1 = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow();
        Station station2 = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow();

        Line line = lineRequest.createLine();
        station1.mappingLine(line);
        station2.mappingLine(line);

        line = lineRepository.save(line);

        Section section = lineRequest.createSection();
        section.mappingLine(line);
        sectionRepository.save(section);

        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        List<LineResponse> lineResponses = new ArrayList<>();

        List<Line> lines = lineRepository.findAll();
        for(Line line : lines) {
            List<Station> stations = new ArrayList<>();
            stations.add(stationRepository.findById(line.getUpStationId()).orElseThrow());
            stations.add(stationRepository.findById(line.getDownStationId()).orElseThrow());

            lineResponses.add(createLineResponse(line, stations));
        }

        return lineResponses;
    }

    public LineResponse findLine(Long id) {
        Line line = lineRepository.findById(id).orElseThrow();
        return createLineResponse(line, line.getStations());
    }

    @Transactional
    public void modifyLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow();
        line.changeLineInfo(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    private LineResponse createLineResponse(Line line, List<Station> stations) {
        List<StationResponse> stationResponses = new ArrayList<>();
        for(Station station : stations) {
            stationResponses.add(new StationResponse(station));
        }

        return new LineResponse(line, stationResponses);
    }

    private LineResponse createLineResponse(Line line) {
        List<StationResponse> stationResponses = new ArrayList<>();
        for(Station station : line.getStations()) {
            stationResponses.add(new StationResponse(station));
        }
        return new LineResponse(line, stationResponses);
    }
}
