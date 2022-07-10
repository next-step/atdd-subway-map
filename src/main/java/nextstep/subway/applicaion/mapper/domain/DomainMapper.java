package nextstep.subway.applicaion.mapper.domain;

public interface DomainMapper<Request, Domain> {

    Domain map(Request request);
}
