package nextstep.subway.domain;

import lombok.Getter;
import nextstep.subway.exception.BadRequestException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author a1101466 on 2022/07/13
 * @project subway
 * @description
 */
@Embeddable
@Getter
public class Sections {

    @OneToMany(mappedBy = "line", orphanRemoval = true, cascade = CascadeType.ALL)
    @OrderBy("id asc")
    private final List<Section> sectionList = new ArrayList<>();

    protected Sections() {
    }

    public Sections(Line line, int distance, Station upStation, Station downStation) {
        Section section = new Section(distance, upStation, downStation);
        section.setLine(line);
        sectionList.add(section);
    }

    public List<Station> getStation() {
        return sectionList.stream()
                .map(Section::getStations)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public void addSection(Line line, Section section) {
        addSectionValidation(section);
        sectionList.add(section);
        section.setLine(line);
    }

    public void addSectionValidation(Section section){
        Boolean checkDownStation =
                getStation().stream()
                        .anyMatch(
                                currentStation -> currentStation.getId().equals(section.getDownStation().getId())
                        );
        if(checkDownStation){
            throw new BadRequestException("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.");
        }
    }

    public void validationAndSectionDelete(Long stationId){
        Section lastSection = getLastSection();

        if(!Objects.equals(lastSection.getDownStation().getId(), stationId))
            throw new BadRequestException("지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.");

        if(sectionList.size() < 2)
            throw new BadRequestException("지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.");

        removeSection(lastSection);
    }

    public Section getLastSection(){
        if(sectionList.size() < 1){
            throw new NoSuchElementException("저장 된 section정보가 없습니다.");
        }
        return sectionList.get(sectionList.size()-1);

    }
    public void removeSection(Section section){
        sectionList.remove(section);
    }

    public Station getLastStation(){
        return getLastSection().getDownStation();
    }

}