package nextstep.subway.applicaion.mapper.response;

public interface ResponseMapper<Domain, Response> {

    Response map(Domain domain);
}
