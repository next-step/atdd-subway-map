package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.exception.LineDuplicateException;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, SectionRepository sectionRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest request) {

        String name = request.getName();

        if (lineRepository.findByName(name)
                          .isPresent()) {
            throw new LineDuplicateException();
        }

        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        Station upStation = stationRepository.findById(request.getUpStationId())
                                             .orElseThrow(() -> new RuntimeException("존재하지 않는 상행역 입니다."));
        Station downStation = stationRepository.findById(request.getDownStationId())
                                               .orElseThrow(() -> new RuntimeException("존재하지 않는 하행역 입니다."));
        Section section = new Section(line, upStation, downStation);

        line.addSection(section);
        sectionRepository.save(section);

        return new LineResponse(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                    .map(this::createLineResponse)
                    .collect(Collectors.toList());
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(line);
    }

    public LineResponse findLineBy(Long id) {
        //TODO: Exception 처리
        Line findLine = lineRepository.findById(id)
                                      .orElseThrow(() -> new RuntimeException("해당하는 노선을 찾을 수 없습니다."));
        return createLineResponse(findLine);
    }

    public void modifyBy(Long id, LineRequest lineRequest) {
        Line findLine = lineRepository.findById(id)
                                      .orElseThrow(RuntimeException::new);
        findLine.change(lineRequest.getName(), lineRequest.getColor());
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }
}
