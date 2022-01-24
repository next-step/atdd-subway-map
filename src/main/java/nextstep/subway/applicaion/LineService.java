package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.*;
import nextstep.subway.exception.DuplicatedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.*;

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
        validateDuplicatedLine(request);

        Station upStation = getStationById(request.getUpStationId());
        Station downStation = getStationById(request.getDownStationId());

        Line line = new Line(request.getName(), request.getColor());
        Section section = new Section(upStation, downStation, request.getDistance());

        line.setSection(section);

        Line saveLine = lineRepository.save(line);
        return new LineResponse(saveLine);
    }

    public List<LineResponse> getAllLines() {
        return lineRepository.findAll()
                .stream()
                .map(LineResponse::new)
                .collect(toList());
    }

    public LineResponse getLine(Long lineId) {
        Line findLine = getLineById(lineRepository, lineId);
        return new LineResponse(findLine);
    }

    public void modifyLine(Long lineId, LineRequest lineRequest) {
        Line findLine = getLineById(lineRepository, lineId);
        findLine.changeLine(lineRequest.getName(), lineRequest.getColor());
    }

    public void deleteLine(Long lineId) {
        Line findLine = getLineById(lineRepository, lineId);
        lineRepository.delete(findLine);
    }

    private Line getLineById(LineRepository lineRepository, Long lineId) {
        return lineRepository
                .findById(lineId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateDuplicatedLine(LineRequest request) {
        boolean existsLine = lineRepository.existsLineByName(request.getName());
        if(existsLine) {
            throw new DuplicatedException("중복된 라인을 생성할 수 없습니다.");
        }
    }

    private Station getStationById(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역 역니다."));
    }


}
