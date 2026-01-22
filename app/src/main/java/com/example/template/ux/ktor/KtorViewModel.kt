package com.example.template.ux.ktor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.example.template.model.webservice.GoogleBooksService
import com.example.template.util.SmtLogger
import kotlinx.coroutines.launch
import org.lds.mobile.network.onException
import org.lds.mobile.network.onSuccess

class KtorViewModel(
    private val googleBooksService: GoogleBooksService,
) : ViewModel() {
    val uiState = KtorUiState(
        onExecute = ::onExecute
    )

    private fun onExecute() = viewModelScope.launch {
        SmtLogger.i("""onExecute()""")
        googleBooksService.getVolumes("intitle", 2)
            .onSuccess {
                SmtLogger.i(
                    """Success:
                    |${this.data}
                """.trimMargin()
                )
            }
            .onException {
                Logger.e(this.throwable) { "Failed to getVolumes" }
            }
    }
}