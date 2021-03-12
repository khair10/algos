package ready

internal class KMP {

    fun search(pattern: String, text: String): ArrayList<Int> {
        val patternLen = pattern.length
        val textLen = text.length
        val result = ArrayList<Int>()
        val prefix = prefixFunc(pattern, patternLen)
        var j = 0
        var i = 0
        while (i < textLen) {
            // Если символы равны, шагаем дальше по тексту и паттерну
            if (text[i] == pattern[j]) {
                j++
                i++
            }
            // Проверяем длину совпадения на соответствие длине паттерна и "сдвигаем" паттерн дальше
            if (j == patternLen) {
                result.add(i - j)
                j = prefix[j - 1]
            } else if (i < textLen && pattern[j] != text[i]) { // Иначе если символы отличаеются и мы не долшли до конца текста
                if (j != 0) {
                    // "сдвигаем" слово если можно
                    j = prefix[j - 1]
                } else {
                    // если сдвигать нельзя, пробуем идти дальше по тексту
                    i++
                }
            }
        }
        return result
    }

    private fun prefixFunc(pattern: String, patternLen: Int): IntArray {
        val prefix = IntArray(patternLen)
        var j = 0
        var i = 1
        while (i < patternLen) {
            if (pattern[i] == pattern[j]) {
                j++
                prefix[i] = j
                i++
            } else {
                if (j > 0) {
                    j = prefix[j - 1]
                } else {
                    i++
                }
            }
        }
        return prefix
    }
}