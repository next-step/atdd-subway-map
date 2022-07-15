package nextstep.subway.section.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sectionList = new ArrayList<>();

    private static final int MINIMUM_SIZE = 1;

    public boolean isConnectable(final Section section) {
        if (isEmpty()) {
            return true;
        }

        return isConnectableSection(section);
    }

    private boolean isConnectableSection(final Section section) {
        return getLastDownStationId().equals(section.getUpStationId());
    }

    public Long getFirstUpStationId() {
        if (isEmpty()) {
            throw new IllegalStateException("Section이 없습니다.");
        }
        return getUpStationId(0);
    }

    private Long getUpStationId(final int index) {
        return sectionList.get(index).getUpStationId();
    }

    public Long getLastDownStationId() {
        if (isEmpty()) {
            throw new IllegalStateException("Section이 없습니다.");
        }
        return getDownStationId(getLastIndex());
    }

    private Long getDownStationId(final int index) {
        return sectionList.get(index).getDownStationId();
    }

    private boolean isEmpty() {
        return sectionList.isEmpty();
    }

    public void addSection(final Section section) {
        sectionList.add(section);
    }

    public boolean hasCircularSection(final Section section) {
        return sectionList.stream()
                .anyMatch(section::isDuplicated);
    }

    public void removeLastSection(final Long stationId) {
        if (!canRemoveSection(stationId)) {
            throw new IllegalStateException("해당 역을 삭제할 수 없습니다.");
        }

        sectionList.remove(getLastIndex());
    }

    private boolean canRemoveSection(final Long stationId) {
        if (hasRemovableSize()) {
            return false;
        }

        return getLastDownStationId().equals(stationId);
    }

    private boolean hasRemovableSize() {
        return sectionList.size() <= MINIMUM_SIZE;
    }

    private int getLastIndex() {
        return sectionList.size() - 1;
    }
}
