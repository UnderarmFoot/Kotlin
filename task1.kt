data class Key(
    val field1: Int,
    var field2: String
) {
    var field3: String? = null
}

/*
Проблема есть.

field1 и field2 участвуют в equals()/hashCode(), так как находятся в primary constructor.
field3 не участвует.

Если изменить field2 после добавления объекта в HashMap,
hashCode изменится и значение по ключу может перестать находиться.

Поэтому ключи для HashMap желательно делать неизменяемыми.
*/
