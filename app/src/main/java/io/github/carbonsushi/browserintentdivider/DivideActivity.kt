package io.github.carbonsushi.browserintentdivider

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

class DivideActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sendIntent = Intent(Intent.ACTION_VIEW, Uri.parse(intent.dataString))
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        val queryActivities =
            packageManager.queryIntentActivities(sendIntent, PackageManager.MATCH_ALL)

        val preferenceManager = PreferenceManager.getDefaultSharedPreferences(this)
        val priorityPackageList =
            preferenceManager.getString("priority_package_list", "")!!.split("\n")

        priorityPackageList.forEach { packageName ->
            queryActivities.forEach { resolveInfo ->
                val activityInfo = resolveInfo.activityInfo
                if (activityInfo.packageName == packageName) {
                    startActivity(
                        sendIntent.setClassName(activityInfo.packageName, activityInfo.name)
                    )
                    finishAndRemoveTask()
                    return
                }
            }
        }

        val excludePackageList =
            preferenceManager.getString("exclude_package_list", "")!!.split("\n")

        queryActivities.forEach { resolveInfo ->
            val activityInfo = resolveInfo.activityInfo
            if (activityInfo.packageName != BuildConfig.APPLICATION_ID
                && !excludePackageList.contains(activityInfo.packageName)
            ) {
                startActivity(sendIntent.setClassName(activityInfo.packageName, activityInfo.name))
                finishAndRemoveTask()
                return
            }
        }

        Toast.makeText(this, R.string.error_toast, Toast.LENGTH_LONG).show()
        finishAndRemoveTask()
    }
}