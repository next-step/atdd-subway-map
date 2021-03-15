package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.line.dto.SectionResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationsRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationsRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Station upStation = stationRepository.getOne(request.getUpStationId());
        Station downStation = stationRepository.getOne(request.getDownStationId());

        Line line = Line.of(request.getName(),request.getColor(),upStation,downStation,request.getDistance());

        return LineResponse.of(lineRepository.save(line));
    }

    public List<LineResponse> findAllLines() {
        List<Line> allLines = lineRepository.findAll();

        return allLines.stream()
                .map(line -> LineResponse.of(line))
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id){
        return LineResponse.of(lineRepository.getOne(id));
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    public LineResponse updateLine(Long id, LineRequest request){
        Line originLine = lineRepository.getOne(id);
        Line updateLine = request.toLine();
        originLine.update(updateLine);

        return LineResponse.of(originLine);
    }

    public LineResponse addSection(long lineId, SectionRequest request){
        Line targetLine = lineRepository.getOne(lineId);

        Station upStation = stationRepository.getOne(request.getUpStationId());
        Station downStation = stationRepository.getOne(request.getDownStationId());

        targetLine.addSection(upStation,downStation,request.getDistance());

        return  LineResponse.of(targetLine);
    }
    public void deleteSection(long lineId, long targetStationId){
        Line targetLine = lineRepository.getOne(lineId);
        targetLine.deleteSection(targetStationId);
    }

    public SectionResponse findLastSection(long lineId){
        Line targetLine = lineRepository.getOne(lineId);
        Section lastSection = targetLine.getLastSection();
        return SectionResponse.of(lastSection);
    }
}
