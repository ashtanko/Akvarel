package me.shtanko.core

import android.content.Context
import me.shtanko.core.di.ApplicationProvider

interface App {
  fun getApplicationContext(): Context
  fun getAppComponent():ApplicationProvider
}