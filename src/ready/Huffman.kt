package ready

import java.util.*
import kotlin.collections.HashMap

@ExperimentalStdlibApi
class Huffman() {

    lateinit var treeRoot: Node

    data class Node(val char: Char, val freq: Int, val left: Node? = null, val right: Node? = null) : Comparable<Node> {

        fun isLeaf() = left == null && right == null

        override fun compareTo(other: Node) = freq.compareTo(other.freq)
    }

    fun compress(input: String): ByteArray {
        val freqDict = makeFreqDict(input)
        val nodesQueue = makeNodesQueue(freqDict)
        treeRoot = makeCodeTree(nodesQueue)
        val codeTable = makeCodeTable(treeRoot)
        val compressedUBytes = compressToUBytes(input, codeTable)
        return compressedUBytes.toByteArray()
    }

    fun decode(codeBytes: ByteArray): String {
        val binaryCodeString = getBinaryCodeString(codeBytes, codeBytes.last())
        return decodeBinary(binaryCodeString)
    }

    private fun makeFreqDict(input: String): Map<Char, Int> {
        val freqDict = HashMap<Char, Int>()
        for (char in input) {
            freqDict[char] = (freqDict[char] ?: 0) + 1
        }
        return freqDict
    }

    private fun makeNodesQueue(freqDict: Map<Char, Int>): PriorityQueue<Node> {
        val nodesQueue = PriorityQueue<Node>()
        for (entry in freqDict) {
            nodesQueue.add(Node(entry.key, entry.value))
        }
        return nodesQueue
    }

    private fun makeCodeTree(nodesQueue: PriorityQueue<Node>): Node {
        while (nodesQueue.size > 1) {
            val node1 = nodesQueue.poll()
            val node2 = nodesQueue.poll()
            val node = Node(Char.MIN_VALUE, node1.freq + node2.freq, node1, node2)
            nodesQueue.add(node)
        }
        return nodesQueue.poll()
    }

    private fun makeCodeTable(treeRoot: Node): Map<Char, String> {
        val codeTable = HashMap<Char, String>()
        addCodeFromRoot(treeRoot, codeTable)
        return codeTable
    }

    private fun compressToUBytes(input: String, codeTable: Map<Char, String>): UByteArray {
        val codeStringBuilder = makeRawBinaryCodeString(input, codeTable)
        val leadingZerosCount = addLeadingZeros(codeStringBuilder)
        return makeByteArrayFromBinaryCodeString(codeStringBuilder.toString(), leadingZerosCount)
    }

    private fun getBinaryCodeString(codeBytes: ByteArray, zeroCount: Byte): String {
        val codeStringBuilder = StringBuilder()
        val codeBytesWithoutZeroCount = codeBytes.copyOfRange(0, codeBytes.lastIndex)
        for (byte in codeBytesWithoutZeroCount) {
            val leadingZerosString = if(byte == 0.toByte()) {
                "0000000"
            } else {
                getLeadingZerosString(byte)
            }
            val tempCode = byteToBinaryString(byte)
            codeStringBuilder.append(leadingZerosString)
            codeStringBuilder.append(tempCode)
        }
        return codeStringBuilder.toString().substring(0 + zeroCount, codeStringBuilder.length)
    }

    private fun addCodeFromRoot(treeRoot: Node, codeTable: HashMap<Char, String>) {
        addCode(treeRoot.left!!, codeTable, "0")
        addCode(treeRoot.right!!, codeTable, "1")
    }

    private fun addCode(node: Node, codeTable: HashMap<Char, String>, code: String) {
        if (!node.isLeaf()) {
            addCode(node.left!!, codeTable, code + "0")
            addCode(node.right!!, codeTable, code + "1")
        } else {
            codeTable[node.char] = code
        }
    }

    private fun makeRawBinaryCodeString(
        input: String,
        codeTable: Map<Char, String>
    ): StringBuilder {
        val codeString = StringBuilder()
        for (char in input) {
            codeString.append(codeTable[char])
        }
        return codeString
    }

    private fun addLeadingZeros(codeString: StringBuilder): Int {
        val zeroCount = 8 - codeString.length % 8
        for (i in 0 until zeroCount) {
            codeString.insert(0, '0')
        }
        return zeroCount
    }

    private fun makeByteArrayFromBinaryCodeString(
        code: String,
        leadingZerosCount: Int
    ): UByteArray {
        val textLen = code.length / 8 + 1
        var bytes = UByteArray(textLen)
        var j = 0
        for (i in code.indices step 8) {
            bytes[j++] = code.substring(i, i + 8).toUByte(2)
        }
        bytes[textLen - 1] = leadingZerosCount.toUByte()
        return bytes
    }

    private fun decodeBinary(codeString: String): String {
        var currentNode = treeRoot
        var text = StringBuilder()
        for (char in codeString) {
            currentNode = if (char == '1') {
                currentNode.right!!
            } else {
                currentNode.left!!
            }
            if (currentNode.isLeaf()) {
                text.append(currentNode.char)
                currentNode = treeRoot
            }
        }
        return text.toString()
    }

    private fun getLeadingZerosString(byte: Byte): String {
        var leadingZerosString = ""
        for (i in 0 until byte.toUByte().countLeadingZeroBits()) {
            leadingZerosString += '0'
        }
        return leadingZerosString
    }

    private fun byteToBinaryString(byte: Byte): String {
        return byte.toUByte().toString(2)
    }
}
