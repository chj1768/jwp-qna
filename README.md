# 1단계 - 엔티티 매핑

## 기능 요구사항

- QnA 서비스를 만들어가면서 JPA로 실제 도메인 모델을 어떻게 구성하고 객체와 테이블을 어떻게 매핑해야 하는지 알아본다.

- 아래의 DDL(Data Definition Language)을 보고 유추하여 엔티티 클래스와 리포지토리 클래스를 작성해 본다.

- `@DataJpaTest`를 사용하여 학습 테스트를 해 본다.

```
create table answer
(
id          bigint generated by default as identity,
contents    clob,
created_at  timestamp not null,
deleted     boolean   not null,
question_id bigint,
updated_at  timestamp,
writer_id   bigint,
primary key (id)
)
create table delete_history
(
id            bigint generated by default as identity,
content_id    bigint,
content_type  varchar(255),
create_date   timestamp,
deleted_by_id bigint,
primary key (id)
)
create table question
(
id         bigint generated by default as identity,
contents   clob,
created_at timestamp    not null,
deleted    boolean      not null,
title      varchar(100) not null,
updated_at timestamp,
writer_id  bigint,
primary key (id)
)
create table user
(
id         bigint generated by default as identity,
created_at timestamp   not null,
email      varchar(50),
name       varchar(20) not null,
password   varchar(20) not null,
updated_at timestamp,
user_id    varchar(20) not null,
primary key (id)
)

alter table user
add constraint UK_a3imlf41l37utmxiquukk8ajc unique (user_id)
```

### 힌트
- Spring Data JPA 사용 시 아래 옵션은 동작 쿼리를 로그로 확인할 수 있게 해준다.

```
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.show-sql=true
```

- Auto-configured Data JPA Tests

```
@DataJpaTest
class StationRepositoryTest {
@Autowired
private StationRepository stations;

    @Test
    void save() {
        Station expected = new Station("잠실역");
        Station actual = stations.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName())
        );
    }

    @Test
    void findByName() {
        String expected = "잠실역";
        stations.save(new Station(expected));
        String actual = stations.findByName(expected).getName();
        assertThat(actual).isEqualTo(expected);
    }
}
```
- H2 데이터베이스를 사용한다면 아래의 프로퍼티를 추가하면 MySQL Dialect을 사용할 수 있다.

```
spring.datasource.url=jdbc:h2:~/test;MODE=MySQL
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL57Dialect
```

