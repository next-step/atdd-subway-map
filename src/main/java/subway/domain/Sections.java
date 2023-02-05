package subway.domain;

import subway.exception.SubwayRestApiException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static subway.common.ErrorResponse.*;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private List<Section> sections = new ArrayList<>();

    public List<Section> getSections() {
        return sections;
    }

    public void add(Section section) throws Exception{
        this.validationAdd(section);

        this.sections.add(section);
    }

    private void validationAdd(Section section) throws Exception{
        if (! this.isUpStationEqualDownStation(section.getUpStationId())) {
            throw new SubwayRestApiException(ERROR_DOWNSTATION_INVAILD_LINE);
        }

        if (this.alreadyExistsDownStation(section.getDownStationId())) {
            throw new SubwayRestApiException(ERROR_DOWNSTATION_INVAILD_LINE);
        }
    }

    private boolean isUpStationEqualDownStation(Long upStationId) {
        if (this.sections.size() < 1 || this.sections.get(this.sections.size() -1 ).getDownStationId().equals(upStationId)) {
            return true;
        }

        return false;
    }

    private boolean alreadyExistsDownStation(Long downStationId) {
        for (Section section : this.sections) {
            if (downStationId.equals(section.getUpStationId()) || downStationId.equals(section.getDownStationId())) {
                return true;
            }
        }

        return false;
    }

    public void remove(Section section) throws Exception{
        this.validationDeleteSection(section);

        this.sections.remove(section);
    }

    private void validationDeleteSection(Section section) throws Exception{
        if (this.isNotValidSectionCount()) {
            throw new SubwayRestApiException(ERROR_DELETE_SECTION_COUNT_LINE);
        }

        if (!this.isLastSection(section)) {
            throw new SubwayRestApiException(ERROR_DELETE_SECTION_NO_LAST_SECTION_LINE);
        }
    }

    private boolean isLastSection(Section section) {
        return this.getLastSection().equals(section);
    }

    private boolean isNotValidSectionCount() {
        return this.sections.size() <= 1;
    }

    private Section getLastSection() {
        int lastIndex = this.sections.size()-1;
        return this.sections.get(lastIndex);
    }
}
