# car-sort-strategy

Консольное приложение для сортировки коллекции автомобилей (`Car`): паттерны **Strategy** и **Builder**, валидация данных, кастомная коллекция. Три участника — три feature-ветки, результат в **`main`**.

**Репозиторий:** https://github.com/ledozeur/car-sort-strategy.git

---

## Команда

| Участник | Ветка | Зона ответственности |
|----------|--------|----------------------|
| **Медведев Артём** | `feature/app-menu-integration` | `Main`, `MenuController`, интеграция сценария, merge в `main` |
| **Ворман Павел** | `feature/car-model-collection-file` | `Car`, `CarValidator`, `CarArray`, `CarParser`, компараторы, `CarFillService`, `FileCarFillService` |
| **Гришков Владимир** | `feature/sort-random-evenodd` | `SortStrategy` и реализации, `EvenOddSortDecorator`, `RandomCarFillService`, `ManualCarFillService`, `ResultFileWriter`, `ParallelCountService` |

---

## Доменная модель `Car`

| Поле | Тип | Ограничения |
|------|-----|-------------|
| `model` | `String` | не пустая, длина 1–60 |
| `powerHp` | `int` | 30–1200 |
| `productionYear` | `int` | 1900–2026 |

**Формат строки** (файл, консоль): `Toyota Camry;181;2020`

Создание объектов — через вложенный **`Car.CarBuilder`** (паттерн Builder). Валидация — в `CarValidator` и при сборке builder.

---

## Запуск

- **JDK:** 17+ (в `pom.xml` указан compiler 23)
- **Точка входа:** `Main` → `menu.MenuController`
- **Maven:** из корня проекта  
  - `mvn test` — тесты (JUnit 5)  
  - запуск main-класса из IDE или `mvn -q exec:java` (при настроенном `exec-maven-plugin` в IDE достаточно Run на `Main`)

**Данные:** каталог `data/` (`cars.txt` — примеры; `cars invalid.txt` — пример с ошибками).  
**Результат сортировки (append):** `data/output/sorted-cars.txt`

---

## Меню (`MenuController`)

| Пункт | Действие |
|-------|----------|
| **1** | Заполнить: файл / случайно / вручную; при непустом списке — заменить или добавить |
| **2** | Показать **исходный** список (без учёта сортировки) |
| **3** | Сортировать (исходный список **не меняется**; результат хранится для п. 4 и 6) |
| **4** | Дописать **последний** результат сортировки в файл |
| **5** | Многопоточный подсчёт вхождений `Car` в **исходном** списке (совпадение по всем полям) |
| **6** | Показать результат **последней** сортировки |
| **0** | Выход |

**Сортировка (п. 3):** Bubble / Insertion; варианты с **Even/Odd** (доп. 1).  
**Поле:** модель, мощность, год или **все поля** (`CarNaturalOrderComparator`: модель → мощность → год).  
Even/Odd по модели недоступен (нужно числовое поле).

**Лимиты:** в файле и при ручном вводе лимит — число **валидных** автомобилей; невалидные строки в коллекцию не попадают.

---

## Интерфейсы проекта

| Интерфейс | Пакет | Назначение |
|-----------|--------|------------|
| `SortStrategy` | `strategy` | Паттерн Strategy: `CarArray sort(CarArray, CarComparator)` |
| `CarFillService` | `fill` | `CarArray create(Integer limit)` — файл / random / ручной ввод |
| `CarComparator` | `comparator` | Сравнение двух `Car` (свой контракт, не сортировка из JDK) |
| `ResultWriter` | `io` | `boolean save(CarArray)` |

**Builder** — класс `Car.CarBuilder`, не интерфейс.

Готовые **алгоритмы сортировки** JDK (`Arrays.sort`, `Collections.sort` для коллекции машин) **не используются**.

---

## Структура проекта

```text
car-sort-strategy/
├── pom.xml
├── README.md
├── data/
│   ├── cars.txt
│   ├── cars invalid.txt
│   └── output/
│       └── sorted-cars.txt          # создаётся при записи (п. 4 меню)
└── src/
    ├── main/java/
    │   ├── Main.java
    │   ├── menu/
    │   │   └── MenuController.java
    │   ├── model/
    │   │   └── Car.java             # + вложенный CarBuilder
    │   ├── validation/
    │   │   └── CarValidator.java
    │   ├── parser/
    │   │   └── CarParser.java
    │   ├── collection/
    │   │   └── CarArray.java
    │   ├── comparator/
    │   │   ├── CarComparator.java
    │   │   ├── CarModelComparator.java
    │   │   ├── CarPowerHpComparator.java
    │   │   ├── CarProductionYearComparator.java
    │   │   └── CarNaturalOrderComparator.java
    │   ├── fill/
    │   │   ├── CarFillService.java
    │   │   ├── FileCarFillService.java
    │   │   ├── RandomCarFillService.java
    │   │   └── ManualCarFillService.java
    │   ├── strategy/
    │   │   ├── SortStrategy.java
    │   │   ├── BubbleSortStrategy.java
    │   │   ├── InsertionSortStrategy.java
    │   │   └── EvenOddSortDecorator.java
    │   ├── io/
    │   │   ├── ResultWriter.java
    │   │   └── ResultFileWriter.java
    │   └── concurrency/
    │       └── ParallelCountService.java
    └── test/java/                   # JUnit 5
        ├── model/CarTest.java
        ├── validation/CarValidatorTest.java
        ├── collection/CarArrayTest.java
        ├── parser/CarParserTest.java
        ├── comparator/CarComparatorTest.java
        ├── fill/                    # File, Manual, Random
        ├── sort/                    # Bubble, Insertion, EvenOdd
        ├── io/ResultFileWriterTest.java
        └── concurrency/ParallelCountServiceTest.java
```

---

## Дополнительные задания

| № | Реализация |
|---|------------|
| **1** | `EvenOddSortDecorator` + пункты 3–4 в меню сортировки |
| **2** | `ResultFileWriter`, режим append → `data/output/sorted-cars.txt` |
| **3** | Stream: `FileCarFillService` (`Files.lines`), `RandomCarFillService` (`IntStream`) |
| **3\*** | Кастомная коллекция `CarArray` |
| **4** | `ParallelCountService` — `Thread`, разбиение по индексам, вывод в консоль |

---

## Git

Три feature-ветки (по одной на участника) смержены в **`main`**.  
Ссылка на репозиторий передаётся по требованиям курса.
