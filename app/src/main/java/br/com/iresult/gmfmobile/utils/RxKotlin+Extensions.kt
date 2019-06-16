package br.com.iresult.gmfmobile.utils

import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

fun Disposable.disposedBy(disposeBag: CompositeDisposable) {
    disposeBag.add(this)
}

fun <T>Single<T>.defaultScheduler(): Single<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
}

fun <T>Observable<T>.defaultScheduler(): Observable<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
}

fun <T>Maybe<T>.defaultScheduler(): Maybe<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
}