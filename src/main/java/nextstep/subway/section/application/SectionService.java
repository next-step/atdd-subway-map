package nextstep.subway.section.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.section.domain.InvalidStationException;
import nextstep.subway.section.domain.Section;
import nextstep.subway.section.domain.SectionRepository;
import nextstep.subway.section.domain.UnableToDeleteException;
import nextstep.subway.section.dto.SectionRequest;
import nextstep.subway.section.dto.SectionResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SectionService {

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    LineService lineService;

    @Autowired
    StationService stationService;

    public SectionResponse createSection(Long lineId, SectionRequest sectionRequest) {

        Station upStation = stationService.getStationById(sectionRequest.getUpStationId());
        Station downStation = stationService.getStationById(sectionRequest.getDownStationId());

        System.out.println(upStation.getStatus().toString());
        System.out.println(downStation.getStatus().toString());

        System.out.println(upStation.getStatus() == StationStatus.END);
        System.out.println(downStation.getStatus()== StationStatus.ENROLLED);

        if(lineService.getLineById(lineId).getSections().size() != 0 && upStation.getStatus()!= StationStatus.END){

        }

        if(stationService.isStationExist(sectionRequest.getUpStationId())
                && lineService.getLineById(lineId).getSections().size() != 0
                && upStation.getStatus()!= StationStatus.END){
            throw new InvalidStationException("새로 추가하는 상행역이 노선의 맨 마지막 역이 아닙니다");
        }

        if(stationService.isStationExist(sectionRequest.getDownStationId())
                && downStation.getStatus()!= StationStatus.WAIT){
            throw new InvalidStationException("새로 추가하는 하행역이 이미 등록된 역입니다");
        }

        if(lineService.getLineById(lineId).getSections().size()==0) upStation.setStatus(StationStatus.START);
        else upStation.setStatus(StationStatus.ENROLLED);
        downStation.setStatus(StationStatus.END);

        Section savedSection =
                sectionRepository.save(
                        new Section(
                                lineService.getLineById(lineId),
                                upStation,
                                downStation,
                                sectionRequest.getDistance()
                        )
                );

        //savedSection.getUpStation().setStatus(StationStatus.ENROLLED);
        //savedSection.getDownStation().setStatus(StationStatus.VERYEND);

        return SectionResponse.of(savedSection);
    }

    public List<Section> getSectionList(Long lineId) {
        return sectionRepository.findByLine_id(lineId);
    }

    public List<SectionResponse> getSectionResponseList(Long lineId){
        return getSectionList(lineId).stream().map(SectionResponse::of).collect(Collectors.toList());
    }

    public void deleteSection(Long lineId, Long downStationId){

        long count = sectionRepository.countByLine_id(lineId);
        if(count<2){
            throw new UnableToDeleteException("구간이 한개 남아 삭제할 수 없습니다");
        }

        //조건을 만족했을 때
        if(stationService.getStationById(downStationId).getStatus()==StationStatus.END){
            System.out.println("===========================delete");
            sectionRepository.deleteByDownStationId(downStationId);
        }else{
            throw new UnableToDeleteException("노선의 마지막 역만 삭제할 수 있습니다");
        }
    }

    public Optional<Section> getFirstSection(Long lineId){
        Optional<Section> optSection = sectionRepository.findByLine_idAndUpStation_status(lineId, StationStatus.START);
        return optSection;

    }

    public Optional<Section> getNextSection(Long lineId, Station downStation){
        return sectionRepository.findByLine_idAndUpStation(lineId, downStation);
    }
}
