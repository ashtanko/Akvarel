/*
 * MIT License
 *
 * Copyright (c) 2018 Alexey Shtanko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.shtanko.akvarel

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import me.shtanko.favorites.FavoritesFragment
import me.shtanko.akvarel.widget.AkvarelBottomNavigationView
import me.shtanko.categories.CategoriesFragment
import me.shtanko.collection.CollectionFragment
import me.shtanko.common.ui.BaseFragment
import me.shtanko.core.Logger
import me.shtanko.core.navigation.OnAboutClickListener
import me.shtanko.other.OtherFragment
import javax.inject.Inject

class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragmentList: MutableList<Fragment> = mutableListOf<Fragment>()

    fun add(fragment: BaseFragment) {
        fragmentList.add(fragment)
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

}

enum class TAB(val value: Int) {
    COLLECTION(0),
    CATEGORIES(1),
    FAVORITES(2),
    SETTINGS(3),
}

class MainActivity : AppCompatActivity(), OnAboutClickListener {


    override fun openAboutScreen() {

    }

    @Inject
    lateinit var logger: Logger

    lateinit var bottomView: AkvarelBottomNavigationView
    lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        provideInjection()
        setContentView(R.layout.activity_main)

        val map = hashMapOf<TAB, BaseFragment>()

        val collection = CollectionFragment.instance
        val categories = CategoriesFragment.instance
        val others = OtherFragment.instance.also {
            it.clickListener = this
        }
        val favorites = FavoritesFragment.instance

        map[TAB.COLLECTION] = collection
        map[TAB.CATEGORIES] = categories
        map[TAB.FAVORITES] = favorites
        map[TAB.SETTINGS] = others

        bottomView = findViewById(R.id.bottomNavigation)
        viewPager = findViewById<ViewPager>(R.id.container)
        val pagerAdapter = PagerAdapter(supportFragmentManager)
        map[TAB.COLLECTION]?.let { pagerAdapter.add(it) }
        map[TAB.CATEGORIES]?.let { pagerAdapter.add(it) }
        map[TAB.FAVORITES]?.let { pagerAdapter.add(it) }
        map[TAB.SETTINGS]?.let { pagerAdapter.add(it) }
        viewPager.adapter = pagerAdapter
        viewPager.offscreenPageLimit = 2

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    TAB.COLLECTION.value -> {
                        bottomView.selectedItemId = R.id.navigationCollection
                    }

                    TAB.CATEGORIES.value -> {
                        bottomView.selectedItemId = R.id.navigationCategories
                    }

                    TAB.FAVORITES.value -> {
                        bottomView.selectedItemId = R.id.navigationFavorites
                    }

                    TAB.SETTINGS.value -> {
                        bottomView.selectedItemId = R.id.navigationSettings
                    }
                }
            }

        })


        bottomView.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.navigationCollection -> {
                    //replaceFragment(collection)
                    viewPager.currentItem = TAB.COLLECTION.value
                    setNavigationBackground(R.color.light_blue)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigationCategories -> {
                    //replaceFragment(categories)
                    viewPager.currentItem = TAB.CATEGORIES.value
                    setNavigationBackground(R.color.yellow)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigationFavorites -> {
                    //replaceFragment(favorites)
                    viewPager.currentItem = TAB.FAVORITES.value
                    setNavigationBackground(R.color.orange)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigationSettings -> {
                    //replaceFragment(others)
                    viewPager.currentItem = TAB.SETTINGS.value
                    setNavigationBackground(R.color.grey_800)
                    return@setOnNavigationItemSelectedListener true
                }

            }

            return@setOnNavigationItemSelectedListener false
        }

    }

    private fun replaceFragment(fragment: BaseFragment) {
//        supportFragmentManager.beginTransaction()
//                .replace(R.id.container, fragment)
//                .commit()
    }

    private fun setNavigationBackground(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            bottomView.setBackgroundColor(resources.getColor(color, theme))
        } else {
            bottomView.setBackgroundColor(resources.getColor(color))
        }
    }
}
