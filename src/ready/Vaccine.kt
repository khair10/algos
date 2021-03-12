package ready

class Vaccine {

    var shortestWayLength = 0

    /**
     * Метод принимает на вход время сборки на каждом отксеке каждого конвейера, время транспортировки с конвейера на другой
     * время начала, время конца.
     * @param workTime время работы отсеков конвейеров
     * @param transportTime время транспортировки между коневейерами
     * @param startupTime время начала работ
     * @param finishTime время окончания работ
     * @return Массив в котором на каждое состояние указывается номер конвейера
     */
    fun fastestWay(
        workTime: Array<IntArray>,
        transportTime: Array<IntArray>,
        startupTime: IntArray,
        finishTime: IntArray
    ): IntArray? {
        val conveyorLength: Int = workTime[0].size
        shortestWayLength = Int.MAX_VALUE
        val fastestWays =
            Array(startupTime.size) { IntArray(conveyorLength) } // время самых быстрых путей
        val conveyorPathIndexes =
            Array(startupTime.size) { IntArray(conveyorLength) } // массив хранит предыдущий конвейер
        val conveyorsPath = IntArray(conveyorLength) // результат
        var bestConveyorIndex = 0 // индекс конечного конвейера при самом быстром пути

        // вычисляем стартовые значения
        for (i in startupTime.indices) {
            fastestWays[i][0] = startupTime[i] + workTime[i][0]
        }

        // проходим по каждому и смотрим на предыдущие состояния, с какого быстрее прийти в нынешнее
        for (i in 1 until conveyorLength) {
            var index = 0
            for (k in startupTime.indices) { // фиксируем конвейер и для него ищем самый мелкий путь
                var answer = Int.MAX_VALUE
                var conveyorIndex = 0
                for (j in startupTime.indices) { // проходим по всем путям
                    var tempAnswer: Int
                    var tempConveyorIndex: Int
                    if (k == j) {
                        // Если конвейер находится в той же стране, то мы не учитываем время на
                        // траспортировку между странами
                        tempAnswer = fastestWays[k][i - 1] + workTime[k][i]
                        tempConveyorIndex = j
                    } else {
                        // Иначе берем в рнасчет время на траспортировку между странами
                        tempAnswer =
                            fastestWays[j][i - 1] + transportTime[index][i - 1] + workTime[k][i]
                        tempConveyorIndex = j
                        index++
                    }
                    if (tempAnswer < answer) {
                        answer = tempAnswer
                        conveyorIndex = tempConveyorIndex
                    }
                }
                // Для каждого отмечаем быстрейший путь и с какого конвейера он был достигнут
                fastestWays[k][i] = answer
                conveyorPathIndexes[k][i] = conveyorIndex
            }
        }

        // сморим в каком конвейере самый быстрый путь
        for (i in finishTime.indices) {
            val answer = finishTime[i] + fastestWays[i][conveyorLength - 1]
            if (answer < shortestWayLength) {
                shortestWayLength = answer
                bestConveyorIndex = i
            }
        }

        // вычисляем этот путь, проходим обратно по матрице путей
        var tempIndex = bestConveyorIndex
        for (i in conveyorLength - 1 downTo 0) {
            conveyorsPath[i] = tempIndex
            tempIndex = conveyorPathIndexes[tempIndex][i]
        }
        return conveyorsPath
    }
}