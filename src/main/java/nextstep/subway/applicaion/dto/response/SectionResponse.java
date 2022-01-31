package nextstep.subway.applicaion.dto.response;

import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;

import java.time.LocalDateTime;
import java.util.Objects;

public class SectionResponse {

    private Long id;
    private Long lineId;
    private Long upStationId;
    private String upStationName;
    private Long downStationId;
    private String downStationName;
    private int distance;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    private SectionResponse(Long id, Long lineId, Station upStation, Station downStation, int distance,
                           LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.lineId = lineId;

        this.upStationId = upStation.getId();
        this.upStationName = upStation.getName();
        this.downStationId = downStation.getId();
        this.downStationName = downStation.getName();
        this.distance = distance;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static SectionResponse createSectionResponse(Section section, Long lineId) {
        return new SectionResponse(
                section.getId(),
                lineId,
                section.getUpStation(),
                section.getDownStation(),
                section.getDistance(),
                section.getCreatedDate(),
                section.getModifiedDate()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public String getUpStationName() {
        return upStationName;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public String getDownStationName() {
        return downStationName;
    }

    public int getDistance() {
        return distance;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SectionResponse response = (SectionResponse) o;
        return getDistance() == response.getDistance() && Objects.equals(getId(), response.getId()) &&
               Objects.equals(getLineId(), response.getLineId()) &&
               Objects.equals(getUpStationId(), response.getUpStationId()) &&
               Objects.equals(getUpStationName(), response.getUpStationName()) &&
               Objects.equals(getDownStationId(), response.getDownStationId()) &&
               Objects.equals(getDownStationName(), response.getDownStationName()) &&
               Objects.equals(getCreatedDate(), response.getCreatedDate()) &&
               Objects.equals(getModifiedDate(), response.getModifiedDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLineId(), getUpStationId(), getUpStationName(),
                        getDownStationId(), getDownStationName(), getDistance(), getCreatedDate(), getModifiedDate());
    }
}
