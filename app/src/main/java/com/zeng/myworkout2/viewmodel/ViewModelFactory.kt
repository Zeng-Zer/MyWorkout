package com.zeng.myworkout2.viewmodel

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

class BaseViewModelFactory<T>(val creator: () -> T) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return creator() as T
    }
}

inline fun <reified T : ViewModel> Fragment.getViewModel(noinline creator: (() -> T)? = null, key: String? = null): T {
    return if (creator == null) {
        if (key == null)
            ViewModelProviders.of(this).get(T::class.java)
        else
            ViewModelProviders.of(this).get(key, T::class.java)
    } else {
        if (key == null)
            ViewModelProviders.of(this, BaseViewModelFactory(creator)).get(T::class.java)
        else
            ViewModelProviders.of(this, BaseViewModelFactory(creator)).get(key, T::class.java)
    }
}

inline fun <reified T : ViewModel> FragmentActivity.getViewModel(noinline creator: (() -> T)? = null, key: String? = null): T {
    return if (creator == null) {
        if (key == null)
            ViewModelProviders.of(this).get(T::class.java)
        else
            ViewModelProviders.of(this).get(key, T::class.java)
    } else {
        if (key == null)
            ViewModelProviders.of(this, BaseViewModelFactory(creator)).get(T::class.java)
        else
            ViewModelProviders.of(this, BaseViewModelFactory(creator)).get(key, T::class.java)
    }
}
