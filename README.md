# car-sort-strategy

Сортировка автомобилей (`Car`): **Strategy**, **Builder**, валидация, 4 feature-ветки → `main`.

**Дедлайн:** 11.10.2026  
**Репо:** https://github.com/ledozeur/car-sort-strategy.git  

---

## Команда

| Участник | Ветка | Зона |
|----------|--------|------|
| **Медведев Артём** | `feature/app-menu-integration` | Меню, merge PR, отчёты, защита (экран) |
| **Павел Ворман** | `feature/car-model-collection-file` | Car, Builder, Validator, Comparator, CarCollection, fill из **файла** |
| **Владимир Гришков** | `feature/sort-random-evenodd` | Сортировки, fill **random**, доп. **1** |
| **Александр Веселков** | `feature/manual-io-parallel` | Parser, fill **вручную**, доп. **2**, **4**, тесты файла/count |

---

## Car

Поля: `model` (String), `powerHp` (30–1200), `productionYear` (1900–2026).  
Файл: `Toyota Camry;181;2020` → папка `data/`.

**Запуск:** JDK 17+, main-класс `ru.aston.Main` (меню — `MenuController`).

---

## Интерфейсы (свои, не из библиотек)

| Интерфейс | Пакет | Кто | Зачем |
|-----------|--------|-----|--------|
| **`SortStrategy`** | `strategy` | **Владимир** | **Обязательно по ТЗ** — паттерн Strategy: разные алгоритмы сортировки с одной сигнатурой. |
| **`CarFillService`** | `fill` | **Павел** (объявляет) | Один контракт для трёх способов заполнения; в меню можно вызывать через интерфейс. |

**`SortStrategy`** — пример методов:

```java
void sort(Car[] array, int length, Comparator<Car> comparator);
```

**`CarFillService`** — пример:

```java
void fill(CarCollection target);  // внутри: файл / random / Scanner — у каждого класса по-своему
```

**Не интерфейс по ТЗ:** **`CarBuilder`** — это **класс** (паттерн Builder).  
**`java.util.Comparator<Car>`** — для сравнения полей можно использовать **стандартный** интерфейс; реализации — ваши статические методы в `CarComparators` (обычно это лямбды или классы, не «скачанный» Comparator из готовой библиотеки сортировки).

---

## Структура программы (дерево)

```text
car-sort-strategy/
├── pom.xml
├── README.md
├── data/
│   ├── cars.txt
│   └── cars_bad.txt
├── output/
│   └── sorted_cars.txt
└── src/main/java/ru/aston/
    ├── Main.java
    │
    ├── menu/
    │   └── MenuController.java                 # Артём
    │
    ├── model/
    │   ├── Car.java                            # Павел
    │   └── CarBuilder.java                     # Павел (класс, не interface)
    │
    ├── validation/
    │   └── CarValidator.java                   # Павел
    │
    ├── comparator/
    │   └── CarComparators.java                 # Павел (Comparator<Car>, не свой interface)
    │
    ├── collection/
    │   └── CarCollection.java                  # Павел
    │
    ├── parser/
    │   └── CarParser.java                      # Александр
    │
    ├── fill/
    │   ├── CarFillService.java                 # interface — Павел
    │   ├── FileCarFillService.java             # implements CarFillService — Павел
    │   ├── RandomCarFillService.java           # implements CarFillService — Владимир
    │   └── ManualCarFillService.java           # implements CarFillService — Александр
    │
    ├── strategy/
    │   ├── SortStrategy.java                   # interface — Владимир
    │   ├── BubbleSortStrategy.java             # implements SortStrategy
    │   ├── InsertionSortStrategy.java          # implements SortStrategy
    │   └── EvenOddPowerSortStrategy.java       # implements SortStrategy — доп. 1
    │
    ├── sort/
    │   ├── SortAlgorithm.java                  # enum для меню — Владимир
    │   └── SortField.java                      # enum — Владимир
    │
    ├── io/
    │   └── ResultFileWriter.java               # Александр (класс; interface не обязателен)
    │
    ├── concurrency/
    │   └── ParallelCountService.java           # Александр
    │
    └── tests/
        ├── CarBuilderTest.java                 # Павел
        ├── CarCollectionTest.java              # Павел
        ├── BubbleSortTest.java                 # Владимир
        ├── EvenOddSortTest.java                # Владимир
        ├── CarParserTest.java                  # Александр
        └── ParallelCountTest.java              # Александр
```

---

## Доп. задания

| № | Кто |
|---|-----|
| 1 — чёт/нечёт по powerHp | Владимир |
| 2 — append в `output/sorted_cars.txt` | Александр |
| 3 — Stream при fill | Павел (файл), Владимир (random), Александр (ручной) |
| 3* — `CarCollection` | Павел |
| 4 — многопоточный count | Александр |

---

## Сдача

4 ветки смержены в `main`, ссылка в чат курса **11.10**, презентация ≤ 20 мин.
