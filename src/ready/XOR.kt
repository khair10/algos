package ready

fun xor(first: Int, second: Int) {
    var a = first
    var b = second
    println("A = $a, B = $b")
    // получаем сначала XOR, который говорит нам о различиях двух чисел, там где единицы, там числа различаются
    a = a xor b
    // после XOR полученного "различия" и одного из чисел мы получаем второе, так мы сначала получаем число "а" из "b"
    b = a xor b
    // а затем получаем "b" из "a"
    a = a xor b
    println("Now A = $a, B = $b")
}