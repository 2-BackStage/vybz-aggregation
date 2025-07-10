# 🎵 VYBZ Aggregation Service

VYBZ 플랫폼의 **댓글/팔로우/좋아요/라이브 집계**를 담당하는 마이크로서비스입니다.  
Kafka 기반의 실시간 이벤트 처리와 Spring Batch를 활용한 대용량 배치 집계를 통해  
플랫폼 내 다양한 소셜 활동의 통계를 신속하고 정확하게 제공합니다.

---

## 🛠 Tech Stack

| 구분              | 기술/버전                                      |
|-------------------|-----------------------------------------------|
| **Language**      | Java 17                                       |
| **Framework**     | Spring Boot 3.4.5, Spring Batch               |
| **Database**      | MongoDB, MySQL (JPA)                          |
| **Message**       | Apache Kafka                                  |
| **Cache**         | Redis                                         |
| **Service Discovery** | Netflix Eureka Client                     |
| **Docs**          | Swagger/OpenAPI 3.0                           |
| **Build**         | Gradle 8.4, Docker                            |

### Architecture Pattern

- Layered Architecture
- Domain-Driven Design (DDD)
- Event-Driven Architecture

---

## 📋 서비스 목록

| 서비스명         | 설명                                 | 언어     | 상태      |
|------------------|--------------------------------------|----------|-----------|
| Aggregation      | 댓글/팔로우/좋아요/라이브 집계       | Java 17  | ✅ Active |
| Comment          | 피드 댓글 집계 및 통계               | Java 17  | ✅ Active |
| Follow           | 팔로우/팔로잉 집계 및 통계           | Java 17  | ✅ Active |
| Like             | 피드/댓글/라이브 좋아요 집계 및 통계 | Java 17  | ✅ Active |
| Live             | 라이브 방송 조회수/좋아요 집계       | Java 17  | ✅ Active |
| Kafka            | 이벤트 기반 실시간 메시징            | Java 17  | ✅ Active |
| Batch            | 대용량 데이터 배치 집계              | Java 17  | ✅ Active |
| Common           | 공통 엔티티/설정/예외처리            | Java 17  | ✅ Active |

---

## 📌 Architecture Diagram

> (아키텍처 다이어그램 이미지는 추후 삽입 예정)

---

## 🚀 Quick Start

### Prerequisites

- Java 17+
- Gradle 8.4+
- MongoDB
- Apache Kafka
- Redis

### Local Development

```bash
# 1. 프로젝트 클론
git clone https://github.com/2-BackStage/vybz-aggregation.git
cd vybz-aggregation

# 2. 빌드 및 실행 (로컬)
./gradlew clean build -x test
docker-compose up -d

# 3. API 문서 (Swagger)
# http://localhost:8080/aggregation-service/swagger-ui/index.html
```

---

## 📁 프로젝트 구조 (Layered Architecture & Batch)

```plaintext
vybz-aggregation/
├── build.gradle
├── Dockerfile
├── gradle/
│   └── wrapper/
├── gradlew
├── gradlew.bat
├── settings.gradle
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── vybz/
│       │           └── aggregation_service/
│       │               ├── comment/
│       │               │   └── batch/
│       │               │       ├── job/         # 🏗️ 배치 잡(Job) 정의
│       │               │       ├── processor/   # ⚙️ 배치 처리 로직(Processor)
│       │               │       ├── reader/      # 📥 데이터 읽기(Reader)
│       │               │       ├── writer/      # 📤 데이터 쓰기(Writer)
│       │               │       └── policy/      # 📝 집계/표시 정책
│       │               ├── follow/
│       │               │   └── batch/
│       │               │       ├── job/
│       │               │       ├── processor/
│       │               │       ├── reader/
│       │               │       ├── writer/
│       │               │       └── policy/
│       │               ├── like/
│       │               │   └── batch/
│       │               │       ├── job/
│       │               │       ├── processor/
│       │               │       ├── reader/
│       │               │       ├── writer/
│       │               │       └── policy/
│       │               ├── live/
│       │               │   └── batch/
│       │               │       ├── job/
│       │               │       ├── processor/
│       │               │       ├── reader/
│       │               │       ├── writer/
│       │               ├── kafka/        # 🔄 Kafka 이벤트/컨슈머/프로듀서/설정
│       │               ├── common/       # 🛠 공통 엔티티/설정/예외
│       │               └── exception/    # 🚨 예외 처리
│       └── resources/   # ⚙️ 설정 파일 (application.yml 등)
└── ...
```

---

## 🗂️ 배치(Batch) 구조

```plaintext
aggregation_service/
└── comment/
    └── batch/
        ├── job/         # 🏗️ 배치 잡(Job) 정의
        ├── processor/   # ⚙️ 배치 처리 로직(Processor)
        ├── reader/      # 📥 데이터 읽기(Reader)
        ├── writer/      # 📤 데이터 쓰기(Writer)
        └── policy/      # 📝 집계/표시 정책
└── follow/
    └── batch/
        ├── job/
        ├── processor/
        ├── reader/
        ├── writer/
        └── policy/
└── like/
    └── batch/
        ├── job/
        ├── processor/
        ├── reader/
        ├── writer/
        └── policy/
└── live/
    └── batch/
        ├── job/
        ├── processor/
        ├── reader/
        ├── writer/
```

---

## 🏗️ 배치 레이어 설명

- **job/** (🏗️): 배치 잡(Job) 및 Step 정의  
- **processor/** (⚙️): 배치 처리 로직(데이터 가공/집계)
- **reader/** (📥): 데이터 소스에서 읽기 (DB, Kafka 등)
- **writer/** (📤): 집계 결과 저장/출력
- **policy/** (📝): 집계/표시 정책(비즈니스 룰)

---

## 🏗️ 레이어별 설명

- **comment/** (💬): 댓글 집계 및 통계 도메인
- **follow/** (👥): 팔로우/팔로잉 집계 및 통계 도메인
- **like/** (👍): 피드/댓글/라이브 좋아요 집계 및 통계 도메인
- **live/** (📺): 라이브 방송 조회수/좋아요 집계 도메인
- **kafka/** (🔄): Kafka 이벤트, 컨슈머, 프로듀서, 설정
- **common/** (🛠): 공통 유틸리티, 예외, 설정, 엔티티
- **exception/** (🚨): 예외 처리

---

## 🔧 주요 기능

- **댓글/팔로우/좋아요/라이브** 실시간 집계 및 통계
- Kafka 기반 이벤트 발행 및 처리
- Spring Batch 기반 대용량 배치 집계
- Cursor 기반 페이지네이션
- Swagger 기반 API 문서 제공
- 실시간 데이터 동기화
- 서비스 디스커버리(Eureka) 지원

---

## 🎯 API 엔드포인트 예시

- `/api/comments` : 댓글 집계/조회
- `/api/follows` : 팔로우/팔로잉 집계/조회
- `/api/likes` : 좋아요 집계/조회
- `/api/live` : 라이브 방송 집계/조회

---

## 🔄 Kafka 이벤트 예시

- comment-delta
- follow-count
- feed-like-delta
- live-like-delta
- live-view-count

---

## 📞 Contact 
- Made with ❤️ by VYBZ Team 