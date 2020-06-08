package kotlister

import com.marcinmoskala.math.combinations
import java.io.File
import kotlin.system.exitProcess
import org.apache.commons.collections4.iterators.PermutationIterator

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

fun main() {
    // TODO: take inputs from terminal parameters
    val capitalize = true
    val upper = true
    val leet = true
    val size = 3
    val append = "2020!"
    val prepend = "prima"

    val inputSet: HashSet<String> = readInputFile(file_path = "wordlist.txt", capitalize, upper)
    // Generate combinations with N components from set
    val filteredCombinations: List<Set<String>> = getFilteredCombinations(inputSet, size)
    for (combination in filteredCombinations) {
        for (permutation in PermutationIterator(combination)) {
            val s: String = permutation.joinToString(separator = "")
            var leetS = ""
            if (leet) {
                leetS= toLeet(s)
                println(leetS)
            }
            if (append.isNotEmpty()) {
                println(s.plus(append))
                if (leet) {
                    println(leetS.plus(append))
                }
            }
            if (prepend.isNotEmpty()) {
                println(prepend.plus(s))
                if (leet) {
                    println(prepend.plus(leetS))
                }
                if (append.isNotEmpty()) {
                    println(prepend.plus(s).plus(append))
                    if (leet) {
                        println(prepend.plus(leetS).plus(append))
                    }
                }
            }
        }
    }
}
