package kotlister

import com.marcinmoskala.math.combinations
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import com.xenomachina.argparser.mainBody
import org.apache.commons.collections4.iterators.PermutationIterator
import java.io.BufferedWriter
import java.io.File
import kotlin.system.exitProcess


fun BufferedWriter.writeLn(line: String) {
    this.write(line)
    this.newLine()
}

class MyArgs(parser: ArgParser) {
    val fileName by parser.storing(
            "-i", "--input",
            help = "input file path")

    val permutations by parser.storing(
            "-p", "--perm",
            help = "max number of words to be combined on the same line") { toInt() }

    val minL by parser.storing(
            "--min",
            help = "minimum generated password length") { toInt() }

    val maxL by parser.storing(
            "--max",
            help = "maximum generated password length") { toInt() }

    val append by parser.storing(
            "--append",
            help = "maximum generated password length").default("")

    val prepend by parser.storing(
            "--prepend",
            help = "maximum generated password length").default("")

    val leet by parser.flagging("-l", "--leet",
            help = "enable leet mutagen")

    val upper by parser.flagging("-u", "--upper",
            help = "enable uppercase mutagen")

    val capitalize by parser.flagging("-c", "--capitalize",
            help = "enable capitalize mutagen")
}

fun toLeet(input_string: String): String {
    // Returns the leeted version of a string

    val sb = StringBuilder()
    for (character in input_string) {
        when (character) {
            'o', 'O' -> sb.append('0')
            'a', 'A' -> sb.append('4')
            'e', 'E' -> sb.append('3')
            'i', 'I' -> sb.append('1')
            's', 'S' -> sb.append('5')
            else -> sb.append(character)
        }
    }
    return sb.toString()
}


fun readInputFile(file_path: String, capitalize: Boolean, upper: Boolean): HashSet<String> {
    // Reading file and creating a set with the contained words

    try {
        val inputSet: HashSet<String> = HashSet(File(file_path).readLines())
        // Apply capitalize and/or upper mutagens if needed
        if (capitalize || upper) {
            val modifiedSet: HashSet<String> = HashSet()
            modifiedSet.addAll(inputSet)
            for (word: String in inputSet) {
                // Skip empty lines and lines filled with whitespaces or tabs
                if (!word.isBlank()) {
                    if (capitalize) {
                        modifiedSet.add(word.capitalize())
                    }
                    if (upper) {
                        modifiedSet.add(word.toUpperCase())
                    }
                }
            }
            return modifiedSet
        } else {
            return inputSet
        }
    } catch (e: java.io.FileNotFoundException) {
        println("Input file \"$file_path\" not found!")
        exitProcess(status = 1)
    }
}

fun getFilteredCombinations(input: HashSet<String>, size: Int): List<Set<String>> {
    val combinations: HashSet<Set<String>> = HashSet()
    for (x in 1..size) {
        combinations.addAll(input.combinations(combinationSize = x))
    }
    // Filter combinations to remove presence of same word with different Case by checking
    // for original set size vs new set size with all words lowerCase (no repetitions)
    return combinations.filter { combination ->
        val check = combination.map { it.toLowerCase() }
        return@filter combination.size == check.toHashSet().size
    }
}

fun main(args: Array<String>) = mainBody {
    ArgParser(args).parseInto(::MyArgs).run {
        val inputSet: HashSet<String> = readInputFile(file_path = fileName, capitalize, upper)
        // Generate combinations with N components from set
        val filteredCombinations: List<Set<String>> = getFilteredCombinations(inputSet, permutations)

        File("output.txt").bufferedWriter().use { out ->
            for (combination in filteredCombinations) {

                // Check that total length of combination (as String) isn <= max
                if (combination.joinToString(separator = "").length <= maxL) {

                    for (permutation in PermutationIterator(combination)) {
                        val s: String = permutation.joinToString(separator = "")
                        var leetS = ""
                        var temp: String

                        if (leet) {
                            leetS = toLeet(s)
                            if (minL <= leetS.length) {
                                out.writeLn(leetS)
                            }
                        }

                        if (append.isNotEmpty()) {
                            temp = s.plus(append)
                            if (temp.length in minL..maxL) {
                                out.writeLn(temp)
                                if (leet) {
                                    out.writeLn(leetS.plus(append))
                                }
                            }
                        }

                        if (prepend.isNotEmpty()) {
                            temp = prepend.plus(s)
                            if (temp.length in minL..maxL) {
                                out.writeLn(temp)
                                if (leet) {
                                    out.writeLn(prepend.plus(leetS))
                                }
                            }

                            if (append.isNotEmpty()) {
                                temp = prepend.plus(s).plus(append)
                                if (temp.length in minL..maxL) {
                                    out.writeLn(prepend.plus(s).plus(append))
                                    if (leet) {
                                        out.writeLn(prepend.plus(leetS).plus(append))
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}
