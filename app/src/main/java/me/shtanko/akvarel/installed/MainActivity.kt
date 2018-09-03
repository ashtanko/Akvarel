package me.shtanko.akvarel.installed

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.crashlytics.android.Crashlytics
import me.shtanko.akvarel.R
import me.shtanko.common.extensions.About
import me.shtanko.common.extensions.intentTo
import me.shtanko.core.utils.AndroidUtils

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_drawer)


    AndroidUtils.checkDisplaySize(this, resources.configuration)

    val drawer = findViewById<AkvarelDrawer>(R.id.akvarelDrawer)

    val drawerLayout =
      LayoutInflater.from(applicationContext).inflate(R.layout.layout_drawer, null) as LinearLayout

    drawer.setDrawerLayout(drawerLayout)

    Handler().postDelayed({
      //drawer.openDrawer()
    }, 1000)

    //drawer.openDrawer(false)

    //ActivityLaunchHelper.launchCategories(this)
    //supportFinishAfterTransition()

    if (savedInstanceState == null) {

    }

//    startActivity(
//        intentTo(About)
//        //, ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
//    )
  }
}
