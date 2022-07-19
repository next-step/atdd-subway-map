package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineCreationRequest;
import nextstep.subway.applicaion.dto.LineModificationRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.repository.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class LineCommandService {

    private static final String NO_SUCH_LINE_FORMAT = "요청한 노선은 존재하지 않는 노선입니다. (요청한 id: %d)";
    private final LineRepository lineRepository;

    public LineResponse saveLine(LineCreationRequest lineRequest, Station upStation, Station downStation) {
        Line line = lineRepository.findByName(lineRequest.getName())
                .orElseGet(() -> lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor())));
        line.addSection(upStation, downStation, lineRequest.getDistance());
        return LineResponse.from(line);
    }

    public void modifyLine(Long lineId, LineModificationRequest lineRequest) {
        lineRepository.findById(lineId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format(NO_SUCH_LINE_FORMAT, lineId)
                ))
                .updateNameAndColor(lineRequest.getName(), lineRequest.getColor());
    }

    public void deleteLineById(Long lineId) {
        lineRepository.deleteById(lineId);
    }

}
