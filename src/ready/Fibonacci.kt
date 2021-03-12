package ready

/*
    Вычисление числа фибоначи по индексу с помощью дополнительной переменной
 */
fun fibonacciWithTemp(index: Int): Int {
    // 0, 1, 1, 2, 3, 5
    var lowerNum = 0
    var higherNum = 1
    var temp: Int
    for (i in 3..index) {
        temp = higherNum
        higherNum += lowerNum
        lowerNum = temp
    }
    return higherNum
}

/*
    Вычисление числа фибоначи по индексу, запоминая только 2 числа
 */
fun fibonacci(index: Int): Int {
    // 0, 1, 1, 2, 3, 5
    var lowerNum = 0
    var higherNum = 1
    for (i in 3..index) {
        higherNum += lowerNum
        lowerNum = higherNum - lowerNum
    }
    return higherNum
}
