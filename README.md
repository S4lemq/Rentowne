# Rentowne: o projekcie 🏠

**Aplikacja Webowa do Zarządzania Nieruchomościami**

Niniejsza aplikacja to zaawansowane narzędzie webowe, dedykowane do efektywnego zarządzania procesem najmu nieruchomości, skierowane zarówno do najemców, jak i wynajmujących. Rozwiązanie to integruje kompleksowe mechanizmy bezpieczeństwa opracowane z wykorzystaniem Spring Security, w tym zaawansowaną autentykację i autoryzację przy użyciu tokenów JWT, a także dwuskładnikową weryfikację (2FA) za pośrednictwem Google Authenticator, zapewniając ochronę danych użytkowników i transakcji.

Kluczowe funkcje dla najemców obejmują:
- Możliwość zakładania kont oraz implementacji dwuskładnikowego uwierzytelnienia za pomocą Google Authenticator, oraz odzyskiwanie hasła poprzez wygenerowany link email
- Dodawanie, edycję oraz zarządzanie nieruchomościami i lokatorami, w tym łatwe przypisywanie dostawców usług i liczników do poszczególnych nieruchomości.
- Możliwość dodawania zdjęć oraz ich dopasowywania
- Wyświetlania danych, w tym stronicowanie, filtrowanie i sortowanie zaimplementowane za pomocą QueryDsl.
- Integrację z systemem płatności Przelewy24, ułatwiającą szybkie rozliczenia.
- Możliwość eksportu danych do formatu CSV oraz analizę rozliczeń za pomocą wykresów słupkowych.
- Personalizację ustawień aplikacji, w tym zmianę języka interfejsu oraz personalizację profilu użytkownika.

Funkcje dla wynajmujących zawierają:
- Przeglądanie opłaconych i nieopłaconych rozliczeń w przejrzystej formie tabelarycznej.
- Bezpośrednią możliwość opłacania świadczeń przez system Przelewy24.

Ponadto, aplikacja wyposażona jest w szereg narzędzi automatyzujących procesy administracyjne, takich jak zaplanowane zadania do zarządzania nieruchomościami, wysyłanie e-maili, oraz eksport danych.

