package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.line.domain.Line.createLine;
import static nextstep.subway.line.domain.Section.createSection;

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
        Line persistLine = lineRepository.save(request.toLine());
        return LineResponse.of(persistLine);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllStations() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(line -> LineResponse.of(line))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findStationById(Long id) {
        Line line = lineRepository.getOne(id);
        return LineResponse.of(line);
    }

    public void modifyLine(LineRequest request, Long id) {
        Line line = lineRepository.getOne(id);
        line.update(request.getName(), request.getColor());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    // TODO 예외처리..? : repository 안들리는것도 @Transactional(readOnly = true) 해야하나?
    public Line lineRequestToLine(LineRequest lineRequest){
        Station upStation = stationRepository.getOne(lineRequest.getUpStationId());
        Station downStation = stationRepository.getOne(lineRequest.getDownStationId());
        return createLine(lineRequest.getName(), lineRequest.getColor(), upStation, downStation, lineRequest.getDistance());
    }

    public LineResponse registerLineSection(Long id, LineRequest request) {
        Line line = lineRepository.getOne(id);

        Long upStationId = request.getUpStationId();
        Long downStationId = request.getDownStationId();

        // 벨리데이션
        if(line.sectionSize() != 0 && (line.getLastStationId() != request.getUpStationId())) throw new IllegalArgumentException("새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 한다.");
        if(line.isContainsStation(downStationId)) throw new IllegalArgumentException("새로운 구간의 하행역은 현재 등록되어있는 역일 수 없다.");

        // 구간 생성
        Station upStation = stationRepository.getOne(upStationId);
        Station downStation = stationRepository.getOne(downStationId);

        // 노선 업데이트
        line.addSection(upStation, downStation, request.getDistance());
        line.update(line.getName(), line.getColor());

        return LineResponse.of(line);
    }
}
