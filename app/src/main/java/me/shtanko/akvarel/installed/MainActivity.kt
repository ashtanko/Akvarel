package me.shtanko.akvarel.installed

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.shtanko.akvarel.R
import me.shtanko.akvarel.installed.di.MainComponent
import me.shtanko.akvarel.installed.widget.AkvarelBottomNavigationView
import me.shtanko.collection.CollectionFragment
import me.shtanko.common.ui.BaseFragment
import me.shtanko.core.App
import me.shtanko.core.Logger
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

  @Inject
  lateinit var logger: Logger

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    inject()
    setContentView(R.layout.activity_main)

    val collection = CollectionFragment.instance

    val bottomView: AkvarelBottomNavigationView =
      findViewById(R.id.bottomNavigation)

    bottomView.setOnNavigationItemSelectedListener {

      when (it.itemId) {
        R.id.navigationCollection -> {
          replaceFragment(collection)
          return@setOnNavigationItemSelectedListener true
        }
      }

      return@setOnNavigationItemSelectedListener false
    }

  }

  private fun replaceFragment(fragment: BaseFragment) {
    supportFragmentManager.beginTransaction()
        .replace(R.id.container, fragment)
        .commit()
  }

  private fun inject() {
    MainComponent.Initializer.init((applicationContext as App).getAppComponent())
        .inject(this@MainActivity)
  }
}
