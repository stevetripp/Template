package com.example.template.ux.stickyheaders

sealed class LazyColumnItem(val text: String) {
    class Header(text: String) : LazyColumnItem(text)
    class Item(text: String) : LazyColumnItem(text)
}