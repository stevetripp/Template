package com.example.template.ux.stickyheaders

import androidx.lifecycle.ViewModel
import com.example.template.inject.ApplicationScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StickyHeaderViewModel
@Inject constructor(
    @param:ApplicationScope
    private val applicationScope: CoroutineScope,
) : ViewModel() {

    private val lazyColumnItemsFlow = MutableStateFlow<List<LazyColumnItem>>(emptyList())

    val uiState = StickyHeadersUiState(
        lazyColumnItemsFlow = lazyColumnItemsFlow,
    )

    init {
        applicationScope.launch {
            val orderedFruits = fruits.sorted().map { fruit -> fruit.replaceFirstChar { it.titlecase() } }
            var previousHeader = ""
            val lazyColumnItems = mutableListOf<LazyColumnItem>()
            orderedFruits.forEach {
                val firstChar = it.first().toString()
                if (firstChar != previousHeader) {
                    previousHeader = firstChar
                    lazyColumnItems.add(LazyColumnItem.Header(firstChar))
                }
                lazyColumnItems.add(LazyColumnItem.Item(it))
            }
            lazyColumnItemsFlow.value = lazyColumnItems
        }
    }

    companion object {
        val fruits = listOf(
            "apple", "banana", "orange", "grape", "strawberry", "watermelon",
            "pineapple", "blueberry", "kiwi", "mango", "pear", "peach", "plum",
            "lemon", "lime", "cherry", "raspberry", "blackberry", "coconut",
            "avocado", "pomegranate", "fig", "nectarine", "apricot", "cranberry",
            "elderberry", "guava", "papaya", "lychee", "dragonfruit", "passionfruit",
            "melon", "cantaloupe", "honeydew", "persimmon", "tangerine", "kumquat",
            "boysenberry", "rhubarb", "starfruit", "mulberry", "date", "quince",
            "tamarind", "plantain", "breadfruit", "durian", "jackfruit", "salak",
            "soursop", "longan", "lychee", "mangosteen", "rambutan", "ackee",
            "currant", "pawpaw", "saskatoon", "bilberry", "cloudberry", "barberry",
            "carambola", "chayote", "feijoa", "loquat", "ugli fruit", "yuzu",
            "surinam cherry", "ugni", "mamey sapote", "sapodilla", "tamarillo",
            "uvaia", "noni", "pulasan", "santol", "jabuticaba", "cupuacu",
            "kiwano", "miracle fruit", "muscadine", "pepino", "ugni", "wampee",
            "white sapote", "zalzalak", "acai", "physalis", "cactus fruit", "carissa"
        )
    }
}