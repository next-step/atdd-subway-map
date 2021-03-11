package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        validateReduplicationLine(request);
        Station upStation = stationRepository.findById(request.getUpStationId()).get();
        Station downStation = stationRepository.findById(request.getDownStationId()).get();
        Line persistLine = lineRepository.save(request.toLine(upStation, downStation));
        return LineResponse.of(persistLine);
    }

    @Transactional(readOnly = true)
    public void validateReduplicationLine(LineRequest request) {
        Lines lines = Lines.of(lineRepository.findByName(request.getName()));
        lines.validateExistLine();
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        Lines lines = Lines.of(lineRepository.findAll());
        return lines.toResponses();
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(Long id) {
        Line line = lineRepository.findById(id).get();

        List<StationResponse> stations = line.getSortedStations();

        return LineResponse.of(line, stations);
    }

    public LineResponse updateLineById(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).get();
        line.update(lineRequest.toLine());
        return LineResponse.of(line);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public LineResponse saveSections(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).get();
        addLineSections(lineRequest, line);
        List<StationResponse> stations = line.getSortedStations();
        return LineResponse.of(line, stations);
    }

    private void addLineSections(LineRequest lineRequest, Line line) {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).get();
        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).get();
        line.validateStationAdd(upStation, downStation);
        line.addSections(Section.of(line, upStation, downStation, lineRequest.getDistance()));
    }

    public void deleteSectionById(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).get();
        line.validateStationDelete(stationId);
        stationRepository.deleteById(stationId);
        line.removeLastSections();
    }
}
