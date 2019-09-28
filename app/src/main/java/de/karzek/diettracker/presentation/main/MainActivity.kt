package de.karzek.diettracker.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import de.karzek.diettracker.R
import de.karzek.diettracker.designsystem.dialog.Dialog
import de.karzek.diettracker.presentation.TrackerApplication
import de.karzek.diettracker.presentation.common.BaseActivity
import de.karzek.diettracker.presentation.main.MainContract.FragmentIndex.*
import de.karzek.diettracker.presentation.main.cookbook.CookbookFragment
import de.karzek.diettracker.presentation.main.diary.DiaryFragment
import de.karzek.diettracker.presentation.main.diary.meal.GenericMealFragment
import de.karzek.diettracker.presentation.main.home.HomeFragment
import de.karzek.diettracker.presentation.main.settings.SettingsFragment
import de.karzek.diettracker.presentation.util.ViewUtils
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity(), MainContract.View, DiaryFragment.OnDateSelectedListener, GenericMealFragment.OnRefreshViewPagerNeededListener {

    @Inject
    lateinit var presenter: MainContract.Presenter

    override fun setupActivityComponents() {
        TrackerApplication.get(this).createMainComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //ViewUtils.customizeBottomNavigation(bottom_navigation_view)

        setupNavigationListener()
        when (intent.getIntExtra(EXTRA_FRAGMENT_ID, 0)) {
            FRAGMENT_HOME -> navigateToFragment(HomeFragment(), "HomeFragment")
            FRAGMENT_DIARY -> navigateToFragment(DiaryFragment(), "DiaryFragment")
            FRAGMENT_COOKBOOK -> navigateToFragment(CookbookFragment(), "CookbookFragment")
            FRAGMENT_SETTINGS -> navigateToFragment(SettingsFragment(), "SettingsFragment")
        }

        presenter.setView(this)
        presenter.start()
    }

    private fun setupNavigationListener() {
        bottom_navigation_view.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> navigateToFragment(HomeFragment(), "HomeFragment")
                R.id.action_diary -> navigateToFragment(DiaryFragment(), "DiaryFragment")
                R.id.action_cookbook -> navigateToFragment(CookbookFragment(), "CookbookFragment")
                R.id.action_settings -> navigateToFragment(SettingsFragment(), "SettingsFragment")
            }
            true
        }
    }

    private fun navigateToFragment(fragment: Fragment, tag: String) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment, tag)
                .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.finish()
        TrackerApplication.get(this).releaseMainComponent()
    }

    override fun onDateSelected(databaseDateFormat: String) {
        val fragment = supportFragmentManager.findFragmentByTag("DiaryFragment") as DiaryFragment?
        fragment!!.refreshViewPager()
    }

    override fun onRefreshViewPagerNeeded() {
        val fragment = supportFragmentManager.findFragmentByTag("DiaryFragment") as DiaryFragment?
        fragment!!.refreshViewPager()
    }


    override fun showOnboardingScreen() {
        Dialog.Builder(this)
                .setMessage(R.string.onboarding_text_welcome)
                .setPositiveText(R.string.lets_go_button)
                .setListener(object: Dialog.Companion.DialogButtonListener {
                    override fun onPositiveButtonClicked() {}
                })
                .setCancelable(true)
                .show()
    }

    companion object {

        private const val EXTRA_FRAGMENT_ID = "EXTRA_FRAGMENT_ID"

        fun newIntent(context: Context): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra(EXTRA_FRAGMENT_ID, FRAGMENT_HOME)
            return intent
        }

        fun newIntentToDiary(context: Context): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra(EXTRA_FRAGMENT_ID, FRAGMENT_DIARY)
            return intent
        }

        fun newIntentToCookbook(context: Context): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra(EXTRA_FRAGMENT_ID, FRAGMENT_COOKBOOK)
            return intent
        }
    }

}
