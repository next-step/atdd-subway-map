package nextstep.subway.line.application;

import nextstep.subway.common.exception.InvalidRequestException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest request) {
        if(isExistLineName(request.getName())) {
            throw new InvalidRequestException("이미 존재하는 노선명 입니다.");
        }

        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());

        Line persistLine = lineRepository.save(new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance()));
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findLineAll() {
        List<Line> lineList = lineRepository.findAll();
        return lineList.stream()
                .map(line -> LineResponse.of(line))
                .collect(Collectors.toList());
    }

    public boolean isExistLineName(String name) {
        return lineRepository.existsLineByName(name);
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(() -> new InvalidRequestException("라인이 존재하지 않습니다."));
    }

    public void updateLine(Long id, LineRequest lineRequest) {
        Line addedLine = lineRepository.findById(id).orElseThrow(() -> new InvalidRequestException("업데이트 할 라인이 등록되어 있지 않습니다."));
        addedLine.update(lineRequest.toLine());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    public void addSection(Long id, SectionRequest sectionRequest) {
        final Line line = findLineById(id);
        final Station inputUpStation = stationService.findStationById(sectionRequest.getUpStationId());
        final Station inputDownStation = stationService.findStationById(sectionRequest.getDownStationId());
        line.addSection(sectionRequest, inputUpStation, inputDownStation);
        return;
    }

    public void deleteSection(Long id, Long stationId) {
        final Line line = findLineById(id);
        final Station inputStation = stationService.findStationById(stationId);
        line.deleteSection(inputStation);
    }
}
