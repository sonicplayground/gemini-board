# 지니보드 (Gemini Board) - 차량 관리 서비스

지니보드는 차량과 관련된 모든 정보를 효율적으로 관리할 수 있도록 돕는 플랫폼입니다. Spring Boot와 Gradle을 기반으로 구축되었으며, MySQL 데이터베이스를 사용하여 데이터를 저장하고 관리합니다. 본 프로젝트는 사용자가 차량 정보, 정비 이력, 정비소 정보 등을 체계적으로 관리할 수 있는 기능을 제공하는 것을 목표로 합니다.

## 프로젝트 개요

지니보드는 다음과 같은 주요 기능을 제공합니다:

*   **차량 관리:** 사용자는 자신의 차량 정보를 등록, 수정, 삭제, 조회할 수 있습니다.
*   **사용자 관리:** 사용자는 개인 정보를 관리하고, 사용자 타입(서비스 관리자, 서비스 이용자, 정비소 관리자)을 선택할 수 있습니다.
*   **정비소 및 정비사 관리:** 정비소 관리자는 정비소 정보와 소속 정비사 정보를 관리할 수 있습니다.
*   **차량 정비 이력 관리:** 사용자는 차량의 정비 이력을 기록하고, 조회하며, 검색할 수 있습니다.
*   **접근 제어 및 보안:** 로그인 및 사용자 권한 관리를 통해 서비스 접근을 제어하고 데이터를 보호합니다.

## 아키텍처 개요

지니보드 애플리케이션은 다음과 같은 레이어드 아키텍처를 기반으로 설계되었습니다.

*   **`interface`**: 외부 클라이언트와의 통신을 담당합니다. (컨트롤러, DTO)
*   **`application`**: `Facade Service`를 통해 비즈니스 로직을 조율합니다.
*   **`domain`**: 핵심 비즈니스 로직을 담당합니다. (서비스, 엔티티, Store, Reader, Command, Enum)
*   **`infrastructure`**: `domain` 계층의 `Store` 및 `Reader` 인터페이스를 구현하며, 외부 시스템과의 통신을 처리합니다. (JpaRepository)
*   **`common`**: 애플리케이션 전반에 걸쳐 사용되는 유틸리티, 상수, 예외 처리 코드를 포함합니다.

## 패키지 구조 및 역할

-   **`interface`**
    -   **역할:** 외부 클라이언트(웹 브라우저, 모바일 앱, 기타 서비스)와의 통신을 담당합니다. 모든 입력 요청의 진입점이며, 출력 응답의 출구점입니다.
    -   **구성 요소:**
        -   `Controller`: HTTP 요청을 받아 유효성을 검사한 후, `Facade Service`에 위임합니다.
        -   `DTO`: 클라이언트와 데이터를 주고받는 구조를 정의합니다.
    -   **예시:**
        -   `com.sonicplayground.geminiboard.interfaces.user.UserController`
        -   `com.sonicplayground.geminiboard.interfaces.user.UserDto`

-   **`application`**
    -   **역할:** `Facade Service`를 포함하여 비즈니스 로직을 조율하는 계층입니다. `interface` 계층과 `domain` 계층 사이에서 중재자 역할을 합니다.
    -   **구성 요소:**
        -   `Facade Service`: 여러 `domain` 서비스를 호출하여 비즈니스 로직을 실행하는 계층입니다.
    -   **예시:**
        -   `com.sonicplayground.geminiboard.application.user.UserApplicationService`

-   **`domain`**
    -   **역할:** 핵심 비즈니스 로직을 담당하는 도메인 계층입니다.
    -   **구성 요소:**
        -   `Service`: 특정 도메인 개념에 대한 비즈니스 로직을 구현합니다. (예: `UserService`)
        -   `Entity`: 핵심 비즈니스 객체를 표현하며, 속성과 관계를 포함합니다. (예: `User`)
        -   `Store`: 데이터 저장을 위한 인터페이스를 정의합니다. (예: `UserStore`)
        -   `Reader`: 읽기 전용 데이터 접근을 위한 인터페이스를 정의합니다. (예: `UserReader`)
        - `Command`: 특정 비즈니스 로직 수행을 위한 명령 객체입니다.
        - `Enum`: 열거형 타입을 정의합니다.
    -   **예시:**
        -   `com.sonicplayground.geminiboard.domain.user.User`
        -   `com.sonicplayground.geminiboard.domain.user.UserService`
        -   `com.sonicplayground.geminiboard.domain.user.UserStore`
        -   `com.sonicplayground.geminiboard.domain.user.UserReader`
        - `com.sonicplayground.geminiboard.domain.user.UserCommand`
        - `com.sonicplayground.geminiboard.domain.user.Gender`
        - `com.sonicplayground.geminiboard.domain.user.UserType`

