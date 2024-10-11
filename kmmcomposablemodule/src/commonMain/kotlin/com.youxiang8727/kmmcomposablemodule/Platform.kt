package com.youxiang8727.kmmcomposablemodule

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform


