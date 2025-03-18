### 개발 가이드 문서

# 차량 관리 애플리케이션 - 개발 가이드

이 문서는 Spring Boot 기반의 **차량 관리 애플리케이션(Vehicle Management Application)** 개발 가이드라인과 아키텍처 원칙을 설명합니다. 패키지 구조, 개발 규칙, 모범 사례를 포함하여 **체계적이고 유지보수 가능하며 확장성 있는 코드베이스**를 구축하는 방법을 다룹니다.

## 1. 패키지 구조 및 역할

이 애플리케이션은 **레이어드 아키텍처**를 따르며, 각 패키지는 특정한 역할을 담당합니다.

- **`interface`**
    - **역할:** 외부 클라이언트(웹 브라우저, 모바일 앱, 기타 서비스)와의 통신을 담당하는 계층입니다.  
      모든 **입력 요청의 진입점**이며, **출력 응답의 출구점**입니다.
    - **구성 요소:**
        - `Controller`: HTTP 요청을 받아 유효성을 검사한 후, `Facade Service`에 위임합니다.
        - `DTO`(데이터 전송 객체): 클라이언트와 데이터를 주고받는 구조를 정의합니다.
    - **주요 규칙:**
        - `Controller`는 반드시 `application` 계층의 `Facade Service`만 호출해야 합니다.
        - `Controller`는 `DTO`와 `domain` 객체(엔터티) 간 변환을 담당합니다.
        - `Controller`에서는 비즈니스 로직을 포함할 수 없습니다.
    - **예제:**
        - `com.sonicplayground.geminiboard.interfaces.user.UserController`
        - `com.sonicplayground.geminiboard.interfaces.user.UserDto`

- **`application`**
    - **역할:** `Facade Service`를 포함하여 **비즈니스 로직을 조율**하는 계층입니다.  
      `interface` 계층과 `domain` 계층 사이에서 **중재자 역할**을 합니다.
    - **구성 요소:**
        - `Facade Service`: 여러 `domain` 서비스를 호출하여 비즈니스 로직을 실행하는 계층입니다.
    - **주요 규칙:**
        - `Facade Service`는 반드시 `domain` 계층의 서비스만 호출해야 합니다.
        - `Facade Service`는 `@Transactional`을 활용하여 트랜잭션을 관리해야 합니다.
        - 복잡한 비즈니스 로직은 `domain` 계층에서 구현해야 합니다.
    - **예제:**
        - `com.sonicplayground.geminiboard.application.user.UserApplicationService`

- **`domain`**
    - **역할:** 핵심 비즈니스 로직을 담당하는 **도메인 계층**입니다.
    - **구성 요소:**
        - `Service`: 특정 도메인 개념에 대한 비즈니스 로직을 구현합니다. (예: `UserService`, `VehicleService`)
        - `Entity`: 핵심 비즈니스 객체를 표현하며, 속성과 관계를 포함합니다. (예: `User`, `Vehicle`)
        - `Store`: 데이터 저장을 위한 인터페이스를 정의합니다. (예: `UserStore`, `VehicleStore`)
        - `Reader`: 읽기 전용 데이터 접근을 위한 인터페이스를 정의합니다. (예: `UserReader`, `VehicleReader`)
        - `Command`: 특정 비즈니스 로직 수행을 위한 명령 객체입니다.
        - `Enum`: 열거형 타입을 정의합니다.
    - **주요 규칙:**
        - `domain` 서비스는 **다른 도메인 서비스를 직접 호출할 수 없습니다**.
        - `domain` 서비스는 `Store` 또는 `Reader`를 통해서만 데이터를 조작해야 합니다.
        - `Entity`는 데이터를 표현하는 속성과 기본적인 메서드만 포함해야 합니다.
        - `Store` 및 `Reader`는 **인터페이스만** 정의해야 합니다.
    - **예제:**
        - `com.sonicplayground.geminiboard.domain.user.User`
        - `com.sonicplayground.geminiboard.domain.user.UserService`
        - `com.sonicplayground.geminiboard.domain.user.UserStore`
        - `com.sonicplayground.geminiboard.domain.user.UserReader`
        - `com.sonicplayground.geminiboard.domain.user.UserCommand`
        - `com.sonicplayground.geminiboard.domain.user.Gender`
        - `com.sonicplayground.geminiboard.domain.user.UserType`

