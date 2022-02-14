package io.github.carbonsushi.browserintentdivider

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.mikepenz.aboutlibraries.LibsBuilder

class SettingsActivity : AppCompatActivity(R.layout.activity_settings) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.settings_container, SettingsFragment())
                .commit()
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)

            findPreference<Preference>("about")?.setOnPreferenceClickListener {
                LibsBuilder().apply {
                    aboutShowIcon = true
                    aboutAppName = getString(R.string.app_name)
                    aboutShowVersionName = true
                    aboutDescription = getString(R.string.about_description)
                }.start(requireContext())
                true
            }
        }
    }
}