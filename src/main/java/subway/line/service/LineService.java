package subway.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.dto.LineUpdateRequest;
import subway.line.entity.Line;
import subway.line.repository.LineRepository;
import subway.section.repository.SectionRepository;
import subway.station.repository.StationRepository;

import java.util.List;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LineService {
    private final LineRepository lineRepository;

    @Transactional
    public Line save(Line entity) {
        return lineRepository.save(entity); // 서비스에서 repository 호출만하고 있는데 contoller에서는 직접 호출하는게 낫지 않을까?
    }

    public List<Line> findAll() {
        return lineRepository.findAll();
    }

    public Line findById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("노선을 조회 할 수 없습니다. id : " + id));
    }

    @Transactional
    public void update(LineUpdateRequest request) {
        Line line = findByIdIfAbsenceThrowException(request.getLineId());
        line.update(request.getName(), request.getColor());
    }

    private Line findByIdIfAbsenceThrowException(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id로 노선을 조회 할 수 없습니다. id" + id));
    }

    @Transactional
    public void delete(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선은 삭제 할 수 없습니다. di:" + id));
        lineRepository.delete(line);
    }
}
