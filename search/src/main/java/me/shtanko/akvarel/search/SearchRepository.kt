package me.shtanko.akvarel.search

import me.shtanko.network.NetworkClient
import javax.inject.Inject

class SearchRepository @Inject constructor(
  private val client: NetworkClient
) {

}