- **`infrastructure`**
    - **역할:** `domain` 계층에서 정의한 `Store` 및 `Reader` 인터페이스를 구현하는 계층입니다.  
      또한, 이메일 전송 등 **외부 시스템과의 통신**을 담당합니다.
    - **구성 요소:**
        - `StoreImpl`: `Store` 인터페이스를 JPA 등을 활용해 구현합니다.
        - `ReaderImpl`: `Reader` 인터페이스를 구현하여 읽기 전용 데이터를 제공합니다.
        - `JpaRepository`: 데이터베이스와 상호작용하기 위한 JPA Repository입니다.
        - `ExternalCommunication`: 이메일 전송 등 외부 시스템과의 통신을 처리합니다.
    - **주요 규칙:**
        - `infrastructure` 계층은 반드시 `domain` 계층의 인터페이스만 구현해야 합니다.
        - `infrastructure` 계층에서 `domain` 서비스를 직접 호출할 수 없습니다.
        - `JpaRepository`는 `StoreImpl` 또는 `ReaderImpl`에서만 사용해야 합니다.
    - **예제:**
        - `com.sonicplayground.geminiboard.infrastructure.user.UserStoreImpl`
        - `com.sonicplayground.geminiboard.infrastructure.user.UserReaderImpl`
        - `com.sonicplayground.geminiboard.infrastructure.user.UserJpaRepository`

- **`common`**
    - **역할:** 애플리케이션 전반에서 **공통적으로 사용되는 유틸리티, 상수, 예외 처리 코드**를 포함합니다.
    - **구성 요소:**
        - `Utils`: 공통적으로 사용되는 유틸리티 클래스
        - `Constants`: 전역 상수 정의
        - `Converter`: 데이터 타입 변환을 담당하는 클래스
        - `Exception`: 예외 및 예외 처리 코드
        - `Response`: 공통 응답 객체 (`PagedContent` 등)
    - **주요 규칙:**
        - `common` 패키지의 클래스는 특정 계층에 종속되지 않아야 합니다.
        - `common` 패키지의 클래스는 **재사용 가능**해야 합니다.
    - **예제:**
        - `com.sonicplayground.geminiboard.common.converter.StringToUserTypeConverter`
        - `com.sonicplayground.geminiboard.common.response.PagedContent`

## 2. 개발 원칙 및 규칙

애플리케이션의 **일관성과 유지보수성을 유지**하기 위해 다음과 같은 개발 원칙을 적용합니다.

- **`Reader`와 `Store`의 분리**
    - `Reader`는 읽기 전용이며 데이터를 수정할 수 없습니다.
    - `Store`는 데이터 저장 및 수정 작업을 담당합니다.
    - **장점:**
        - 읽기/쓰기 분리로 성능 최적화 가능
        - 코드 테스트 용이성 증가

- **호출 계층 규칙**
    - `interface` → `application` (Controller는 `Facade Service`만 호출)
    - `application` → `domain` (Facade Service는 `domain` 서비스만 호출)
    - `domain` → `Store` or `Reader` (domain 서비스끼리 직접 호출 금지)
    - `infrastructure` → `domain` 인터페이스만 구현

- **트랜잭션 관리**
    - 트랜잭션(`@Transactional`)은 `Facade Service`에서 시작해야 합니다.
    - 모든 데이터 변경 작업은 반드시 `Store` 인터페이스를 통해 수행해야 합니다.
    - 읽기 작업은 `Reader`에서 처리하며 트랜잭션을 사용하지 않습니다.

## 3. 추가 고려 사항
- **에러 핸들링**: 예외를 적절히 처리해야 합니다.
- **유효성 검사**: 입력 데이터는 반드시 검증해야 합니다.
- **로깅**: 코드 추적 및 디버깅을 위한 로깅이 필요합니다.
- **코드 리뷰**: 모든 코드 변경 사항은 리뷰를 거쳐야 합니다.
- **테스트**: 단위 테스트 및 통합 테스트를 작성해야 합니다.

이 가이드를 따르면 **일관되고 유지보수 가능한 고품질 애플리케이션**을 개발할 수 있습니다. 🚀