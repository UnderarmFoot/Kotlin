sealed class DataState<out T> {
    data object Loading : DataState<Nothing>()
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val throwable: Throwable) : DataState<Nothing>()
}

abstract class BaseViewModel<T> : ViewModel() {

    private val _state = MutableStateFlow<DataState<T>>(DataState.Loading)
    val state: StateFlow<DataState<T>> = _state.asStateFlow()

    protected fun setLoading() {
        _state.value = DataState.Loading
    }

    protected fun setSuccess(data: T) {
        _state.value = DataState.Success(data)
    }

    protected fun setError(throwable: Throwable) {
        _state.value = DataState.Error(throwable)
    }

    fun loadData() {
        viewModelScope.launch {
            setLoading()

            try {
                val result = fetchData()
                setSuccess(result)
            } catch (throwable: Throwable) {
                setError(throwable)
            }
        }
    }

    protected abstract suspend fun fetchData(): T
}


abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel<T>, T>(
    @LayoutRes layoutId: Int
) : Fragment(layoutId) {

    private var _binding: VB? = null
    protected val binding: VB
        get() = requireNotNull(_binding)

    protected abstract val viewModel: VM

    protected abstract fun bindView(view: View): VB

    protected open fun initToolbar() {
        binding.root.findViewById<Toolbar?>(R.id.toolbar)?.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    protected open fun initUi() = Unit

    protected open fun renderLoading() = Unit

    protected abstract fun renderSuccess(data: T)

    protected open fun renderError(throwable: Throwable) = Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = bindView(view)

        initToolbar()
        initUi()
        observeViewModel()

        viewModel.loadData()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        DataState.Loading -> renderLoading()
                        is DataState.Success -> renderSuccess(state.data)
                        is DataState.Error -> renderError(state.throwable)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}