# Rentowne - Aplikacja Webowa do Zarządzania Nieruchomościami 🏠

Niniejsza aplikacja to zaawansowane narzędzie webowe, dedykowane do efektywnego zarządzania procesem najmu nieruchomości, skierowane zarówno do najemców, jak i wynajmujących. Rozwiązanie to integruje kompleksowe mechanizmy bezpieczeństwa opracowane z wykorzystaniem Spring Security, w tym zaawansowaną autentykację i autoryzację przy użyciu tokenów JWT, a także dwuskładnikową weryfikację (2FA) za pośrednictwem Google Authenticator, zapewniając ochronę danych użytkowników i transakcji.

**Kluczowe funkcje dla najemców obejmują:**
- Możliwość zakładania kont oraz implementacji dwuskładnikowego uwierzytelnienia za pomocą Google Authenticator, oraz odzyskiwanie hasła poprzez wygenerowany link email 🔐
- Dodawanie, edycję oraz zarządzanie nieruchomościami i lokatorami, w tym łatwe przypisywanie dostawców usług i liczników do poszczególnych nieruchomości 🏢➕
- Możliwość dodawania zdjęć oraz ich dopasowywania 🖼️
- Wyświetlania danych, w tym stronicowanie, filtrowanie i sortowanie zaimplementowane za pomocą QueryDsl 🔍
- Integrację z systemem płatności Przelewy24, ułatwiającą szybkie rozliczenia 💳
- Możliwość eksportu danych do formatu CSV oraz analizę rozliczeń za pomocą wykresów słupkowych 📊
- Personalizację ustawień aplikacji, w tym zmianę języka interfejsu oraz personalizację profilu użytkownika ⚙️🌐

**Funkcje dla wynajmujących zawierają:**
- Przeglądanie opłaconych i nieopłaconych rozliczeń w przejrzystej formie tabelarycznej 💼
- Bezpośrednią możliwość opłacania świadczeń przez system Przelewy24 💰

Ponadto, aplikacja wyposażona jest w szereg narzędzi automatyzujących procesy administracyjne, takich jak zaplanowane zadania do zarządzania nieruchomościami, wysyłanie e-maili, oraz eksport danych.

# Użyte technologie - BACKEND
- Java 17
- Spring Boot 3.2.0
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- Spring Boot Starter Web
- Spring Boot Starter WebFlux
- Spring Boot Starter Actuator
- Spring Boot Starter Mail
- Spring Boot Starter Validation
- SpringDoc OpenAPI
- Liquibase Core
- Lombok
- JJwt
- TOTP
- Guava
- Commons IO
- Commons Codec
- Commons CSV
- QueryDSL JPA 5.0
- JUnit
- AssertJ
- Mockito

# Użyte technologie - FRONTEND
- Angular 15.2.0
- Angular Material 15.2.6
- RxJS 7.8.0
- @ngx-translate/core
- Chart.js
- Cropperjs
- Express
- JWT-decode
- Moment
- Ng-otp-input
- TypeScript
- Jasmine-core
- Karma
