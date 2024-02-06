package subway.entity;

import javax.persistence.*;

@Entity
public class StationLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    private Long upStationId;

    private Long downStationId;

    private int distance;

    @Embedded
    private StationSections sections = new StationSections();

    protected StationLine() {
    }

    public StationLine(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public StationLine update(String name, String color) {
        this.name = name;
        this.color = color;
        return this;
    }

    public boolean canSectionSave(StationSection toSaveSection) {
        if(toSaveSection.areStationsSame()) {
            return false;
        }
        if(!toSaveSection.isConnectToLastUpStation(downStationId)) {
            return false;
        }
        return hasNoConnectingDownStation(toSaveSection);
    }

    private boolean hasNoConnectingDownStation(StationSection toSaveSection) {
        return sections.areAllUpStationsDifferentFrom(toSaveSection);
    }

    public boolean canSectionDelete(Long stationId) {
        if(!this.downStationId.equals(stationId)) {
            return false;
        }
        return sections.isDeletionAllowed();
    }

    public void updateDownStation(Long downStationId) {
        this.downStationId = downStationId;
    }

    public StationLine addSection(StationSection createdStationSection) {
        sections.addSection(createdStationSection);
        return this;
    }

    public void deleteSectionByStationId(Long stationIdToDelete) {
        this.sections.deleteSection(stationIdToDelete);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public StationSections getSections() {
        return sections;
    }
}
