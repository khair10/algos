package ready

object LZ78 {

    val dict = HashMap<String, Int>()

    /*
        Кодирование текста с помощью LZ78
     */
    fun encode(text: String): String {
        var prefix = ""
        var result = ""
        // Для каждого символа из исходного текста
        for (char in text) {
            var word = prefix + char
            // Мы проверяем существует ли уже в словаре связка "префикс + символ"
            if (word in dict) {
                // Если связка "префикс + символ" уже находится в словаре, мы обновляем префикс
                prefix = word
            } else {
                // Если связки "префикс + символ" в словаре нет, мы добавляем в результирующий код индекс ближайшего
                // слова в словаре и последний считанный символ, обнуляем префикс
                result = result + (dict[prefix] ?: "0") + char
                dict[word] = dict.size + 1
                prefix = ""
            }
        }
        return result
    }

    fun decode(encoded: String): String {
        val regex = "\\d+\\w".toRegex()
        var match = regex.find(encoded)
        var result = ArrayList<String>()
        // Мы ищем все пары (цифра-буква) в исходящей строке
        while (match != null) {
            // Для каждой пары мы получаем индекс ближайшего слова и символ
            val entry = match.value
            val index = entry.substring(0, entry.lastIndex).toInt()
            // Если индекс равен нулю, мы добавляем символ в список результатов, иначе мы берем из списка ближайшее
            // слово, добавляем к нему символ, новое слово добавляем в список результатов
            if(index == 0){
                result.add(entry.last().toString())
            } else {
                result.add(result[index - 1] + entry.last().toString())
            }
            match = match.next()
        }
        // Объединяем список слов в строку
        return result.joinToString("")
    }
}