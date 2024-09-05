package com.povush.modusvivendi.data.local

import com.povush.modusvivendi.data.dataclass.Difficulty
import com.povush.modusvivendi.data.dataclass.Quest
import com.povush.modusvivendi.data.dataclass.QuestType
import com.povush.modusvivendi.data.dataclass.Task

object LocalQuestsDataProvider {
    var allQuests = mutableListOf(
        Quest(
            title = "Код реальности II",
            difficulty = Difficulty.HIGH,
            description = "Итогом длительных парламентарных дебатов на тему выбора направления в сфере IT стала разработка мобильных приложений. Основными преимуществами этого выбора являются бо́льшая востребованность относительно фронтенда, бо́льшее влияние на непосредственно видимый результат относительно бэкенда, возможность портативного переноса игровых механик и локальная совместимость с текущими запросами Программеры. Но главное – это наша вера в то, что за мобильной разработкой будущее.",
            tasks = listOf(
                Task(
                    text = "Пройти короткий первичный курс по Android разработке на Kotlin",
                    isCompleted = true
                ),
                Task(text = "Пройти Android Basics with Compose"),
                Task(text = "Создать приложение для лингвистической симуляции"),
                Task(text = "Создать приложение для диплома Ильи"),
                Task(
                    text = "Препарировать весь Play Market",
                    isAdditional = true
                )
            ),
            isCompleted = false,
            dateOfCompletion = null
        ),
        Quest(
            title = "Эпоха Поволарпа",
            difficulty = Difficulty.HIGH,
            description = "Настала пора воплотить игровые механики в их изначально задуманном виде. Цифровая метавселенная, её эстетичные интерфейсы и сплетение всего многообразия внедрённых элементов. Разработка полноценной игры может занять годы и привести к самым разным результатам.",
            tasks = listOf(
                Task(text = "Закончить разработку MV I"),
                Task(text = "Прописать файл модификаторов"),
                Task(text = "Воплотить любую механику в виде приложения"),
            ),
            isCompleted = false,
            dateOfCompletion = null
        ),
        Quest(
            title = "Планета Спорт",
            difficulty = Difficulty.HIGH,
            description = "Новая вихревая попытка обрести состояние апполоничного хорни-идеала зародилась в желании выйти из мучительных состояний тяжести раннеиюльских толстопов. Величие самоконтроля эпохи Таблицы давно ушло, а последние подступы к взятию себя в руки, предпринимаемые за последние полгода, оборачивались провалом то из-за затяжной болезни, то из-за недостатка административного ресурса для составления полноценной программы. Наша цель сейчас: превзойти табличных пов в итоговых результатах, сохранять дисциплину на протяжении ближайшего полугодия и достичь видимого невооружённым взглядом эффекта. Ну мы уже не лопнум.",
            tasks = listOf(
                Task(text = "Провести переговоры с мамой об ограничении количества соблазнительных сладостей дома"),
                Task(
                    text = "Купить весы в комнату",
                    isCompleted = true
                ),
                Task(text = "Составить программу упражнений",),
                Task(text = "Активировать протокол «Планета Спорт»"),
                Task(text = "Успешно продержаться первый месяц"),
                Task(text = "Соматические традиции: 20.0 и более"),
            ),
            isCompleted = false,
            dateOfCompletion = null,
            type = QuestType.Additional
        )
    )
}