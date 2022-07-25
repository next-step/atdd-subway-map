package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.*;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationService stationService, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {

        Sections sections = new Sections();
        Section section = new Section(lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance());

        Line line = new Line.Builder()
                    .name(lineRequest.getName())
                    .color(lineRequest.getColor())
                    .Sections(sections)
                    .build();
        line.registerSection(section);
        lineRepository.save(line);

        return createLineResponse(line);
    }

    private LineResponse createLineResponse(Line line) {

        Sections sections = line.getSections();
        List<StationResponse> stationResponses = createStationResponses(sections.getLineStationIds());

        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                createStationResponses(sections.getLineStationIds())
        );
    }

    private List<StationResponse> createStationResponses(Set<Long> lineStationIds) {
        return stationRepository.findAllById(lineStationIds).stream().map(StationResponse::new).collect(Collectors.toList());
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        Optional<Line> line = lineRepository.findById(id);
        return lineRepository.findById(id).map(this::createLineResponse).orElseThrow(() -> new EntityNotFoundException(id + "번 id로 조회되는 노선이 없습니다."));
    }

    public Line getLine(Long lineId) {
        return lineRepository.findById(lineId).orElseThrow(() -> new EntityNotFoundException(lineId + "번 id로 조회되는 노선이 없습니다."));
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest updateDto) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("manager");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        Line line = getLine(id);
        line.updateLine(updateDto.getName(), updateDto.getColor());
        tx.commit();
    }

    @Transactional
    public void deleteLine(Long id) {
        Line line = getLine(id);

        lineRepository.delete(line);
    }

    @Transactional
    public void registerSection(Long id, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id + "번 id로 조회되는 노선이 없습니다."));
        Section section = new Section(sectionRequest.getUpStationId(), sectionRequest.getDownStationId(), sectionRequest.getDistance());
        line.registerSection(section);
    }

    @Transactional
    public void deleteSection(Long id, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id + "번 id로 조회되는 노선이 없습니다."));
        line.deleteSection(sectionRequest);

    }
}
