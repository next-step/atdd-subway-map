package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;

import java.util.ArrayList;
import java.util.List;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;
    private List<SectionResponse> sections;

    public LineResponse(Line line) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.sections = new ArrayList<>();
        this.upStationId = 0L;
        this.downStationId = 0L;
        this.distance = 0L;

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

    public Long getDistance() {
        return distance;
    }

    public List<SectionResponse> getSections() {
        return sections;
    }

    public void addSection(SectionResponse section) {
        this.sections.add(section);
        this.distance += section.getDistance();
        if(this.upStationId.equals(0L)){
            this.upStationId = section.getUpStationId();
        }
        this.downStationId = section.getDownStationid();
    }

    public void setSections(List<SectionResponse> sections) {
        this.sections.addAll(sections);
        for (SectionResponse section : sections) {
            if(this.upStationId.equals(0L)){
                this.upStationId = section.getUpStationId();
            }
            this.distance += section.getDistance();
        }
        this.downStationId = this.sections.get(sections.size()-1).getDownStationid();
    }


}
