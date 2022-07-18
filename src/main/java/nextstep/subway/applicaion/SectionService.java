package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SectionService {
    private SectionRepository sectionRepository;
    private StationService stationService;

    public SectionService(SectionRepository sectionRepository, StationService stationService) {
        this.sectionRepository = sectionRepository;
        this.stationService = stationService;
    }

    public boolean checkRegistPreCodition(Long id, SectionRequest sectionRequest) throws Exception {
        //최근 등록된 구간 확인
        Section recentSection = sectionRepository.findTop1ByLineIdOrderByIdDesc(id);

        if (!recentSection.getDownStationId().equals(sectionRequest.getUpStationId())) {
            throw new Exception("DID_NOT_MATCH_STATION");
        }

        //해당 노선에 등록여부 확인
        Section alreadyRegistSection = sectionRepository.findByLineIdByStationId(id, sectionRequest.getDownStationId());
        if (alreadyRegistSection != null) {
            throw new Exception("ALREADY_REGIST");
        }
        return true;
    }

    public boolean checkDeletePreCondition(Long id, Long stationId) throws Exception {
        //최근 등록된 구간 확인
        Section recentSection = sectionRepository.findTop1ByLineIdOrderByIdDesc(id);

        if(!recentSection.getUpStationId().equals(stationId)){
            throw new Exception("STATION_IS_NOT_DOWN_STATION");
        }

        //해당 노선의 구간개수 확인
        List<SectionResponse> sections = findAllByLineId(id);
        if(sections.size() <=1){
            throw new Exception("SIZE_IS_ONE");
        }
        return true;
    }

    @Transactional
    public SectionResponse saveSection(Long id, SectionRequest sectionRequest) {
        Section section = sectionRepository.save(new Section(sectionRequest));
        section.setLineId(id);
        return createSectionResponse(section);
    }

    @Transactional
    public void deleteByStationId(Long stationId) {
        sectionRepository.deleteByUpStationId(stationId);
    }

    @Transactional
    public void deleteAllByLineId(Long lineId) {
        sectionRepository.deleteAllByLineId(lineId);
    }

    public List<SectionResponse> findAllByLineId(Long lineId) {
        List<Section> sections = sectionRepository.findByLineId(lineId);
        return createSectionResponses(sections);
    }

    private SectionResponse createSectionResponse(Section section) {
        SectionResponse response = new SectionResponse(section);
        response.setStation(stationService.findStationById(section.getUpStationId()));
        response.setStation(stationService.findStationById(section.getDownStationId()));
        return response;
    }

    private List<SectionResponse> createSectionResponses(List<Section> sections) {
        List<SectionResponse> response = new ArrayList<>();
        for (Section section : sections) {
            response.add(createSectionResponse(section));
        }
        return response;
    }
}
