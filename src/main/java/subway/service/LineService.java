package subway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Section;
import subway.dto.*;

import subway.domain.Line;
import subway.domain.Station;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;

import java.util.List;
import java.util.stream.Collectors;



@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;
    private final SectionRepository sectionRepository;

    public List<LineResponse> findALlLines() {
        List<Line> list = lineRepository.findAll();
        return list.stream().map(this::createLineResponse).collect(Collectors.toList());
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationService.findStationById(lineRequest.getUpStationId());
        Station downStation = stationService.findStationById(lineRequest.getDownStationId());
        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor(), upStation, downStation));
        sectionRepository.save(new Section(upStation, downStation, line));
        return createLineResponse(line);
    }

    public LineResponse createLineResponse(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), List.of(stationService.createStationResponse(line.getUpStation()), stationService.createStationResponse(line.getDownStation())));
    }

    public LineResponse findLineById(Long id) {
        Line line = findVerifiedLine(id);
        return createLineResponse(line);
    }

    private Line findVerifiedLine(Long id) {
        return lineRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 Line 입니다"));
    }


    @Transactional
    public void updateLineById(Long id, LinePatchResponse linePatchResponse) {
        Line line = findVerifiedLine(id);
        line.update(linePatchResponse.getName(), linePatchResponse.getColor());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }


    public SectionResponse createSectionResponse(Section section) {
        return new SectionResponse(section.getId(), section.getDistance(), stationService.createStationResponse(section.getUpStation()), stationService.createStationResponse(section.getDownStation()), section.getLine().getId());
    }

    @Transactional
    public SectionResponse addSection(Long lineId, SectionRequest sectionRequest) {
        Line line = findVerifiedLine(lineId);
        Station upStation = stationService.findStationById(sectionRequest.getUpStationId());
        Station downStation = stationService.findStationById(sectionRequest.getDownStationId());
        line.isAddValidation(upStation, downStation);
        line.update(downStation);
        Section section = sectionRepository.save(new Section(sectionRequest.getDistance(), upStation, downStation, line));
        return createSectionResponse(section);
    }

    @Transactional
     public void deleteSectionById(Long lineId, Long stationId) {
        Line line = findVerifiedLine(lineId);
        List<Section> sections = line.getSections();
        line.isDeleteValidation(stationService.findStationById(stationId));
        sectionRepository.deleteById(sections.get(sections.size()-1).getId());
        sections.remove(sections.size()-1);
        line.update(sections.get(sections.size()-1).getDownStation());
    }

}
