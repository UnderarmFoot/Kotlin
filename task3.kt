// Ищет первый элемент типа Int в списке Any
fun List<Any>.findInt(): Int? {
    for (item in this) {
        if (item is Int) {
            return item
        }
    }

    return null
}

// Проверка работы extension-функции на списке с разными типами данных
fun main() {
    val list: List<Any> = listOf(
        "Hello",
        3.14,
        true,
        "Kotlin",
        42,
        User("Nikita")
    )

    println(list.findInt())
}

data class User(
    val name: String
)
