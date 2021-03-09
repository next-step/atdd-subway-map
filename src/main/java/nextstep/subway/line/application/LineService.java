package nextstep.subway.line.application;

import nextstep.subway.line.DuplicatedLineNameException;
import nextstep.subway.line.NotFoundLineException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        if(lineRepository.existsLineByName(request.getName())){
            throw new DuplicatedLineNameException(request.getName());
        }
        Line persistLine = lineRepository.save(request.toLine());
        return LineResponse.of(persistLine);
    }

    public LineResponse findLineById(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundLineException(id));
        return LineResponse.of(line);
    }

    public void updateLine(Long id, LineRequest updateRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundLineException(id));
        line.update(updateRequest.toLine());
    }

    public void deleteLineById(Long id) {
        lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundLineException(id));
        lineRepository.deleteById(id);
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(line -> LineResponse.of(line))
                .collect(Collectors.toList());
    }

    public void addLineStation(Long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(lineId)
                        .orElseThrow(() -> new NotFoundLineException(lineId));

        Station upStation = stationRepository.findById(sectionRequest.getUpStationId()).get();
        Station downStation = stationRepository.findById(sectionRequest.getDownStationId()).get();

        line.addSection(new Section(line, upStation, downStation,sectionRequest.getDistance()));
    }

    public void removeSection(Long lineId, Station station) {
        Line line = lineRepository.findById(lineId)
                    .orElseThrow(() -> new NotFoundLineException(lineId));
        line.removeSection(station);
    }
}
