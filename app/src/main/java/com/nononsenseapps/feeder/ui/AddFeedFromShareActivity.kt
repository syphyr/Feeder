package com.nononsenseapps.feeder.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nononsenseapps.feeder.base.DIAwareComponentActivity
import com.nononsenseapps.feeder.base.diAwareViewModel
import com.nononsenseapps.feeder.ui.compose.editfeed.CreateFeedScreen
import com.nononsenseapps.feeder.ui.compose.navigation.AddFeedDestination
import com.nononsenseapps.feeder.ui.compose.searchfeed.SearchFeedScreen
import com.nononsenseapps.feeder.ui.compose.utils.withAllProviders

/**
 * This activity should only be started via a Send (share) or Open URL/Text intent.
 */
class AddFeedFromShareActivity : DIAwareComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val initialFeedUrl =
            (intent?.dataString ?: intent?.getStringExtra(Intent.EXTRA_TEXT))?.trim()

        setContent {
            withAllProviders {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "search") {
                    composable(
                        "search",
                        enterTransition = { fadeIn() },
                        exitTransition = { fadeOut() },
                        popEnterTransition = { fadeIn() },
                        popExitTransition = { fadeOut() },
                    ) { backStackEntry ->
                        SearchFeedScreen(
                            onNavigateUp = {
                                onNavigateUpFromIntentActivities()
                            },
                            searchFeedViewModel = backStackEntry.diAwareViewModel(),
                            {
                                AddFeedDestination.navigate(
                                    navController,
                                    feedUrl = it.url,
                                    feedTitle = it.title,
                                    feedImage = it.feedImage,
                                )
                            },
                            initialFeedUrl = initialFeedUrl,
                        )
                    }
                    composable(
                        route = AddFeedDestination.route,
                        arguments = AddFeedDestination.arguments,
                        deepLinks = AddFeedDestination.deepLinks,
                        enterTransition = { fadeIn() },
                        exitTransition = { fadeOut() },
                        popEnterTransition = { fadeIn() },
                        popExitTransition = { fadeOut() },
                    ) { backStackEntry ->
                        CreateFeedScreen(
                            onNavigateUp = {
                                navController.popBackStack()
                            },
                            createFeedScreenViewModel = backStackEntry.diAwareViewModel(),
                        ) {
                            finish()
                        }
                    }
                }
            }
        }
    }
}

fun Activity.onNavigateUpFromIntentActivities() {
    startActivity(
        Intent(
            this,
            MainActivity::class.java,
        ),
    )
    finish()
}
