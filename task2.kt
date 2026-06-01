class AppStartTimeDelegate(
    scope: CoroutineScope
) {

    private val startTime: String = run {
        val timeMillis = System.currentTimeMillis()
        val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        formatter.format(Date(timeMillis))
    }

    init {
        scope.launch(Dispatchers.IO) {
            while (isActive) {
                Log.d("time", "launch time: $startTime")
                delay(3_000)
            }
        }
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return startTime
    }
}