package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Line;
import subway.line.domain.Section;
import subway.line.exception.NonExistentLineException;
import subway.line.payload.AddSectionRequest;
import subway.line.payload.CreateLineRequest;
import subway.line.payload.LineResponse;
import subway.line.payload.UpdateLineRequest;
import subway.line.repository.LineRepository;
import subway.station.Station;
import subway.station.StationRepository;
import subway.station.exception.NonExistentStationException;

import java.util.List;


@Transactional
@Service
public class LineCommandService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineCommandService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(final CreateLineRequest request) {
        var upStation = this.getStationById(request.getUpStationId());
        var downStation = this.getStationById(request.getDownStationId());

        Line line = lineRepository.save(
                new Line(request.getName(),
                        request.getColor(),
                        new Section(upStation.getId(), downStation.getId(), request.getDistance())
                ));

        return LineResponse.from(line, getLineStations(line));
    }

    public void modify(final Long id, final UpdateLineRequest request) {
        var line = getLineById(id);
        line.update(request.getName(), request.getColor());
    }

    public void delete(final Long id) {
        var line = getLineById(id);
        lineRepository.delete(line);
    }

    public void addSection(final Long id, final AddSectionRequest request) {
        Line line = getLineById(id);
        var upStation = getStationById(request.getUpStationId());
        var downStation = getStationById(request.getDownStationId());
        line.addSection(upStation.getId(), downStation.getId(), request.getDistance());
    }

    public void removeSection(final Long id, final Long stationId) {
        Line line = getLineById(id);
        line.removeLastStation(stationId);
    }

    private Station getStationById(final Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new NonExistentStationException("존재하지 않는 역입니다."));
    }

    private Line getLineById(final Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new NonExistentLineException("존재하지 않는 지하철 노선입니다."));
    }

    private List<Station> getLineStations(final Line line) {
        return stationRepository.findByIdIn(line.getStationIds());
    }

}
