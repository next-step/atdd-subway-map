package nextstep.subway.applicaion.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class SectionsResponse {

    private Set<SectionResponse> sections;

}
