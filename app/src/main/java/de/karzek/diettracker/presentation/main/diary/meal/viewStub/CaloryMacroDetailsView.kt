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
class CaloryMacroDetailsView(view: View) : CaloryDetailsView(view) {

    var proteinProgressBar: CircularProgressBar = view.findViewById(R.id.circle_progress_bar_protein)
    var proteinProgressBarValue: TextView = view.findViewById(R.id.circle_progress_bar_protein_value)
    var proteinProgressBarMaxValue: TextView = view.findViewById(R.id.circle_progress_bar_protein_max_value)

    var carbsProgressBar: CircularProgressBar = view.findViewById(R.id.circle_progress_bar_carbs)
    var carbsProgressBarValue: TextView = view.findViewById(R.id.circle_progress_bar_carbs_value)
    var carbsProgressBarMaxValue: TextView = view.findViewById(R.id.circle_progress_bar_carbs_max_value)

    var fatsProgressBar: CircularProgressBar = view.findViewById(R.id.circle_progress_bar_fats)
    var fatsProgressBarValue: TextView = view.findViewById(R.id.circle_progress_bar_fats_value)
    var fatsProgressBarMaxValue: TextView = view.findViewById(R.id.circle_progress_bar_fats_max_value)

}
