package me.shtanko.akvarel

import android.app.Activity
import android.os.Bundle
import me.shtanko.akvarel.R

class AppLaunchActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
    }

}