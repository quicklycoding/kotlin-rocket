package com.quicklycoding.rocketkotlin.util.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.kodein.di.*

inline fun <reified VM : ViewModel> DI.Builder.bindViewModel() = bind<VM>(VM::class.java.simpleName)

inline fun <reified VM : ViewModel, T> T.kodeinViewModel(): Lazy<VM> where  T : DIAware, T : Fragment {
    return lazy { ViewModelProvider(this, direct.instance())[VM::class.java] }
}

inline fun <reified VM : ViewModel, T> T.kodeinViewModel(): Lazy<VM> where  T : DIAware, T : AppCompatActivity {
    return lazy { ViewModelProvider(this, direct.instance())[VM::class.java] }
}