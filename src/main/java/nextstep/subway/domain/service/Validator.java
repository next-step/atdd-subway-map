package nextstep.subway.domain.service;

@FunctionalInterface
public interface Validator<T> {

    void validate(T t);
}
