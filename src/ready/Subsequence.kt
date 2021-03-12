package ready

import java.util.*

/*
    Наибольшая общая подпоследовательность
    На вход подаются две строки
 */
fun longestCommonSubsequence(first: String, second: String): String {
    val firstSequence = first.toCharArray().toTypedArray()
    val secondSequence = second.toCharArray().toTypedArray()
    val firstLength: Int = firstSequence.size // m
    val secondLength: Int = secondSequence.size // n
    // Матрица для вычислений
    val resultMatrix = Array(firstLength + 1) {
        IntArray(secondLength + 1)
    }
    // Проходим по всем символам двух строк
    for (i in 1..firstLength) {
        for (j in 1..secondLength) {
            // Сравниваем символы в двух строках по индексам
            if (firstSequence[i - 1] == secondSequence[j - 1]) {
                // Если символы равны, мы предполагаем что общая последовательность увеличивается на 1 от прошлой
                resultMatrix[i][j] = resultMatrix[i - 1][j - 1] + 1
            } else {
                // Иначе мы смотрим на большую общую подпоследовательность среди прошлых, в которых изменилась лишь
                // одна буква в одной из строк
                resultMatrix[i][j] = Integer.max(resultMatrix[i - 1][j], resultMatrix[i][j - 1])
            }
        }
    }

    // Получаем результирующую подпоследовательность по матрице решения
    var result = ""
    var i = firstLength
    var j = secondLength
    // Пока какой либо индекс не равен 0, выполняем, начиная с конца таблицы
    while (i != 0 && j != 0) {
        when {
            // Если последние символы строк равны
            firstSequence[i - 1] == secondSequence[j - 1] -> {
                // Мы добавляем букву в результат и уменьшаем индексы на единицу
                result = firstSequence[i - 1].toString() + result
                i--
                j--
            }
            // Проверяем максимальные длины подпоследовательностей на прошлом шаге и вычитаем соответствующий индекс,
            // чтобы идти в нужном направлении обратно
            resultMatrix[i - 1][j] >= resultMatrix[i][j - 1] -> {
                i--
            }
            else -> {
                j--
            }
        }
    }
    return result
}

// Чтобы найти наибольшую возрастающую подпоследовательность нужно всего лишь найти наибольшую общую последовательность
// для строки и отсортированной по буквам этой же строки по возрастанию
fun longestIncreasingSubsequence(string: String): String {
    val temp = string.toCharArray().copyOf()
    Arrays.sort(temp)
    return longestCommonSubsequence(String(temp), string)
}