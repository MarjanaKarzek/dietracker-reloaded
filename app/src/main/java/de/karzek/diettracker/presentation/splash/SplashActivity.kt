package de.karzek.diettracker.presentation.splash

import android.os.Bundle
import de.karzek.diettracker.R
import de.karzek.diettracker.presentation.TrackerApplication
import de.karzek.diettracker.presentation.common.BaseActivity
import de.karzek.diettracker.presentation.main.MainActivity
import javax.inject.Inject

/**
 * Created by MarjanaKarzek on 31.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 31.05.2018
 */
class SplashActivity : BaseActivity(), SplashContract.View {

    @Inject
    lateinit var presenter: SplashContract.Presenter

    override fun setupActivityComponents() {
        TrackerApplication.get(this).createSplashComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        presenter.setView(this)

        presenter.init()
        presenter.start()
    }

    override fun startMainActivity() {
        startActivity(MainActivity.newIntent(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        TrackerApplication.get(this).releaseSplashComponent()
    }
}
