package nextstep.subway.line.application;

import nextstep.subway.common.exception.InvalidRequestException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.SectionRequest;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest request) {
        if(isExistLineName(request.getName())) {
            throw new InvalidRequestException("이미 존재하는 노선명 입니다.");
        }

        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());

        Line persistLine = lineRepository.save(new Line(request.getName(), request.getColor(), upStation, downStation, request.getDistance()));
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findLineAll() {
        List<Line> lineList = lineRepository.findAll();
        return lineList.stream()
                .map(line -> LineResponse.of(line))
                .collect(Collectors.toList());
    }

    public boolean isExistLineName(String name) {
        return lineRepository.existsLineByName(name);
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(() -> new InvalidRequestException("라인이 존재하지 않습니다."));
    }

    public void updateLine(Long id, LineRequest lineRequest) {
        Line addedLine = lineRepository.findById(id).orElseThrow(() -> new InvalidRequestException("업데이트 할 라인이 등록되어 있지 않습니다."));
        addedLine.update(lineRequest.toLine());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    public void addSection(Long id, SectionRequest sectionRequest) {
        final Line line = findLineById(id);
        final Station inputUpStation = stationService.findStationById(sectionRequest.getUpStationId());
        final Station inputDownStation = stationService.findStationById(sectionRequest.getDownStationId());
        final Section newSection = new Section(line, inputUpStation, inputDownStation, sectionRequest.getDistance());

        List<Section> sections = line.getSections();

        if(sections.size() == 0) {
            sections.add(newSection);
            return;
        }

        List<Station> sortedStations = getSortedStations(sections);
        Station terminalStationByBaseSections = sortedStations.get(sortedStations.size() - 1);

        if(!terminalStationByBaseSections.equals(inputUpStation)) {
            throw new InvalidRequestException("새로운 구간의 상행역은 기존 구간의 하행종점역 이어야 합니다.");
        }

        if(sortedStations.contains(inputDownStation)) {
            throw new InvalidRequestException("새로운 구간의 하행역은 기존 구간에 포함된 역이 아니어야 합니다.");
        }

        sections.add(newSection);
        return;
    }

    private List<Station> getSortedStations(List<Section> sections) {
        Station upTerminalStation = getUpTerminalStation(sections);
        Section sectionByUpStation = getSectionByUpStation(sections, upTerminalStation);

        List<Station> sortedStations = new ArrayList<>();
        while(sectionByUpStation != null) {
            if(sortedStations.isEmpty()) {
                sortedStations.add(sectionByUpStation.getUpStation());
            }

            Station downStation = sectionByUpStation.getDownStation();
            sortedStations.add(downStation);
            sectionByUpStation = getSectionByUpStation(sections, downStation);
        }
        return sortedStations;
    }

    private Section getSectionByUpStation(List<Section> sections, Station upTerminalStation) {
        for(Section section : sections) {
            if(section.getUpStation().equals(upTerminalStation)) {
                return section;
            }
        }
        return null;
    }

    private Section getSectionByDownStation(List<Section> sections, Station downTerminalStation) {
        for(Section section : sections) {
            if(section.getDownStation().equals(downTerminalStation)) {
                return section;
            }
        }
        return null;
    }

    private Station getUpTerminalStation(List<Section> sections) {
        List<Station> upStations = new ArrayList<>();
        List<Station> downStations = new ArrayList<>();
        
        for(Section section : sections) {
            upStations.add(section.getUpStation());
            downStations.add(section.getDownStation());
        }
        
        upStations.removeAll(downStations);
        
        if(upStations.size() > 0) {
            return upStations.get(0);
        }
        return null;
    }

    public void deleteSection(Long id, Long stationId) {
        final Line line = findLineById(id);
        List<Section> sections = line.getSections();

        if(sections.size() < 2) {
            throw new InvalidRequestException("구간이 2개 이상일때만 삭제할 수 있습니다.");
        }

        List<Station> sortedStations = getSortedStations(sections);
        final Station downTerminalStation = sortedStations.get(sortedStations.size() - 1);
        final Station inputStation = stationService.findStationById(stationId);

        if(!downTerminalStation.equals(inputStation)) {
            throw new InvalidRequestException("이 구간의 마지막 역만 삭제할 수 있습니다.");
        }

        Section targetSection = getSectionByDownStation(sections, downTerminalStation);
        sections.remove(targetSection);
    }
}
