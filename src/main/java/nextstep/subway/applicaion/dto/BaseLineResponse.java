package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Section;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public abstract class BaseLineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<Section> sections;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    public BaseLineResponse(
            Long id,
            String name,
            String color,
            List<Section> sections,
            LocalDateTime createdDate,
            LocalDateTime modifiedDate) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.sections = sections;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public List<Section> getSections(){
        return Collections.unmodifiableList(sections);
    }
}
