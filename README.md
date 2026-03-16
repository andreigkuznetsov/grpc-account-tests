
# gRPC Account API Automation Framework

Автоматизированный тестовый фреймворк для тестирования **gRPC API пользовательских аккаунтов**.

Проект демонстрирует архитектуру **масштабируемого automation framework для тестирования gRPC сервисов**, включая:

- Unary RPC
- Server Streaming
- Client Streaming
- Bidirectional Streaming
- бизнес‑ошибки и gRPC статусы
- сценарии с email‑подтверждениями

Фреймворк спроектирован так, чтобы быть максимально похожим на **production automation framework**, применяемый для тестирования backend‑сервисов.

---

# 🎯 Цель проекта

Основная цель проекта — продемонстрировать:

- архитектуру **gRPC automation framework**
- тестирование **Unary / Server Stream / Client Stream / Bidi Stream**
- автоматизацию пользовательских сценариев
- тестирование **gRPC статусов и бизнес‑ошибок**
- читаемые **бизнес‑ориентированные тесты**
- масштабируемую структуру тестового проекта

---

# ⚙️ Технологический стек

| Технология | Назначение |
|---|---|
| Java 21 | основной язык |
| Gradle | система сборки |
| gRPC Java | клиент для gRPC сервисов |
| Protocol Buffers | описание API |
| JUnit 5 | тестовый фреймворк |
| SLF4J | abstraction логирования |
| Logback | backend логирования |
| Java Faker | генерация тестовых данных |

---

# 🧠 Архитектурные принципы

Фреймворк построен на нескольких ключевых принципах.

## Разделение ответственности

Каждый слой отвечает только за свою задачу.

```
Tests
  ↓
Steps
  ↓
Client
  ↓
gRPC Service
```

Это позволяет:

- уменьшить дублирование кода
- улучшить читаемость тестов
- упростить поддержку проекта

---

# 🏗 Архитектурная диаграмма

```
                  ┌─────────────────────┐
                  │        Tests        │
                  │ JUnit Test classes  │
                  └──────────┬──────────┘
                             │
                             ▼
                  ┌─────────────────────┐
                  │        Steps        │
                  │ UserFlowSteps       │
                  │ MailSteps           │
                  └──────────┬──────────┘
                             │
                             ▼
                  ┌─────────────────────┐
                  │        Client       │
                  │    AccountClient    │
                  │   gRPC channel      │
                  └──────────┬──────────┘
                             │
                             ▼
                  ┌─────────────────────┐
                  │     gRPC Service    │
                  │  AccountService     │
                  └─────────────────────┘
```

---

# 📦 Структура проекта

Пример структуры проекта:

```
src
 └── test
      └── java
           └── account
                ├── assertions
                │     └── GrpcAssertions
                │
                ├── base
                │     └── BaseGrpcTest
                │
                ├── client
                │     └── AccountClient
                │
                ├── model
                │     ├── TestUser
                │     └── AccountUpdateData
                │
                ├── steps
                │     ├── UserFlowSteps
                │     └── MailSteps
                │
                ├── support
                │     └── TestDataGenerator
                │
                └── tests
                      ├── RegisterAccountPositiveTest
                      ├── LoginNegativeTest
                      ├── GetCurrentAccountPositiveTest
                      ├── ChangeAccountPasswordPositiveTest
                      ├── RegisterAccountClientStreamPositiveTest
                      └── GetAccountsByLoginDuplexStreamPositiveTest
```

---

# 🧪 Типы тестируемых RPC

Фреймворк покрывает все основные типы gRPC вызовов.

| Тип RPC | Пример метода |
|------|------|
| Unary | registerAccount |
| Unary | login |
| Unary | getCurrentAccount |
| Server Streaming | getAccountsServerStream |
| Client Streaming | registerAccountClientStream |
| Bidirectional Streaming | getAccountsByLoginDuplexStream |

---

# 🔐 Пример пользовательского сценария

### Регистрация пользователя

```
registerAccount
     ↓
получение письма
     ↓
извлечение activationToken
     ↓
activateAccount
     ↓
login
```

---

# 🔄 Пример streaming теста

Пример **bidirectional streaming теста**.

```java
@Test
void getAccountsByLoginDuplexStreamShouldReturnUserForExistingLogin() {

    TestUser user = userFlowSteps.registerActivateAndLogin();

    var responses = asyncStub
            .getAccountsByLoginDuplexStream()
            .send(user.login())
            .collect();

    assertFalse(responses.isEmpty());

    var response = responses.getFirst();

    assertEquals(user.login(), response.getLogin());
    assertTrue(response.hasUser());
}
```

Этот тип теста проверяет:

- bidirectional streaming RPC
- корректность ответа сервиса
- наличие пользователя в ответе

---

# 📊 Логирование

Используется **SLF4J + Logback**.

Логируются:

- gRPC методы
- параметры запроса
- ответы сервиса
- пользовательские сценарии

Пример:

```
Starting user flow: register -> activate -> login
Generated test user: login=user123, email=user@mail.test
RegisterAccount success
Activation token received
Login success
```

Это значительно упрощает диагностику тестов.

---

# ▶️ Запуск тестов

Запуск всех тестов:

```
gradle test
```

или

```
./gradlew test
```

---

# 🔧 Параметры подключения

Сервис можно настроить через system properties.

```
grpc.host
grpc.port
```

Пример:

```
gradle test -Dgrpc.host=localhost -Dgrpc.port=5055
```

По умолчанию:

```
grpc.host=185.185.143.231
grpc.port=5055
```

---

# 🔮 Возможные улучшения

Проект можно расширить:

- Allure отчёты
- CI/CD интеграция
- Testcontainers
- retry‑механизмы для flaky тестов
- contract testing
- нагрузочное тестирование API

---

# 💡 Что демонстрирует проект

Этот проект демонстрирует навыки:

- разработки automation framework
- тестирования gRPC API
- архитектуры тестов
- автоматизации пользовательских сценариев
- интеграционного тестирования backend‑сервисов

---

# 👤 Автор

### Андрей Кузнецов

QA Engineer / QA Automation Engineer

Специализация:

- API тестирование
- Java automation
- архитектура тестовых фреймворков
- тестирование backend систем
