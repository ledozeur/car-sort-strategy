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
