package subway.lines;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.stations.Station;
import subway.stations.StationRepository;
import subway.stations.StationResponse;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;
    
    public LineService(LineRepository lineRepository, StationRepository stationRepository)
    {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest)
    {
        Line line = this.lineRepository.save(new Line(
            lineRequest.getName(), lineRequest.getColor(), 
            lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance()));
        return this.createLineResponse(line); 
    }

    public List<LineResponse> findAllLines()
    {
        return this.lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long modifyLine(Long id, LineRequest lineRequest)
    {
        Optional<Line> oLine = this.lineRepository.findById(id);
        if(!oLine.isPresent())
            return -1l;

        Line line = oLine.get();
        line.setName(lineRequest.getName());
        line.setColor(lineRequest.getColor());
        line.setUpStationId(lineRequest.getUpStationId());
        line.setDownStationId(lineRequest.getDownStationId());
        line.setDistance(lineRequest.getDistance());
        this.lineRepository.save(line);
        return id;
    }

    @Transactional
    public void deleteLineById(Long id)
    {
        this.lineRepository.deleteById(id);
    }

    private LineResponse createLineResponse(Line line)
    {
        List<StationResponse> stations = new LinkedList<>();
        stations.add(this.findStation(line.getUpStationId()));
        stations.add(this.findStation(line.getDownStationId()));

        return new LineResponse(
            line.getId(), line.getName(), line.getColor(), line.getDistance(), stations
        );
    }

    public LineResponse findLine(Long id)
    {
        return this.createLineResponse(this.lineRepository.findById(id).get());
    }

    public StationResponse findStation(Long id)
    {
        return this.createStationResponse(this.stationRepository.findById(id).get());
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }
}
