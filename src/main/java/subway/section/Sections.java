package subway.section;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Sections {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sectionList = new ArrayList<>();

    public void addSection(Section section) {
        sectionList.add(section);
    }

    public Long getFirstUpStationId() {
        if(sectionList.isEmpty()) {
            throw new IllegalArgumentException("section is Empty");
        }
        return getUpStationId(0);
    }

    public Long getLastDownStationId() {
        if (sectionList.isEmpty()) {
            throw new IllegalArgumentException("section is Empty");
        }
        return getDownStationId(getLastIndex());
    }

    private Long getUpStationId(int index) {
        return sectionList.get(index).getUpStationId();
    }

    private Long getDownStationId(int index) {
        return sectionList.get(index).getDownStationId();
    }

    public boolean isDupleCate(Section section) {
        return sectionList.stream()
            .anyMatch(section::isSame);
    }

    public void removeLastSection(Long stationId) {
        if(sectionList.isEmpty()) {
            throw new IllegalArgumentException("section is Empty");
        }

        if(!isRemovable(stationId)) {
            throw new IllegalStateException("해당 역을 삭제할 수 없습니다.");
        }

        sectionList.remove(getLastIndex());
    }

    private int getLastIndex() {
        return sectionList.size() - 1;
    }

    private boolean isRemovable(Long stationId) {
        return sectionList.size() > 1 && getLastDownStationId().equals(stationId);
    }
}
