# Library Management

Spring Boot tabanlı bir kütüphane yönetim sistemi.

## Teknolojiler

- Java 21
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- Spring Validation
- H2 Database (runtime)
- Lombok
- Maven

## Gereksinimler

- JDK 21+
- Maven (veya proje kökündeki `mvnw` / `mvnw.cmd` sarmalayıcısı)

## Çalıştırma

```bash
./mvnw spring-boot:run
```

Windows:

```cmd
mvnw.cmd spring-boot:run
```

## Test

```bash
./mvnw test
```

## Proje Yapısı

```
src/main/java/com/muhammed/library_management/   Uygulama kaynak kodu
src/main/resources/application.properties        Uygulama yapılandırması
src/test/java/...                                 Testler
```
