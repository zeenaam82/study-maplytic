# Maplytic

> Spring Boot + PostGIS 기반의 버스 정류장 공간 분석 API 프로젝트

---

## 기술 스택

- **백엔드**: Java 17, Spring Boot, JPA, Maven  
- **DB**: PostgreSQL + PostGIS  
- **GIS 처리**: JTS, Hibernate Spatial  
- **배포**: Docker, Docker Compose  
- **API 문서화**: SpringDoc OpenAPI (Swagger UI)  

---

## 주요 기능

### 정류장 데이터 API

- 전체 정류장 목록 조회  
- 키워드 기반 정류장 검색  
- 정류장 유형별 필터링  
- 정류장 유형별 개수 통계

### 공간 분석 API

- 중심점 계산 (`/center-point`)
- 반경 내 정류장 밀도 (`/density`)
- 정류장 간 거리 계산 (`/distance`)
- 반경 내 가장 가까운 정류장 조회 (`/nearest`)
- 반경 내 정류장 수 및 유형별 개수

---

## 실행 방법

### 1. 환경 변수 설정

`.env` 파일 생성:

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://postgis:5432/maplytic
SPRING_DATASOURCE_USERNAME=maplytic_user
SPRING_DATASOURCE_PASSWORD=your_password
SPRING_DATASOURCE_DATABASE=maplytic
