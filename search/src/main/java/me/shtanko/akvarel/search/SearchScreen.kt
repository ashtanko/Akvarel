package me.shtanko.akvarel.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.shtanko.akvarel.search.di.SearchComponent
import me.shtanko.common.ui.BaseActivity
import me.shtanko.common.ui.BaseFragment

class SearchActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    //setContentView(R.layout.activity_container)
    if (savedInstanceState == null) {
//      supportFragmentManager.beginTransaction()
//          .replace(R.id.container, SearchFragment.INSTANCE)
//          .commit()
    }
  }

  fun inject() {

  }

}

class SearchFragment : BaseFragment() {

  companion object {
    val INSTANCE = SearchFragment()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.screen_search, container, false)
  }
}
