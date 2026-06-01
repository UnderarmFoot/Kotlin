// Режим сохранения данных: синхронный commit или асинхронный apply
enum class SaveMode {
    COMMIT,
    APPLY
}

// Асинхронно сохраняет данные в SharedPreferences и возвращает результат через callbacks
fun SharedPreferences.saveAsync(
    scope: CoroutineScope,
    mode: SaveMode,
    onSuccess: () -> Unit,
    onError: (Throwable) -> Unit,
    editBlock: SharedPreferences.Editor.() -> Unit
) {
    scope.launch(Dispatchers.IO) {
        try {
            val editor = edit()
            editor.editBlock()

            val success = when(mode) {
                SaveMode.COMMIT -> {
                    editor.commit()
                }
                SaveMode.APPLY -> {
                    editor.apply()
                    true
                }
            }

            withContext(Dispatchers.Main) {
                if (success) {
                    onSuccess()
                } else {
                    onError(IllegalStateException("SharedPreferences commit failed"))
                }
            }
        } catch(throwable: Throwable) {
            withContext(Dispatchers.Main) {
                onError(throwable)
            }
        }
    }
}

// Асинхронно загружает данные из SharedPreferences, отфильтрованные по ключу
fun SharedPreferences.loadFilteredAsync(
    scope: CoroutineScope,
    onSuccess: (Map<String, Any?>) -> Unit,
    onError: (Throwable) -> Unit = {},
    keyPredicate: (String) -> Boolean,
) { 
    scope.launch(Dispatchers.IO) {
        try {
            val result = all.filterKeys { key ->
                keyPredicate(key)
            }

            withContext(Dispatchers.Main) {
                onSuccess(result)
            }
        } catch (throwable: Throwable) {
            withContext(Dispatchers.Main) {
                onError(throwable)
            }
        }
    }
}
