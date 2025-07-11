# VYBZ Aggregation Service

VYBZ 플랫폼의 데이터 집계 및 통계를 담당하는 마이크로서비스입니다.

## 📋 목차

-   [개요](#개요)
-   [기술 스택](#기술-스택)
-   [주요 기능](#주요-기능)
-   [프로젝트 구조](#프로젝트-구조)
-   [API 문서](#api-문서)
-   [설치 및 실행](#설치-및-실행)
-   [환경 설정](#환경-설정)
-   [배치 처리 시스템](#배치-처리-시스템)
-   [이벤트 처리](#이벤트-처리)

## 🎯 개요

VYBZ Aggregation Service는 다음과 같은 기능을 제공합니다:

-   **데이터 집계**: 댓글, 좋아요, 팔로우, 라이브 시청자 수 등의 통계 데이터 집계
-   **실시간 처리**: Kafka를 통한 실시간 이벤트 처리
-   **배치 처리**: Spring Batch를 통한 대용량 데이터 처리
-   **캐싱**: Redis를 통한 고성능 데이터 캐싱
-   **데이터 저장**: MySQL과 MongoDB를 통한 데이터 저장
-   **API 제공**: 집계된 데이터 조회 API 제공

## 🛠 기술 스택

### Backend

![Spring Cloud](https://img.shields.io/badge/Spring_Cloud-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Batch](https://img.shields.io/badge/Spring_Batch-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-59666C?style=for-the-badge)
![Apache Kafka](https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

### Infra

![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white)
![Amazon EC2](https://img.shields.io/badge/Amazon_EC2-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white)
![Nginx](https://img.shields.io/badge/Nginx-009639?style=for-the-badge&logo=nginx&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

### 협업

![Discord](https://img.shields.io/badge/Discord-5865F2?style=for-the-badge&logo=discord&logoColor=white)
![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)

### Database & Cache

-   **MySQL 8.0**: 집계 데이터 및 메타데이터 저장
-   **MongoDB**: 원본 데이터 저장 (댓글, 좋아요 등)
-   **Redis**: 실시간 캐싱 및 배치 처리 큐

### Message Queue

-   **Apache Kafka**: 비동기 이벤트 처리 및 데이터 동기화

### Documentation

-   **Swagger/OpenAPI 3.0**: API 문서화

### Build & Deploy

-   **Gradle**: 빌드 도구
-   **Docker**: 컨테이너화

## 🚀 주요 기능

### 1. 팔로우 수 집계

-   **버스커 팔로워 수**: 버스커별 팔로워 수 집계 및 조회
-   **사용자 팔로잉 수**: 사용자별 팔로잉 수 집계 및 조회
-   **실시간 업데이트**: 팔로우/언팔로우 시 실시간 카운트 업데이트
-   **배치 처리**: 대용량 데이터 배치 처리

### 2. 댓글 수 집계

-   **피드별 댓글 수**: 각 피드의 댓글 수 집계
-   **실시간 집계**: 새 댓글 작성 시 실시간 카운트 업데이트
-   **표시 정책**: 댓글 수에 따른 표시 형식 변환 (예: 1000 → 1K)

### 3. 좋아요 수 집계

-   **피드 좋아요 수**: 피드별 좋아요 수 집계
-   **댓글 좋아요 수**: 댓글별 좋아요 수 집계
-   **라이브 좋아요 수**: 라이브 스트림별 좋아요 수 집계
-   **실시간 처리**: 좋아요/취소 시 실시간 업데이트

### 4. 라이브 시청자 수 집계

-   **실시간 시청자 수**: 라이브 스트림별 시청자 수 집계
-   **배치 처리**: 대용량 시청자 데이터 배치 처리
-   **성능 최적화**: Redis를 통한 고성능 처리

### 5. 이벤트 처리

-   **Kafka 이벤트 수신**: 외부 서비스로부터 이벤트 수신
-   **이벤트 발행**: 집계 결과를 다른 서비스로 전송
-   **비동기 처리**: 이벤트 기반 비동기 데이터 처리

## 📁 프로젝트 구조

```
src/main/java/com/vybz/aggregation_service/
├── common/                    # 공통 모듈
│   ├── config/               # 설정 클래스들
│   │   ├── MongoConfig.java
│   │   └── SwaggerConfig.java
│   ├── entity/               # 공통 엔티티
│   │   ├── BaseEntity.java
│   │   ├── BaseResponseEntity.java
│   │   ├── BaseResponseStatus.java
│   │   └── SoftDeletableEntity.java
│   └── exception/            # 예외 처리
│       ├── AsyncExceptionHandler.java
│       ├── BaseException.java
│       ├── BaseExceptionHandler.java
│       └── BaseExceptionHandlerFilter.java
├── kafka/                    # Kafka 이벤트 처리
│   ├── config/               # Kafka 설정
│   │   ├── BuskerFollowerCountEventProducerConfig.java
│   │   ├── CommentCountEventProducerConfig.java
│   │   ├── CommentDeltaEventConfig.java
│   │   ├── CommentLikeCountEventProducerConfig.java
│   │   ├── CommentLikeDeltaEventConfig.java
│   │   ├── CommonKafkaConfig.java
│   │   ├── CommonKafkaProducerConfig.java
│   │   ├── FeedLikeCountResultEventProducerConfig.java
│   │   ├── FeedLikeDeltaEventConfig.java
│   │   ├── FollowCountEventConfig.java
│   │   ├── LiveLikeCountResultEventProducerConfig.java
│   │   ├── LiveLikeDeltaEventConfig.java
│   │   ├── LiveViewCountResultEventProducerConfig.java
│   │   ├── UserFollowerCountEventProducerConfig.java
│   │   └── ViewCountKafkaEventConfig.java
│   ├── consumer/             # 이벤트 컨슈머
│   │   ├── CommentDeltaEventConsumer.java
│   │   ├── CommentLikeDeltaEventConsumer.java
│   │   ├── FeedLikeDeltaEventConsumer.java
│   │   ├── FollowCountEventConsumer.java
│   │   ├── LiveLikeDeltaEventConsumer.java
│   │   └── ViewCountKafkaEventConsumer.java
│   ├── event/                # 이벤트 모델
│   │   ├── BuskerFollowerCountEvent.java
│   │   ├── CommentCountEvent.java
│   │   ├── CommentDeltaEvent.java
│   │   ├── CommentLikeCountEvent.java
│   │   ├── CommentLikeDeltaEvent.java
│   │   ├── FeedLikeCountResultEvent.java
│   │   ├── FeedLikeDeltaEvent.java
│   │   ├── FollowCountEvent.java
│   │   ├── LiveLikeCountResultEvent.java
│   │   ├── LiveLikeDeltaEvent.java
│   │   ├── LiveViewCountResultEvent.java
│   │   ├── UserFollowingCountEvent.java
│   │   └── ViewCountKafkaEvent.java
│   └── producer/             # 이벤트 프로듀서
│       ├── BuskerFollowerCountKafkaProducer.java
│       ├── CommentCountKafkaProducer.java
│       ├── CommentLikeCountKafkaProducer.java
│       ├── FeedLikeCountResultEventProducer.java
│       ├── LiveLikeCountResultEventProducer.java
│       ├── LiveViewCountResultEventProducer.java
│       └── UserFollowingCountKafkaProducer.java
├── follow/                   # 팔로우 도메인
│   ├── application/          # 팔로우 서비스 로직
│   │   ├── FollowCountService.java
│   │   └── FollowCountServiceImpl.java
│   ├── batch/                # 배치 처리
│   │   ├── dto/
│   │   │   ├── BuskerFollowerCountDto.java
│   │   │   └── UserFollowingCountDto.java
│   │   ├── job/
│   │   │   └── FollowCountBatchJob.java
│   │   ├── policy/
│   │   │   └── FollowCountDisplayPolicy.java
│   │   ├── processor/
│   │   │   ├── BuskerFollowerCountProcessor.java
│   │   │   └── UserFollowingCountProcessor.java
│   │   ├── reader/
│   │   │   ├── BuskerFollowerCountReader.java
│   │   │   └── UserFollowingCountReader.java
│   │   ├── scheduler/
│   │   │   └── FollowCountBatchScheduler.java
│   │   └── writer/
│   │       ├── BuskerFollowerCountWriter.java
│   │       └── UserFollowingCountWriter.java
│   ├── domain/               # 팔로우 도메인 모델
│   │   ├── BuskerFollowerCount.java
│   │   └── UserFollowingCount.java
│   ├── dto/                  # 팔로우 DTO
│   │   └── response/
│   │       ├── ResponseBuskerFollowerCountDto.java
│   │       └── ResponseUserFollowingCountDto.java
│   ├── infrastructure/       # 팔로우 리포지토리
│   │   ├── BuskerFollowerCountRepository.java
│   │   └── UserFollowingCountRepository.java
│   ├── presentation/         # 팔로우 컨트롤러
│   │   └── FollowCountController.java
│   └── vo/                   # 팔로우 VO
│       └── response/
│           ├── ResponseBuskerFollowerCountVo.java
│           └── ResponseUserFollowingCountVo.java
├── comment/                  # 댓글 도메인
│   ├── batch/                # 배치 처리
│   │   ├── dto/
│   │   │   └── CommentCountDto.java
│   │   ├── job/
│   │   │   └── CommentCountBatchJob.java
│   │   ├── policy/
│   │   │   └── CommentCountDisplayPolicy.java
│   │   ├── processor/
│   │   │   └── CommentCountProcessor.java
│   │   ├── reader/
│   │   │   └── CommentFeedIdReader.java
│   │   ├── scheduler/
│   │   │   └── CommentCountBatchScheduler.java
│   │   └── writer/
│   │       └── CommentCountWriter.java
│   ├── domain/               # 댓글 도메인 모델
│   │   ├── CommentCount.java
│   │   └── FeedType.java
│   └── infrastructure/       # 댓글 리포지토리
│       └── CommentCountRepository.java
├── like/                     # 좋아요 도메인
│   ├── batch/                # 배치 처리
│   │   ├── job/
│   │   │   ├── CommentLikeCountJobConfig.java
│   │   │   ├── FeedLikeCountJobConfig.java
│   │   │   └── LiveLikeCountJobConfig.java
│   │   ├── policy/
│   │   │   ├── CommentLikeCountDisplayPolicy.java
│   │   │   └── FeedLikeCountDisplayPolicy.java
│   │   ├── processor/
│   │   │   ├── CommentLikeCountProcessor.java
│   │   │   ├── FeedLikeCountProcessor.java
│   │   │   └── LiveLikeCountProcessor.java
│   │   ├── reader/
│   │   │   ├── CommentLikeReader.java
│   │   │   ├── FeedLikeReader.java
│   │   │   └── LiveLikeReader.java
│   │   ├── scheduler/
│   │   │   ├── CommentLikeScheduler.java
│   │   │   ├── FeedLikeScheduler.java
│   │   │   └── LiveLikeScheduler.java
│   │   └── writer/
│   │       ├── CommentLikeCountWriter.java
│   │       ├── FeedLikeCountWriter.java
│   │       └── LiveLikeCountWriter.java
│   ├── domain/               # 좋아요 도메인 모델
│   │   ├── CommentLikeCount.java
│   │   ├── FeedLikeCount.java
│   │   ├── FeedType.java
│   │   └── LiveLikeCount.java
│   └── infrastructure/       # 좋아요 리포지토리
│       ├── CommentLikeCountRepository.java
│       ├── FeedLikeCountRepository.java
│       └── LiveLikeRepository.java
└── live/                     # 라이브 도메인
    ├── batch/                # 배치 처리
    │   ├── job/
    │   │   └── LiveViewCountJobConfig.java
    │   ├── processor/
    │   │   └── LiveViewCountProcessor.java
    │   ├── reader/
    │   │   └── LiveViewCountReader.java
    │   ├── scheduler/
    │   │   └── LiveViewCountScheduler.java
    │   └── writer/
    │       └── LiveViewCountWriter.java
    ├── domain/               # 라이브 도메인 모델
    │   └── LiveViewCount.java
    └── infrastructure/       # 라이브 리포지토리
        └── LiveViewCountRepository.java
```

## 📚 API 문서

Swagger UI를 통해 API 문서를 확인할 수 있습니다:

-   **URL**: `http://localhost:8000/aggregation-service/swagger-ui/index.html`
-   **API 그룹**: VYBZ AGGREGATION SERVICE

### 주요 API 엔드포인트

#### 팔로우 수 조회 API

-   `GET /api/v1/follow-count/user/{userUuid}` - 사용자 팔로잉 수 조회
-   `GET /api/v1/follow-count/busker/{buskerUuid}` - 버스커 팔로워 수 조회

### API 요청/응답 예시

#### 사용자 팔로잉 수 조회

**요청**

```
GET /api/v1/follow-count/user/user123
```

**응답**

```json
{
    "httpStatus": 200,
    "isSuccess": true,
    "message": "SUCCESS",
    "code": 200,
    "result": {
        "userUuid": "user123",
        "followingCount": 150,
        "displayFollowingCount": "150"
    }
}
```

#### 버스커 팔로워 수 조회

**요청**

```
GET /api/v1/follow-count/busker/busker456
```

**응답**

```json
{
    "httpStatus": 200,
    "isSuccess": true,
    "message": "SUCCESS",
    "code": 200,
    "result": {
        "buskerUuid": "busker456",
        "followerCount": 1250,
        "displayFollowerCount": "1.2K"
    }
}
```

## 🚀 설치 및 실행

### 1. 사전 요구사항

-   Java 17
-   Gradle 8.4+
-   Docker (선택사항)
-   MySQL 8.0
-   MongoDB
-   Redis
-   Kafka

### 2. 로컬 실행

```bash
# 프로젝트 클론
git clone <repository-url>
cd vybz-aggregation

# Gradle 빌드
./gradlew clean build

# 애플리케이션 실행
./gradlew bootRun
```

### 3. Docker 실행

```bash
# Docker 이미지 빌드
docker build -t vybz-aggregation .

# Docker 컨테이너 실행
docker run -p 0:0 vybz-aggregation
```

## ⚙️ 환경 설정

### 주요 설정 파일

-   `application.yml`: 기본 설정
-   `application-dev.yml`: 개발 환경 설정

### 환경 변수

```yaml
# 데이터베이스 설정
spring:
  datasource:
    url: jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}

# MongoDB 설정
spring:
  data:
    mongodb:
      uri: mongodb://${MONGO_USERNAME}:${MONGO_PASSWORD}@${MONGO_HOST}:${MONGO_PORT}/${MONGO_DATABASE}

# Redis 설정
spring:
  data:
    redis:
      host: ${REDIS_HOST}
      port: ${REDIS_PORT}
      password: ${REDIS_PASSWORD}

# Kafka 설정
spring:
  kafka:
    bootstrap-servers: ${KAFKA_SERVERS}
```

## 🔄 배치 처리 시스템

### 배치 작업 구성

#### 1. 팔로우 수 배치

-   **스케줄러**: `FollowCountBatchScheduler`
-   **실행 주기**: 10초마다 실행
-   **처리 내용**: 버스커 팔로워 수, 사용자 팔로잉 수 집계

#### 2. 댓글 수 배치

-   **스케줄러**: `CommentCountBatchScheduler`
-   **실행 주기**: 매분 실행
-   **처리 내용**: 피드별 댓글 수 집계

#### 3. 좋아요 수 배치

-   **피드 좋아요**: `FeedLikeScheduler` (매분 실행)
-   **댓글 좋아요**: `CommentLikeScheduler` (매분 실행)
-   **라이브 좋아요**: `LiveLikeScheduler` (매분 실행)

#### 4. 라이브 시청자 수 배치

-   **스케줄러**: `LiveViewCountScheduler`
-   **실행 주기**: 매분 실행
-   **처리 내용**: 라이브 스트림별 시청자 수 집계

### 배치 처리 흐름

1. **Reader**: Redis에서 처리할 데이터 읽기
2. **Processor**: MongoDB에서 실제 데이터 집계
3. **Writer**: 결과를 Kafka로 전송

### Redis 키 구조

-   `busker:follower:queue`: 버스커 팔로워 수 업데이트 대상
-   `user:following:queue`: 사용자 팔로잉 수 업데이트 대상
-   `live:view:batch:set`: 라이브 시청자 수 배치 대상
-   `live:like:batch:set`: 라이브 좋아요 수 배치 대상

## 📡 이벤트 처리

### Kafka 이벤트

#### 수신 이벤트

-   **FollowCountEvent**: 팔로우/언팔로우 이벤트
-   **CommentDeltaEvent**: 댓글 작성/삭제 이벤트
-   **FeedLikeDeltaEvent**: 피드 좋아요/취소 이벤트
-   **CommentLikeDeltaEvent**: 댓글 좋아요/취소 이벤트
-   **LiveLikeDeltaEvent**: 라이브 좋아요/취소 이벤트
-   **ViewCountKafkaEvent**: 라이브 시청자 수 이벤트

#### 발행 이벤트

-   **BuskerFollowerCountEvent**: 버스커 팔로워 수 결과
-   **UserFollowingCountEvent**: 사용자 팔로잉 수 결과
-   **CommentCountEvent**: 댓글 수 결과
-   **FeedLikeCountResultEvent**: 피드 좋아요 수 결과
-   **CommentLikeCountEvent**: 댓글 좋아요 수 결과
-   **LiveLikeCountResultEvent**: 라이브 좋아요 수 결과
-   **LiveViewCountResultEvent**: 라이브 시청자 수 결과

### Kafka 토픽

-   `create-follow`: 팔로우 생성 이벤트
-   `comment-delta-count`: 댓글 증감 이벤트
-   `feed-delta-count`: 피드 좋아요 증감 이벤트
-   `comment-like-delta-count`: 댓글 좋아요 증감 이벤트
-   `live-like-delta-count`: 라이브 좋아요 증감 이벤트
-   `live-view-count`: 라이브 시청자 수 이벤트

### 이벤트 처리 흐름

1. **이벤트 수신**: Kafka Consumer가 이벤트 수신
2. **실시간 처리**: 즉시 Redis에 업데이트 대상 추가
3. **배치 처리**: 스케줄러가 배치 작업 실행
4. **결과 발행**: 집계 결과를 Kafka로 전송

## 🏗 아키텍처

### 도메인 주도 설계 (DDD)

-   **Domain Layer**: 도메인 모델과 비즈니스 로직
-   **Application Layer**: 서비스 로직과 유스케이스
-   **Infrastructure Layer**: 데이터베이스 접근과 외부 시스템 연동
-   **Presentation Layer**: REST API 엔드포인트

### 마이크로서비스 패턴

-   **Service Discovery**: Eureka Client를 통한 서비스 등록
-   **Event-Driven**: Kafka를 통한 비동기 이벤트 처리
-   **Stateless**: 상태 없는 서비스 설계

### 데이터베이스 설계

-   **MySQL**: 집계 결과 데이터 저장
-   **MongoDB**: 원본 데이터 저장 (댓글, 좋아요 등)
-   **Redis**: 실시간 캐싱 및 배치 처리 큐

### 배치 아키텍처

-   **Spring Batch**: 대용량 데이터 처리
-   **Chunk Processing**: 청크 단위 처리로 메모리 효율성 확보
-   **Scheduling**: Cron 표현식을 통한 정기 실행
-   **Error Handling**: 배치 실패 시 예외 처리

## 🔧 개발 가이드

### 코드 컨벤션

-   **패키지 구조**: 도메인별 계층 분리
-   **네이밍**: 명확하고 일관된 네이밍 규칙
-   **예외 처리**: BaseException을 통한 통일된 예외 처리
-   **로깅**: Slf4j를 통한 구조화된 로깅

### 테스트

```bash
# 단위 테스트 실행
./gradlew test

# 통합 테스트 실행
./gradlew integrationTest

# 배치 테스트
./gradlew test --tests "*BatchTest*"
```

### 배치 작업 테스트

```java
// 배치 작업 수동 실행 테스트
@Autowired
private JobLauncher jobLauncher;

@Autowired
private JobRegistry jobRegistry;

public void testBatchJob() throws Exception {
    Job job = jobRegistry.getJob("followAggregationJob");
    JobParameters params = new JobParametersBuilder()
        .addLong("time", System.currentTimeMillis())
        .toJobParameters();

    jobLauncher.run(job, params);
}
```

### 성능 최적화

#### 데이터베이스 최적화

-   **인덱싱**: 자주 조회되는 필드에 인덱스 설정
-   **배치 처리**: 대량 데이터 처리 시 배치 사용
-   **캐싱**: Redis를 통한 자주 조회되는 데이터 캐싱

#### 배치 처리 최적화

-   **청크 크기**: 적절한 청크 크기 설정 (100-200)
-   **병렬 처리**: 멀티스레드 배치 처리
-   **메모리 관리**: 대용량 데이터 처리 시 메모리 최적화

#### Kafka 최적화

-   **배치 전송**: 메시지 배치 처리
-   **파티션 관리**: 토픽 파티션 최적화
-   **컨슈머 그룹**: 컨슈머 그룹 설정

## 📊 모니터링

### 로깅

-   **애플리케이션 로그**: Spring Boot 로깅
-   **배치 로그**: 배치 작업 실행 로그
-   **Kafka 로그**: 이벤트 처리 로그

### 메트릭

-   **배치 실행 수**: 배치 작업 실행 횟수
-   **처리 데이터량**: 배치당 처리 데이터 수
-   **응답 시간**: API 응답 시간
-   **에러율**: 에러 발생률

### 알림

-   **배치 실패**: 배치 작업 실패 알림
-   **데이터베이스 오류**: DB 연결 오류 알림
-   **Kafka 오류**: 메시지 전송 실패 알림

## 🚨 트러블슈팅

### 일반적인 문제

#### 배치 작업 실패

```bash
# 배치 작업 상태 확인
curl -X GET "http://localhost:8000/aggregation-service/actuator/batch"

# 배치 작업 재시작
curl -X POST "http://localhost:8000/aggregation-service/actuator/batch/restart"
```

#### 데이터베이스 연결 오류

```bash
# MySQL 연결 확인
mysql -h <탄력적 IP> -P 33306 -u vybz -p

# MongoDB 연결 확인
mongo mongodb://<탄력적 IP>:27020/vybz
```

#### Kafka 연결 오류

```bash
# Kafka 브로커 상태 확인
kafka-topics.sh --bootstrap-server <탄력적 IP>:10000 --list

# 토픽 메시지 확인
kafka-console-consumer.sh --bootstrap-server <탄력적 IP>:10000 --topic create-follow --from-beginning
```

### 로그 확인

```bash
# 애플리케이션 로그 확인
tail -f logs/application.log

# 배치 로그 확인
grep "BATCH" logs/application.log

# 에러 로그 확인
grep "ERROR" logs/application.log
```

### Redis 디버깅

```bash
# Redis 연결 확인
redis-cli -h <탄력적 IP> -p 63379 -a vybz1234

# 키 확인
KEYS "*batch*"
KEYS "*follower*"
KEYS "*following*"
```

## 📝 라이선스

이 프로젝트는 VYBZ 팀의 내부 프로젝트입니다.

## 👥 팀

-   **개발팀**: VYBZ Backend Team

---

**VYBZ Aggregation Service** - 데이터 집계 및 통계 서비스
