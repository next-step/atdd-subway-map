package subway.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.exception.BadRequestSectionsException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Embeddable
public class LineSections {

    @OneToMany(mappedBy = "line" , cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Section> add(Section section){
        isAddableSections(section);
        sections.add(section);
        return sections;
    }

    public void remove(Station station){
        int count = this.getSections().size();
        if (count <= 1) {
            throw new BadRequestSectionsException("노선에는 하나 이상의 구간이 존재해야합니다.");
        }
        if (!this.getSections().get(count- 1).getDownStation().equals(station)) {
            throw new BadRequestSectionsException("해당 노선의 마지막 하행 종점역이 아닙니다.");
        }

        sections.remove(sections.size() - 1);
    }


    private void isAddableSections(Section newSection) {
        if(sections.size()<1) {
            return;
        }
        Station lastStation = sections.get(sections.size() - 1).getDownStation();
        if(!newSection.getUpStation().equals(lastStation)){
            throw new BadRequestSectionsException("구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야합니다.");
        }
        if(sections.stream().anyMatch(section->section.getDownStation().equals(newSection))){
            throw new BadRequestSectionsException("이미 해당 노선에 등록되어있는 역입니다.");
        }
    }

    public List<Station> getStations() {
        List<Station> stations = sections.stream().map(Section::getUpStation).collect(Collectors.toList());
        stations.add(sections.get(sections.size()-1).getDownStation());
        return stations;
    }
}
