# Testy publiczne zadań Java

Repozytorium zawiera publiczne testy JUnit 5 dla zadań programistycznych.
Każde zadanie jest osobnym modułem Maven i można je uruchamiać niezależnie.

## Wymagania

- JDK 17,
- IntelliJ IDEA albo Eclipse IDE for Java Developers,
- połączenie z internetem podczas pierwszego uruchomienia (Maven musi pobrać
  JUnit i swoje wtyczki).

Do uruchamiania testów z poziomu IDE nie trzeba instalować Mavena osobno.
IntelliJ IDEA zawiera własną dystrybucję Mavena, a Eclipse obsługuje projekty
Maven przez dodatek m2e, który standardowo znajduje się w wydaniu „Eclipse IDE
for Java Developers”. Maven zainstalowany w systemie jest potrzebny tylko do
uruchamiania polecenia `mvn` w terminalu.

## Pobranie testów

Repozytorium można sklonować za pomocą Git:

```bash
git clone https://github.com/js-ivy/java-public-tests.git
cd java-public-tests
```

Alternatywnie można wybrać **Code → Download ZIP**, rozpakować
archiwum i otworzyć rozpakowany katalog w IDE.

## Struktura repozytorium

```text
java-public-tests/
├── pom.xml                 # uruchamianie wszystkich zadań
├── Zadanie1/
│   ├── pom.xml
│   ├── src/                # pliki źródłowe zadania i rozwiązanie
│   └── test/               # testy oraz klasy pomocnicze testów
├── Zadanie2/
│   ├── src/
│   └── test/
├── Zadanie3/
│   ├── src/
│   └── test/
├── Zadanie4/
│   ├── src/
│   └── test/
└── Zadanie5/
    ├── src/
    └── test/
```

Katalogi celowo nie mają standardowych nazw `src/main/java` i
`src/test/java`. Plik `pom.xml` każdego zadania wskazuje Mavenowi odpowiednio
`src` jako katalog kodu produkcyjnego oraz `test` jako katalog kodu testowego.

Przed uruchomieniem testów należy umieścić własną klasę rozwiązania w katalogu
`ZadanieX/src`. Nazwa klasy oraz jej interfejs bazowy muszą być zgodne z treścią
zadania. Plik powinien pozostać w pakiecie domyślnym — nie należy dodawać na
jego początku deklaracji `package`.

## IntelliJ IDEA

1. Wybierz **File → Open** i wskaż główny katalog repozytorium (ten, w którym
   znajduje się nadrzędny `pom.xml`).
2. Jeżeli pojawi się pytanie o sposób otwarcia projektu, wybierz zaimportowanie
   go jako projekt Maven. Poczekaj na zakończenie pobierania zależności.
3. Ustaw JDK 17 w **File → Project Structure → Project → SDK**, jeżeli IDE nie
   wybrało go automatycznie.
4. Dodaj swoją klasę rozwiązania do `ZadanieX/src`.
5. Otwórz okno **View → Tool Windows → Maven**.
6. Aby sprawdzić jedno zadanie, rozwiń jego moduł, następnie **Lifecycle** i
   uruchom dwukrotnym kliknięciem cel **test**.
7. Aby sprawdzić wszystkie zadania, uruchom **test** w sekcji **Lifecycle**
   nadrzędnego projektu `java-public-tests`. Wszystkie pięć rozwiązań musi
   być wtedy obecne.

Można również kliknąć prawym przyciskiem myszy klasę testową lub katalog
`test` i wybrać **Run Tests**. Uruchomienie celu Maven `test` jest jednak
najlepszym sposobem sprawdzenia dokładnie takiej samej konfiguracji jak w
terminalu.

## Eclipse

1. Wybierz **File → Import… → Maven → Existing Maven Projects**.
2. Jako **Root Directory** wskaż główny katalog repozytorium.
3. Zaznacz nadrzędny projekt i wszystkie wykryte moduły `ZadanieX`, a następnie
   wybierz **Finish**. Poczekaj na pobranie zależności i zbudowanie workspace.
4. Jeżeli trzeba ustawić JDK, wybierz **Window → Preferences → Java → Installed
   JREs**, dodaj JDK 17 i ustaw je jako domyślne.
5. Dodaj własną klasę rozwiązania do `ZadanieX/src`, a następnie odśwież projekt
   klawiszem **F5**.
6. Kliknij prawym przyciskiem myszy wybrany moduł `ZadanieX` i wybierz
   **Run As → Maven test**.

Aby uruchomić wszystkie moduły, wybierz **Run As → Maven test** dla
nadrzędnego projektu. Pojedynczą klasę testową można też uruchomić przez
**Run As → JUnit Test**.

Jeżeli Eclipse nie rozpozna poprawnie katalogów źródłowych po zmianie pliku
`pom.xml`, kliknij projekt prawym przyciskiem i wybierz
**Maven → Update Project…**.
