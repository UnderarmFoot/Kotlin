import kotlinx.coroutines.*  

fun shouldSwap(left: Int?, right: Int?): Boolean {
    return when {
        left == null && right == null -> false
        left == null -> true
        right == null -> false
        else -> left > right
    }
}

fun shakerSort(list: List<Int?>?): List<Int?> {
	if (list == null) return emptyList()
    
    val numbers = list.toMutableList()
    
    var left = 0
    var right = numbers.lastIndex
    
    while(left < right) {
        for(i in left until right) {
            if (shouldSwap(numbers[i], numbers[i + 1])) {
                val temp = numbers[i + 1]
                numbers[i + 1] = numbers[i]
                numbers[i] = temp
            }
        }
        
        right--
        
        for (i in right downTo left + 1) {
            if (shouldSwap(numbers[i - 1], numbers[i])) {
                val temp = numbers[i - 1]
                numbers[i - 1] = numbers[i]
                numbers[i] = temp
            }
        }
        
        left++
    }
    
    return numbers
}

fun main() {
	println(shakerSort(listOf(3, null, 1, 2, null)))
    println(shakerSort(null))
    println(shakerSort(listOf(null, null)))
    println(shakerSort(listOf(5, 4, 3, 2, 1)))
}