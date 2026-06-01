class AppStartTimeDelegate(
    scope: CoroutineScope
) {

    // Кешируем время один раз при создании делегата
    private val startTime: String = run {
        val timeMillis = System.currentTimeMillis()
        val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        formatter.format(Date(timeMillis))
    }

    init {
        // Логируем кешированное значение не в UI-потоке
        scope.launch(Dispatchers.IO) {
            while (isActive) {
                Log.d("time", "launch time: $startTime")
                delay(3_000)
            }
        }
    }

    // Возвращаем уже сохранённое время при обращении к свойству
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return startTime
    }
}
