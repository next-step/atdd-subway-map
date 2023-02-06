package subway.domain;

import subway.exception.SubwayRestApiException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static subway.exception.ErrorResponseEnum.*;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public List<Section> getSections() {
        return sections;
    }

    public void add(Section section) throws Exception{
        this.validationAdd(section);

        this.sections.add(section);
    }

    private void validationAdd(Section section) throws Exception{
        if (! this.isUpStationEqualDownStation(section.getUpStation())) {
            throw new SubwayRestApiException(ERROR_DOWNSTATION_INVAILD_LINE);
        }

        if (this.alreadyExistsDownStation(section.getDownStation())) {
            throw new SubwayRestApiException(ERROR_DOWNSTATION_INVAILD_LINE);
        }
    }

    private boolean isUpStationEqualDownStation(Station upStation) {
        if (this.sections.size() < 1 || this.sections.get(this.sections.size() -1 ).getDownStation().equals(upStation)) {
            return true;
        }

        return false;
    }

    private boolean alreadyExistsDownStation(Station downStation) {
        for (Section section : this.sections) {
            if (downStation.equals(section.getUpStation()) || downStation.equals(section.getDownStation())) {
                return true;
            }
        }

        return false;
    }

    public void remove(Long sectionId) throws Exception{
        this.validationDeleteSection(sectionId);

        this.sections.remove(this.sections.size()-1);
    }

    private void validationDeleteSection(Long sectionId) throws Exception{
        if (this.isNotValidSectionCount()) {
            throw new SubwayRestApiException(ERROR_DELETE_SECTION_COUNT_LINE);
        }

        if (!this.isLastSection(sectionId)) {
            throw new SubwayRestApiException(ERROR_DELETE_SECTION_NO_LAST_SECTION_LINE);
        }
    }

    private boolean isLastSection(Long sectionId) {
        return this.getLastSection().getId().equals(sectionId);
    }

    private boolean isNotValidSectionCount() {
        return this.sections.size() <= 1;
    }

    private Section getLastSection() {
        int lastIndex = this.sections.size()-1;
        return this.sections.get(lastIndex);
    }
}
