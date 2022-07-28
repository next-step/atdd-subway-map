package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.line.LineRequest;
import nextstep.subway.applicaion.dto.line.LineResponse;
import nextstep.subway.applicaion.dto.section.SectionRequest;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.LineRepository;
import nextstep.subway.domain.section.Section;
import nextstep.subway.domain.section.SectionRepository;
import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.station.StationRepository;
import nextstep.subway.error.exception.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {
    private LineRepository lineRepository;
    private SectionRepository sectionRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, SectionRepository sectionRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(toEntity(lineRequest));
        return createLineResponse(line);
    }

    private Line toEntity(LineRequest lineRequest) {
        Station upStation = findStationByStationId(lineRequest.getUpStationId());
        Station downStation = findStationByStationId(lineRequest.getDownStationId());
        Section section = new Section(upStation, downStation, lineRequest.getDistance());
        sectionRepository.save(section);

        return new Line.Builder()
                        .name(lineRequest.getName())
                        .color(lineRequest.getColor())
                        .sections(section)
                        .distance(lineRequest.getDistance())
                        .build();
    }

    private LineResponse createLineResponse(Line entity) {
        List<Section> sections = entity.getSections();
        Station upStation = findStationByStationId(sections.get(0).getId());
        Station downStation = findStationByStationId(sections.get(sections.size() - 1).getId());

        return new LineResponse(entity);
    }

    private Station findStationByStationId(Long id) {
        return stationRepository.findById(id)
                                .orElseThrow(() -> new StationNotFoundException(id));
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        Line line = getLine(id);
        return createLineResponse(line);
    }

    private Line getLine(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new LineNotFoundException(id));
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = getLine(id);
        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public LineResponse addSection(Long id, SectionRequest sectionRequest) {
        Line line = getLine(id);
        List<Section> sections = line.getSections();
        if (sections.get(sections.size() - 1).getDownStation().getId() != sectionRequest.getUpStationId()) {
            throw new SectionIsNotLastSequenceOfLine();
        }
        Long downStationId = sectionRequest.getDownStationId();
        line.getSections().stream()
                .filter(section -> section.getDownStation().getId() == downStationId || section.getUpStation().getId() == downStationId)
                .findFirst()
                .ifPresent(section -> {
                    throw new SectionAlreadyHasStationException(downStationId);
                });

//        System.out.println("+++++++++");
//        System.out.println(line.getSections());
//        System.out.println("+++++++++");

        Station upStation = findStationByStationId(sectionRequest.getUpStationId());
        Station downStation = findStationByStationId(sectionRequest.getDownStationId());
        Section section = new Section(upStation, downStation, sectionRequest.getDistance());
        sectionRepository.save(section);

        line.addSection(section);

//        System.out.println("*********");
//        System.out.println(line.getSections());
//        System.out.println("*********");

        return new LineResponse(getLine(id));
    }

    @Transactional
    public LineResponse deleteSection(Long lineId, Long stationId) {
        Line line = getLine(lineId);
        List<Section> sections = line.getSections();
        if (sections.size() == 1) {
            throw new LengthOfLineIsOneException(lineId);
        }
        if (sections.get(sections.size() - 1).getDownStation().getId() != stationId) {
            throw new SectionIsNotLastSequenceOfLine();
        }
        line.deleteSection();

        return createLineResponse(line);
    }
}
