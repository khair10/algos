import ready.*
import ready.KMP
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.log2
import kotlin.math.round

@ExperimentalStdlibApi
fun main(){
    // Фибоначи
    //println(fibonacci(500))
    //println(fibonacciWithTemp(500))

    // XOR
    //xor(10, 20)

    // LZ78
    //lz78()

    // Наибольшая общая подпоследовательность и наибольшая возрастающая подпоследовательность
    //println(longestCommonSubsequence("abcbdab", "bdcaba")) // bcba
    //println(longestIncreasingSubsequence("124315"))

    // Джарвис
    jarvis()

    // Грехем
    graham()

    // Вакцина(конвейер)
    //vaccine()

    // Алгоритм проверки числа на простоту
    //millerRabin()

    // Алгоритм поиска подстроки Кнута-Морриса-Пратта
    //kmp()

    // Хаффман
    //huffman()

    // Шеннон-Фано
    //shannonFano()
}

private fun lz78(){
    val text = "abacababacabcabcde"
    //Ответ: 0a0b1c1b4a0c4c7d0e
    val encoded = LZ78.encode(text)
    println(encoded)
    val dictionary = LZ78.dict
    println(dictionary)
    println(LZ78.decode(encoded))
}

private fun jarvis(){
    val points = Array(300) {
        IntArray(2)
    }
    for (i in 0..293) {
        points[i][0] = i / 50 + 20
        points[i][1] = i % 50 + 10
    }
    points[294][0] = 0
    points[294][1] = 0
    points[295][0] = 100
    points[295][1] = 10
    points[296][0] = 50
    points[296][1] = 5
    points[297][0] = 70
    points[297][1] = 100
    points[298][0] = 40
    points[298][1] = 100
    points[299][0] = 10
    points[299][1] = 80
    val jarvis = Jarvis()
    println(jarvis.jarvis(points))
}

private fun graham(){
    val points = Array(300) {
        IntArray(2)
    }
    for (i in 0..293) {
        points[i][0] = i / 50 + 20
        points[i][1] = i % 50 + 10
    }
    points[294][0] = 0
    points[294][1] = 0
    points[295][0] = 100
    points[295][1] = 10
    points[296][0] = 50
    points[296][1] = 2
    points[297][0] = 70
    points[297][1] = 100
    points[298][0] = 40
    points[298][1] = 100
    points[299][0] = 10
    points[299][1] = 80
    val graham = Graham()
    println(graham.graham(points))
}

private fun vaccine() {
    val vaccine = Vaccine()
    /*
        val workTime: Array<IntArray> = arrayOf(
        intArrayOf(8, 10, 4, 5, 2, 3),
        intArrayOf(8, 6, 7, 1, 6, 1)
        )
        val transportTime: Array<IntArray> = arrayOf(
            intArrayOf(3, 4, 2, 2, 5),
            intArrayOf(1, 2, 3, 3, 1)
        )
        val startupTime: IntArray = intArrayOf(3, 5)
        val finishTime: IntArray = intArrayOf(4, 3)
     */
    val workTime: Array<IntArray> = arrayOf(
        intArrayOf(8, 10, 4, 5, 2, 3),
        intArrayOf(8, 6, 7, 1, 6, 1),
        intArrayOf(10, 10, 10, 1, 1, 10)
    )
    val transportTime: Array<IntArray> = arrayOf(
        intArrayOf( 3,  4,  2,  2,  5),
        intArrayOf(10, 10, 10, 10, 10),
        intArrayOf( 1,  2,  3,  3, 1 ),
        intArrayOf(10, 10, 10, 10, 2 ),
        intArrayOf(10, 10,  1, 10, 10),
        intArrayOf(10, 10, 10, 10, 10)
    )
    val startupTime: IntArray = intArrayOf(3, 5, 10)
    val finishTime: IntArray = intArrayOf(4, 3, 4)
    val shortestWay = vaccine.fastestWay(workTime, transportTime, startupTime, finishTime)
    println(shortestWay?.joinToString())
    println(vaccine.shortestWayLength)
}

private fun millerRabin() {
    val tests = arrayListOf<Int>(1, 2, 3, 353, 355, 2016, 2017, 65537, 65538, 3559, 3553)
    for (test in tests) {
        println("$test = ${algo(
            BigInteger.valueOf(test.toLong()),
            round(log2(test.toFloat())).toInt()
        )}")
    }
}

private fun kmp() {
    val txt = "ABABDABACDABABCABAB"
    val pat = "ABAB"
    println(KMP().search(pat, txt).joinToString())
}

@ExperimentalStdlibApi
fun huffman() {
    val huffman = Huffman()
    val fileName = "pult"
    val text = Files.readAllBytes(Paths.get("C:\\Users\\khair\\Desktop\\asd\\${fileName}.txt"))
    val code = huffman.compress(String(text))
    Files.write(Paths.get("C:\\Users\\khair\\Desktop\\asd\\${fileName}.bin"), code)
    val decodedBytes = Files.readAllBytes(Paths.get("C:\\Users\\khair\\Desktop\\asd\\${fileName}.bin"))
    val decoded = huffman.decode(decodedBytes)
    Files.writeString(Paths.get("C:\\Users\\khair\\Desktop\\asd\\${fileName}_copy.txt"), decoded)
}

fun shannonFano() {
    var shannonFano = ShannonFano()
    val fileName = "WarSF"
    val text = Files.readAllBytes(Paths.get("C:\\Users\\khair\\Desktop\\asd\\${fileName}.txt"))
    val code = shannonFano.compress(String(text))
    Files.write(Paths.get("C:\\Users\\khair\\Desktop\\asd\\${fileName}.bin"), code)
    val decodedBytes = Files.readAllBytes(Paths.get("C:\\Users\\khair\\Desktop\\asd\\${fileName}.bin"))
    val decoded = shannonFano.decode(decodedBytes)
    Files.writeString(Paths.get("C:\\Users\\khair\\Desktop\\asd\\${fileName}_copy.txt"), decoded)
}