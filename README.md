# Dokumentacja Projektowa - Notatnik Java

## 1. Wykaz Technologii i Narzędzi
Aplikacja została zbudowana w oparciu o nowoczesny stos technologiczny dla platformy Java, kładąc nacisk na natywne zachowanie interfejsu oraz przejrzystość kodu.

*   **Język**: Java 17 (OpenJDK)
*   **Interfejs Użytkownika**: Java Swing (wykorzystanie bibliotek `javax.swing` oraz `java.awt`)
*   **System Budowania**: Maven
*   **Zarządzanie Zmianami**: `UndoManager` (standardowa obsługa historii edycji)
*   **Zarządzanie Plikami**: `JFileChooser` z systemowymi filtrami rozszerzeń

---

## 2. Architektura i Działanie Aplikacji
Aplikacja oparta jest na architekturze modułowej, dzielącej odpowiedzialności na trzy główne klasy:

### SimpleNotepad (Punkt Wejścia)
Klasa inicjalizująca główne okno aplikacji (`JFrame`).
*   Konfiguruje systemowy wygląd i zachowanie (`System Look and Feel`).
*   Zarządza rozmieszczeniem komponentów (układ `BorderLayout`).
*   Obsługuje cykl życia aplikacji i bezpieczne zamykanie okna z weryfikacją zmian.

### Editor (Logika Biznesowa)
Główny silnik aplikacji zarządzający stanem dokumentu.
*   **Obszar Tekstowy**: Wykorzystuje `JTextArea` z dostosowanymi marginesami i czcionką.
*   **Śledzenie zmian**: Monitoruje treść za pomocą `DocumentListener`, zarządzając flagą `isModified`.
*   **Obsługa Plików**: Hermetyzuje logikę otwierania i zapisywania plików tekstowych.
*   **Wyszukiwanie i Zamiana**: Implementuje mechanizmy oparte na wyrażeniach regularnych (`Regex`) z wizualnym podświetlaniem wyników.
*   **Pasek Stanu**: Aktualizuje w czasie rzeczywistym informacje o pozycji kursora (linia/kolumna).

### MenuBar (Interfejs Sterowania)
Definiuje hierarchię menu i wiąże akcje użytkownika z logiką edytora.
*   Implementuje system skrótów klawiszowych (Accelerators).
*   Zapewnia szybki dostęp do funkcji edycyjnych poprzez mnemotechniki (klawisz Alt).

---

## 3. Kluczowe Funkcjonalności
*   **Pełna Edycja Tekstu**: Wsparcie dla wielopoziomowego cofania i ponawiania zmian (`Undo`/`Redo`).
*   **Inteligentne Zarządzanie Plikami**: Tworzenie nowych dokumentów, otwieranie istniejących oraz zapisywanie zmian.
*   **Ochrona Danych**: System ostrzegania przed utratą niezapisanych danych.
*   **Narzędzia Tekstowe**: Wyszukiwanie fraz w tekście oraz ich masowa zamiana.
*   **UX/UI**: Natywny wygląd kontrolek, pasek stanu z informacjami o kursorze oraz wygodne marginesy edycyjne.

---

## 4. Instrukcja Obsługi (Skróty Klawiszowe)
Aplikacja została zoptymalizowana pod kątem pracy z klawiaturą:

| Funkcja | Skrót Klawiszowy |
| :--- | :--- |
| **Nowy plik** | `Ctrl + N` |
| **Otwórz plik** | `Ctrl + O` |
| **Zapisz plik** | `Ctrl + S` |
| **Wyjdź z programu** | `Ctrl + Q` |
| **Cofnij** | `Ctrl + Z` |
| **Ponów** | `Ctrl + Y` |
| **Szukaj** | `Ctrl + F` |
| **Zamień** | `Ctrl + H` |

---

## 5. Kompilacja i Uruchomienie
Wymagania: JDK 17+ oraz Maven.

**Budowanie projektu:**
```bash
mvn compile
```

**Uruchomienie aplikacji:**
```bash
mvn exec:java
```
