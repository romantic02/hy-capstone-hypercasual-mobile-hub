package com.capstone.hub.launcher

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import com.capstone.hub.R
import com.capstone.hub.model.GameInfo

sealed class LaunchResult {
    data object Success : LaunchResult()
    data object NotInstalled : LaunchResult()
    data object Failed : LaunchResult()
}

object GameLauncher {

    fun isInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (_: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun launch(context: Context, game: GameInfo): LaunchResult {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(game.packageName)
            ?.apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

        return when {
            launchIntent == null && !isInstalled(context, game.packageName) -> LaunchResult.NotInstalled
            launchIntent == null -> LaunchResult.Failed
            else -> {
                context.startActivity(launchIntent)
                LaunchResult.Success
            }
        }
    }

    fun launchWithFeedback(context: Context, game: GameInfo) {
        when (launch(context, game)) {
            LaunchResult.Success -> Unit
            LaunchResult.NotInstalled -> Toast.makeText(
                context,
                context.getString(R.string.not_installed),
                Toast.LENGTH_LONG,
            ).show()
            LaunchResult.Failed -> Toast.makeText(
                context,
                context.getString(R.string.launch_failed),
                Toast.LENGTH_SHORT,
            ).show()
        }
    }
}
