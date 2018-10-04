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

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.shtanko.akvarel.widget.AkvarelBottomNavigationView
import me.shtanko.categories.CategoriesFragment
import me.shtanko.collection.CollectionFragment
import me.shtanko.common.ui.BaseFragment
import me.shtanko.core.Logger
import me.shtanko.other.OtherFragment
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var logger: Logger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        provideInjection()
        setContentView(R.layout.activity_main)

        val collection = CollectionFragment.instance
        val categories = CategoriesFragment.instance
        val others = OtherFragment.instance

        val bottomView: AkvarelBottomNavigationView =
                findViewById(R.id.bottomNavigation)

        bottomView.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.navigationCollection -> {
                    replaceFragment(collection)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigationCategories -> {
                    replaceFragment(categories)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigationSettings -> {
                    replaceFragment(others)
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
}
