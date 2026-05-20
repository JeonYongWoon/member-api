# member-api

팀원 정보를 저장하고 조회할 수 있는 Spring Boot 기반 API 서버입니다.

## 배포 정보

- EC2 Public IP: 52.78.132.180
- Health Check URL: http://52.78.132.180:8080/actuator/health

## 기술 스택

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Boot Actuator
- H2 Database
- MySQL
- AWS EC2
- Docker

## 주요 기능

- 팀원 정보 저장
- 팀원 정보 조회
- API 요청 로그 출력
- 전역 예외 처리
- Actuator Health Check 제공

## API 명세

### 팀원 저장

```http
POST /api/members
Request Body:

{
  "name": "홍길동",
  "age": 25,
  "mbti": "INTJ"
}
팀원 조회
GET /api/members/{id}
Profile 구성
local: H2 Database 사용
prod: MySQL 사용
Health Check
GET /actuator/health
응답 예시:

{
  "status": "UP"
}
배포 구성
VPC 내 Public Subnet과 Private Subnet을 분리했습니다.
EC2는 Public Subnet에 생성했습니다.
애플리케이션은 EC2에서 JAR 파일로 실행했습니다.
운영 DB는 MySQL을 사용했습니다.
