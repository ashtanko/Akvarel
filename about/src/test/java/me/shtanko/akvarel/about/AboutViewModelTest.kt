package me.shtanko.akvarel.about

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AboutViewModelTest {

  private lateinit var aboutViewModel: AboutViewModel

  @Before
  fun setUp() {
    aboutViewModel = AboutViewModel()
  }

  @Test
  fun `tmp solution view model test`() {
    Assert.assertTrue(aboutViewModel.returnParam(true))
  }
}