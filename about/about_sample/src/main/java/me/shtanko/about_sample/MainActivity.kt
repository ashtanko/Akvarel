package me.shtanko.about_sample

import android.os.Bundle
import me.shtanko.common.ui.BaseActivity

class MainActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
//    val intent = Intent(this, AboutActivity::class.java)
//    startActivity(intent)
  }
}
