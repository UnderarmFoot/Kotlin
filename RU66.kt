enum class SaveMode {
    COMMIT,
    APPLY
}

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