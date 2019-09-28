package de.karzek.diettracker.presentation.main.diary.meal.viewStub

import android.view.View
import android.widget.TextView

import com.mikhaellopez.circularprogressbar.CircularProgressBar

import de.karzek.diettracker.R


/**
 * Created by MarjanaKarzek on 31.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 31.05.2018
 */
open class CaloryDetailsView(view: View) {

    var caloryProgressBar: CircularProgressBar = view.findViewById(R.id.circle_progress_bar_calories)
    var caloryProgressBarValue: TextView = view.findViewById(R.id.circle_progress_bar_calories_value)
    var caloryProgressBarMaxValue: TextView = view.findViewById(R.id.circle_progress_bar_calories_max_value)

}
