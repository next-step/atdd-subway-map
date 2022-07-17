package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.LineUpdateRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(new Line.Builder()
                                        .name(lineRequest.getName())
                                        .color(lineRequest.getColor())
                                        .upStationId(lineRequest.getUpStationId())
                                        .downStationId(lineRequest.getDownStationId())
                                        .build());

        return createLineResponse(line);
    }

    private LineResponse createLineResponse(Line line) {

        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                stationService.getUpAndDownStationResponses(line.getUpStationId(), line.getDownStationId())
        );
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public Line findLine(Long lineId) {
        return lineRepository.findById(lineId).orElseThrow(() -> new EntityNotFoundException(lineId + "번 id로 조회되는 노선이 없습니다."));
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest updateDto) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("manager");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        Line line = findLine(id);
        line.updateLine(updateDto.getName(), updateDto.getColor());
        tx.commit();
    }

    @Transactional
    public void deleteLine(Long id) {
        Line line = findLine(id);

        lineRepository.delete(line);
    }
}
