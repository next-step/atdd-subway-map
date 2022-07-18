package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.applicaion.dto.StationLineResponse;
import nextstep.subway.domain.*;
import nextstep.subway.exception.SubwayException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StationLineService {

    private final StationLineRepository lineRepository;
    private final StationSectionRepository sectionRepository;

    public StationLineResponse save(Line line) {
        return StationLineResponse.form(lineRepository.save(line));
    }

    public List<StationLineResponse> findAllStationLines() {
        return lineRepository.findAll()
                             .stream()
                             .map(StationLineResponse::form)
                             .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StationLineResponse findById(Long id) {
       return StationLineResponse.form(findLineById(id));
    }

    public void update(Line line) {
        StationLineResponse.form(lineRepository.save(line));
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public SectionResponse addSection(Long lineId, SectionRequest.PostRequest request) {
        Line line = findLineById(lineId);
        Section section = sectionRepository.save(request.toEntity());
        line.addSection(section);
        return SectionResponse.form(line.findLastSection());
    }

    public void deleteSection(Long lineId, Long stationsId) {
        Line line = findLineById(lineId);
        Sections sections = line.getSections();
        sections.deleteSection(stationsId);
    }


    private Line findLineById(Long lineId) {
        return lineRepository.findById(lineId).orElseThrow(() -> new SubwayException("등록되지 않은 노선 입니다."));
    }

}