-   **`infrastructure`**
    -   **역할:** `domain` 계층에서 정의한 `Store` 및 `Reader` 인터페이스를 구현하는 계층입니다. 또한, 이메일 전송 등 외부 시스템과의 통신을 담당합니다.
    -   **구성 요소:**
        -   `StoreImpl`: `Store` 인터페이스를 JPA 등을 활용해 구현합니다.
        -   `ReaderImpl`: `Reader` 인터페이스를 구현하여 읽기 전용 데이터를 제공합니다.
        - `JpaRepository`: 데이터베이스와 상호작용하기 위한 JPA Repository입니다.
    -   **예시:**
        -   `com.sonicplayground.geminiboard.infrastructure.user.UserStoreImpl`
        - `com.sonicplayground.geminiboard.infrastructure.user.UserReaderImpl`
        - `com.sonicplayground.geminiboard.infrastructure.user.UserJpaRepository`

- **`common`**
    - **역할**: 애플리케이션 전반에서 **공통적으로 사용되는 유틸리티, 상수, 예외 처리 코드**를 포함합니다.
    - **구성 요소:**
        - `Utils`: 공통적으로 사용되는 유틸리티 클래스
        - `Constants`: 전역 상수 정의
        - `Converter`: 데이터 타입 변환을 담당하는 클래스
        - `Exception`: 예외 및 예외 처리 코드
        - `Response`: 공통 응답 객체 (`PagedContent` 등)
    -   **예제:**
        - `com.sonicplayground.geminiboard.common.converter.StringToUserTypeConverter`
        - `com.sonicplayground.geminiboard.common.response.PagedContent`

## 개발 원칙 및 규칙

지니보드 애플리케이션은 다음과 같은 개발 원칙과 규칙을 준수합니다.

-   **`Reader`와 `Store`의 분리:**
    -   `Reader`는 읽기 전용 작업만 담당합니다.
    -   `Store`는 데이터 저장 및 수정 작업을 담당합니다.
-   **호출 계층 규칙:**
    -   `interface` → `application` (Controller는 `Facade Service`만 호출)
    -   `application` → `domain` (Facade Service는 `domain` 서비스만 호출)
    -   `domain` → `Store` or `Reader` (`domain` 서비스끼리 직접 호출 금지)
    - `infrastructure` -> `domain` 인터페이스만 구현
-   **트랜잭션 관리:**
    -   트랜잭션(`@Transactional`)은 `Facade Service`에서 시작합니다.
    -   모든 데이터 변경 작업은 반드시 `Store` 인터페이스를 통해 수행해야 합니다.
    - 읽기 작업은 `Reader`에서 처리하며 트랜잭션을 사용하지 않습니다.

## 사용 기술 스택

*   **Java:** 주요 프로그래밍 언어. (Java 17)
*   **Spring Boot:** 독립 실행형, 프로덕션급 Spring 기반 애플리케이션 구축.
*   **Spring Data JPA:** 데이터 접근을 위한 Spring 프레임워크.
*   **Spring Security:** 사용자 인증 및 권한 부여.
*   **Spring Web**: Rest API를 만들기 위한 spring framework입니다.
*   **MySQL:** 관계형 데이터베이스.
*   **Gradle:** 빌드 및 의존성 관리.
*   **JPA (Java Persistence API):** 객체 관계 매핑 프레임워크.
*   **Lombok:** 반복적인 코드(getter, setter 등)를 자동 생성.
*   **Validation**: 데이터 유효성 검사를 위한 라이브러리
*   **JUnit**: 단위 테스트


## 시작하기

1.  **사전 준비 사항:**
    *   Java JDK 17 이상
    *   Gradle 8.5 이상
    *   `localhost:13306`에서 실행 중인 MySQL 서버, `gemini` 데이터베이스, `gemini_user` 사용자, `gemini_password1!` 비밀번호 설정.

2.  **프로젝트 복제:**
    ```bash
    git clone <repository-url>
    ```

3.  **프로젝트 디렉토리로 이동:**
    ```bash
    cd gemini-board
    ```

4.  **프로젝트 빌드:**
    ```bash
    ./gradlew clean build
    ```

5.  **애플리케이션 실행:**
    ```bash
    ./gradlew bootRun