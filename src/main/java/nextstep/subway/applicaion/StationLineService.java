package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.applicaion.dto.StationLineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.StationLineRepository;
import nextstep.subway.exception.LineNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StationLineService {

    private final StationLineRepository repository;

    public StationLineResponse save(Line line) {
        return StationLineResponse.form(repository.save(line));
    }

    public List<StationLineResponse> findAllStationLines() {
        return repository.findAll()
                         .stream()
                         .map(StationLineResponse::form)
                         .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StationLineResponse findById(Long id) {
       return StationLineResponse.form(findLineById(id));
    }

    public void update(Line line) {
        StationLineResponse.form(repository.save(line));
    }

    public void deleteLineById(Long id) {
        repository.deleteById(id);
    }

    public SectionResponse addSection(Long lineId, SectionRequest.PostRequest request) {
        Section section = request.toEntity();
        Line line = findLineById(lineId);
        line.addSection(section);
        repository.save(line);
        return SectionResponse.form(line.findLastSection());
    }


    private Line findLineById(Long lineId) {
        return repository.findById(lineId).orElseThrow(() -> new LineNotFoundException("등록되지 않은 노선 입니다."));
    }
}
