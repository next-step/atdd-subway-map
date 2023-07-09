package subway.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.exception.SubwayNotFoundException;
import subway.line.constant.LineMessage;
import subway.line.dto.LineCreateRequest;
import subway.line.dto.LineModifyRequest;
import subway.line.dto.LineResponse;
import subway.line.model.Line;
import subway.line.repository.LineRepository;
import subway.station.model.Station;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LineService {


    private final LineRepository lineRepository;

    @Transactional
    public Line saveLine(LineCreateRequest createRequest,
                                 Station upStation,
                                 Station downStation) {
        Line request = LineCreateRequest.to(createRequest, upStation, downStation);
        return lineRepository.save(request);
    }

    @Transactional
    public Line saveLine(Line line) {
        return lineRepository.save(line);
    }

    @Transactional
    public void updateLine(Long id, LineModifyRequest request) {
        Line line = this.findLineById(id);
        line.updateLine(request.getName(), request.getColor());
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(Long id) {
        return lineRepository.findById(id)
                .map(LineResponse::from)
                .orElseThrow(() -> new SubwayNotFoundException(LineMessage.NOT_FOUND_MESSAGE.getCode(),
                        LineMessage.NOT_FOUND_MESSAGE.getMessage()));
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new SubwayNotFoundException(LineMessage.NOT_FOUND_MESSAGE.getCode(),
                        LineMessage.NOT_FOUND_MESSAGE.getMessage()));
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.findById(id)
                .orElseThrow(() -> new SubwayNotFoundException(LineMessage.NOT_FOUND_MESSAGE.getCode(),
                        LineMessage.NOT_FOUND_MESSAGE.getMessage()));
        lineRepository.deleteById(id);
    }
}
