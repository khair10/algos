package ready

import java.util.*
import kotlin.math.sqrt

class Graham {

    /*
        Находит выпуклую оболочку для множества точек
     */
    fun graham(points: Array<IntArray>): ArrayList<Int> {
        // количество точек
        val length = points.size
        // заводим массив для хранения индексов точек, чтобы не трогать основной массив точек
        var indexes = IntArray(length)
        // заполняем массив для хранения индексов точек индексами от 0 до length - 1
        for (i in 0 until length) {
            indexes[i] = i
        }

        // ищем самую левую точку, сравнивая координату x
        for (i in 1 until length) {
            if (points[indexes[i]][0] < points[indexes[0]][0]) {
                // если точка левее действующей точки, то меняем местами индексы в массиве индексов
                val temp = indexes[0]
                indexes[0] = indexes[i]
                indexes[i] = temp
            }
        }

        // сортируем массив индексов по углу поворота против часовой стрелки, не трогая 0-ой элемент
        indexes = sort(points, indexes)

        // создаем стек для хранения индексов вершин, которые войдут в минимальную оболочку
        val stack = ArrayList<Int>()
        // заполняем стек первыми двумя вершинами
        stack.add(indexes[0])
        stack.add(indexes[1])
        // переменная для отслеживания размера стека
        var stackSize = 2
        // проходим по каждой точке и добавляем её в стек, но перед этим
        for (i in 2 until length) {
            // проверяем с какой стороны находится рассматриваемая точка от прямой, которая проходит через две последние
            // точки из стека, пока точка находится правее повторяем итерацию
            while (vectorProduct(
                    points[stack[stackSize - 2]],
                    points[stack[stackSize - 1]],
                    points[indexes[i]]
                ) < 0
            ) {
                // убираем последний элемент из стека, уменьшаем размер стека на 1
                stack.removeAt(stackSize - 1)
                stackSize--
            }
            stack.add(indexes[i])
            stackSize++
        }
        // возвращаем минимальную оболочку
        return stack
    }

    /*
     * Смотрим где лежит точка next от anchor-current прямой
     * Если > 0, next слева
     * Если == 0 все точки лежат на одной прямой
     * Если < 0  next правее
     */
    private fun vectorProduct(anchor: IntArray, current: IntArray, next: IntArray): Int {
        return (current[0] - anchor[0]) * (next[1] - anchor[1]) - (next[0] - anchor[0]) * (current[1] - anchor[1])
    }

    private fun sort(points: Array<IntArray>, indexes: IntArray): IntArray {
        val length = points.size
        for (i in 2 until length) {
            var j = i
            while (j > 1 && vectorProduct(
                    points[indexes[0]],
                    points[indexes[j - 1]],
                    points[indexes[j]]
                ) < 0
            ) {
                val temp = indexes[j - 1]
                indexes[j - 1] = indexes[j]
                indexes[j] = temp
                j--
            }
        }
        return indexes
    }
}