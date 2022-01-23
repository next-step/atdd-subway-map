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

## 🚀 1단계 - 지하철 노선 관리 기능 구현
- 지하철 노선 생성
- 지하철 노선 목록 조회
- 지하철 노선 조회
- 지하철 노선 수정
- 지하철 노선 삭제

## 🚀 2단계 - 인수 테스트 리팩터링
- 지하철역과 지하철 노선 이름 중복 금지 기능 추가
- 새로운 기능 추가하면서 인수 테스트 리팩터링

## 🚀 3 단계 - 지하철 구간 관리
- 지하철 노선 생성 시 필요한 인자 추가하기
- 지하철 노선에 구간을 등록하는 기능 구현
- 지하철 노선에 구간을 제거하는 기능 구현
- 지하철 노선에 등록된 구간을 통해 역 목록을 조회하는 기능 구현
- 구간 등록 / 제거 시 예외 케이스에 대한 인수 테스트 작성

