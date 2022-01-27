<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-6.14.15-blue">
  <img alt="node" src="https://img.shields.io/badge/node-14.18.2-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
</p>

<br>

# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

<br>

## 🚀 Getting Started

### Install
#### npm 설치
```
cd frontend
npm install
```
> `frontend` 디렉토리에서 수행해야 합니다.

### Usage
#### webpack server 구동
```
npm run dev
```
#### application 구동
```
./gradlew bootRun
```

---

## 2주차 피드백
- [x] 테스트 `검증` 코드 중복 제거
- [x] DTO 정적 팩토리 메서드 적용

## 3단계 - 지하철 구간 관리
- [x] 노선(Line)
  - [x] 상행 종점역, 하행 종점역, 거리
  - [x] 노선 : 구간 = 1:n
- [x] 구간(Section)
  - [x] 상행역, 하행역, 거리
- [x] 구간 등록
  - [x] 상행역은 등록된 하행 종점역이어야 한다.
  - [x] 하행역은 등록된 역과 중복될 수 없다.
- [x] 구간 제거
  - [x] 노선에 등록된 마지막 하행역만 제거할 수 있다.
  - [x] 구간이 1개인 경우 제거할 수 없다.
- [x] 노선의 역 목록 조회
  - [x] 구간 순서대로 정렬하여 상행 종점역부터 하행 종점역 조회