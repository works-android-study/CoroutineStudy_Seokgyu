package com.example.searchimagecoroutine.hilt

import javax.inject.Qualifier

interface RetrofitQualifier {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class SearchRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DownloadRetrofit
}
