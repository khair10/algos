package ready

import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@ExperimentalStdlibApi
class ShannonFano {

    lateinit var treeRoot: Node

    data class Node(var char: Char, val freq: Double, var left: Node? = null, var right: Node? = null) : Comparable<Node> {

        fun isLeaf() = left == null && right == null

        override fun compareTo(other: Node) = other.freq.compareTo(freq)
    }

    fun compress(input: String): ByteArray{
        val freqDict = createFreqDict(input)
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

    private fun createFreqDict(input: String): Map<Char, Double> {
        val dictionary = HashMap<Char, Double>()
        var count = input.length
        for (char in input) {
            dictionary[char] = (dictionary[char] ?: 0.0) + 1.0
        }
        normalizeDict(dictionary, count)
        return dictionary
    }

    private fun makeNodesQueue(freqDict: Map<Char, Double>): ArrayList<Node> {
        val nodesQueue = ArrayList<Node>(freqDict.size)
        var addedCount = 0
        for (entry in freqDict) {
            var isInserted = false
            for (i in 0 until addedCount) {
                if(nodesQueue[i].freq < entry.value) {
                    nodesQueue.add(i, Node(entry.key, entry.value))
                    isInserted = true
                    break
                }
            }
            if(!isInserted) {
                nodesQueue.add(Node(entry.key, entry.value))
            }
            addedCount++
        }
        return nodesQueue
    }

    private fun makeCodeTree(nodesQueue: ArrayList<Node>): Node {
        val rootNode = Node(Char.MIN_VALUE, 1.0)
        val leftArray = ArrayList<Node>()
        val rightArray = ArrayList<Node>()
        var leftSum = 0.0
        var rightSum = rootNode.freq
        var index = 0
        while (leftSum < rootNode.freq / 2 - 0.1) {
            leftSum += nodesQueue[index].freq
            leftArray.add(nodesQueue[index])
            rightSum -= nodesQueue[index].freq
            index++
        }
        for (i in index until nodesQueue.size) {
            rightArray.add(nodesQueue[i])
        }
        val left = Node(Char.MIN_VALUE, leftSum)
        val right = Node(Char.MIN_VALUE, rightSum)
        rootNode.left = left
        rootNode.right = right
        makeCodeSubtrees(left, leftArray)
        makeCodeSubtrees(right, rightArray)
        return rootNode
    }

    private fun makeCodeSubtrees(rootNode: Node, nodes: ArrayList<Node>) {
        if(nodes.size == 1) {
            rootNode.char = nodes[0].char
        }else if(nodes.size == 2) {
            rootNode.left = nodes[0]
            rootNode.right = nodes[1]
        } else {
            val leftArray = ArrayList<Node>()
            val rightArray = ArrayList<Node>()

            var leftSum = 0.0
            var rightSum = rootNode.freq
            var index = 0
            while (leftSum < rootNode.freq / 2) {
                leftSum += nodes[index].freq
                leftArray.add(nodes[index])
                rightSum -= nodes[index].freq
                index++
            }
            for (i in index until nodes.size) {
                rightArray.add(nodes[i])
            }
            val left = Node(Char.MIN_VALUE, leftSum)
            val right = Node(Char.MIN_VALUE, rightSum)
            rootNode.left = left
            rootNode.right = right
            makeCodeSubtrees(left, leftArray)
            makeCodeSubtrees(right, rightArray)
        }
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

    private fun normalizeDict(dictionary: HashMap<Char, Double>, count: Int) {
        for (key in dictionary.keys) {
            dictionary[key] = dictionary[key]!!.toDouble() / count
        }
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
    }private fun decodeBinary(codeString: String): String {
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