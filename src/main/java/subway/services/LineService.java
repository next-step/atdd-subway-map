package subway.services;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.request.LineRequest;
import subway.dto.response.LineResponse;
import subway.models.Line;
import subway.models.Section;
import subway.models.Station;
import subway.repositories.LineRepository;
import subway.repositories.SectionRepository;
import subway.repositories.StationRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    @Transactional
    public LineResponse saveLine(LineRequest.Create lineRequest) {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId())
            .orElseThrow(EntityNotFoundException::new);
        Station downStation = stationRepository.findById(lineRequest.getDownStationId())
            .orElseThrow(EntityNotFoundException::new);
        Section section = sectionRepository.save(lineRequest.toSectionEntity(upStation, downStation));
        Line line = lineRepository.save(lineRequest.toEntity(section));

        return LineResponse.of(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
            .map(LineResponse::of)
            .collect(Collectors.toList());
    }

    public LineResponse findById(Long id) {
        return LineResponse.of(lineRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    @Transactional
    public void update(Long id, LineRequest.Update lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        lineRequest.apply(line);
        lineRepository.save(line);
    }

    @Transactional
    public void deleteById(Long id) {
        lineRepository.deleteById(id);
    }
}
