package ready

import java.math.BigInteger


fun algo(n: BigInteger, k: Int): Boolean {
    if (n == BigInteger.valueOf(2)|| n == BigInteger.valueOf(3)) return true

    // если n < 2 или n четное - возвращаем false
    if (n < BigInteger.TWO || n % BigInteger.valueOf(2) == BigInteger.ZERO) return false

    // представим n − 1 в виде (2^s)·t, где t нечётно, это можно сделать последовательным делением n - 1 на 2
    var t = n - BigInteger.ONE
    var s = 0
    while (t % BigInteger.TWO == BigInteger.ZERO) {
        t /= BigInteger.TWO
        s += 1
    }
    for (i in 0 until k) {
        var a: BigInteger = randomBigInteger(BigInteger.TWO, n - BigInteger.ONE)
        var x = a.modPow(t, n)
        if (x == BigInteger.ONE || x == n - BigInteger.ONE) {
            continue
        }
        for (j in 0 until s - 1) {
            x = x * x % n
            if (x == BigInteger.ONE) {
                return false
            }
            if (x == n - BigInteger.ONE){
                break
            }
        }
        if (x != n - BigInteger.ONE) {
            return false
        }
    }
    return true
}

fun randomBigInteger(minLimit: BigInteger, maxLimit: BigInteger): BigInteger{
    val bigInteger = maxLimit.subtract(minLimit)
    val randNum = java.util.Random()
    val len = maxLimit.bitLength()
    var res = BigInteger(len, randNum)
    if (res < minLimit) res = res.add(minLimit)
    if (res >= bigInteger) res = res.mod(bigInteger).add(minLimit)
    return res
}