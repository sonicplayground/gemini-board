# gemini-board
## 설정

*   **`spring.application.name=geminiboard`:** 애플리케이션 이름.
*   **`spring.datasource.url=jdbc:mysql://localhost:13306/gemini?serverTimezone=Asia/Seoul`:** MySQL 데이터베이스 URL.
*   **`spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver`:** MySQL JDBC 드라이버.
*   **`spring.datasource.username={{your_database_user_name}}`:** 데이터베이스 사용자 이름.
*   **`spring.datasource.password={{your_database_user_password}}`:** 데이터베이스 비밀번호.
*   **`spring.jpa.show-sql=true`:** SQL 쿼리 콘솔 출력.
*   **`spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect`**: hibernate의 방언을 MySQL로 설정합니다.

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
    ```
6.  **애플리케이션 접속**
    *   애플리케이션이 성공적으로 시작된 후에는 아직 구현되지 않은 API를 통해 상호 작용할 수 있습니다.

## 향후 개발 계획

*   **기능 구현:**
    *   `Vehicle`, `User`, `MaintenanceShop`, `Mechanic`, `VehicleMaintenanceHistory` 엔티티에 대한 CRUD 로직 구현.
    *   REST API 엔드포인트 개발 및 테스트.
*   **보안 강화:**
    *   Spring Security를 활용한 사용자 인증 및 권한 부여 완벽 구현.
* **이력관리**: 데이터의 생성, 수정, 삭제에 대한 이력을 남겨야 합니다.
*   **테스트:**
    *   단위 테스트 및 통합 테스트 추가.