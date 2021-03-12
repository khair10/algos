package ready

import java.util.*

class Jarvis {

    /*
        Находит выпуклую оболочку для множества точек
     */
    fun jarvis(points: Array<IntArray>): ArrayList<Int> {
        val pointCounts = points.size
        var point = points[0]
        val checked = BooleanArray(pointCounts)
        var leftIndex = 0

        // Ищем самую левую точку и её индекс, сравниваем координаты X
        for (i in 1 until pointCounts) {
            if (point[0] > points[i][0]) {
                point = points[i]
                leftIndex = i
            }
        }
        // добавляем индекс в список
        val pointIndexes = ArrayList<Int>()
        pointIndexes.add(leftIndex)
        // устанавливаем стартовую точку как самую левую точку
        var currentPoint = point
        var currentIndex = 0
        var collinear = ArrayList<Int>()
        // делаем пока не вернёмся в стартовую точку
        do {
            //начинаем с 0-ой точки из списка
            var next = points[0]
            var nextIndex = 0
            // проходим для всех точек и ищем самую правую по повороту точку
            for (i in 1 until pointCounts) {
                // пропускаем уже добавленные в оболочку точки и саму точку
                if (isPointsEqual(currentPoint, points[i]) || checked[i]) {
                    continue
                }
                // смотрим где лежит точка относительно вектора current, next
                val vectorProduct = vectorProduct(currentPoint, next, points[i])
                // если точка правее, то переопределяем next
                if (vectorProduct < 0) {
                    next = points[i]
                    nextIndex = i
                    collinear = ArrayList()
                    //если точки на одной прямой, то смотрим кто ближе и кладем в стек точку которая ближе
                } else if (vectorProduct == 0) {
                    // Иначе проверяем на коллинеарность, выбираем самую отдаленную точку
                    if (distance(currentPoint, next, points[i]) < 0) {
                        collinear.add(nextIndex)
                        next = points[i]
                        nextIndex = i
                    } else {
                        collinear.add(i)
                    }
                }
            }
            // добавляем коллинеарные точки в список оболочки и помечаем их
            for (check in collinear) {
                pointIndexes.add(check)
                checked[check] = true
            }
            checked[nextIndex] = true
            pointIndexes.add(nextIndex)
            currentIndex = pointIndexes.size - 1
            currentPoint = points[nextIndex]
        } while (pointIndexes[0] != pointIndexes[currentIndex])
        pointIndexes.removeAt(currentIndex)
        return pointIndexes
    }

    private fun isPointsEqual(a: IntArray, b: IntArray): Boolean {
        return a[0] == b[0] && a[1] == b[1]
    }

    /*
     * Сравниваем длины векторов anchor-current и anchor-next
     * Если < 0 то anchor-current меньше
     * Если > 0 то anchor-next меньше
     */
    private fun distance(anchor: IntArray, current: IntArray, next: IntArray): Int {
        val y1 = current[1] - anchor[1]
        val y2 = next[1] - anchor[1]
        val x1 = current[0] - anchor[0]
        val x2 = next[0] - anchor[0]
        return Integer.compare(y1 * y1 + x1 * x1, y2 * y2 + x2 * x2)
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
}