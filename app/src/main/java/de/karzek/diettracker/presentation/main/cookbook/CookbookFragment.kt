package de.karzek.diettracker.presentation.main.cookbook

import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import de.karzek.diettracker.R
import de.karzek.diettracker.presentation.TrackerApplication
import de.karzek.diettracker.presentation.common.BaseFragment
import de.karzek.diettracker.presentation.main.cookbook.adapter.RecipeCookbookSearchResultListAdapter
import de.karzek.diettracker.presentation.main.cookbook.dialog.filterOptionsDialog.RecipeFilterOptionsDialog
import de.karzek.diettracker.presentation.main.cookbook.dialog.sortOptionsDialog.RecipeSortOptionsDialog
import de.karzek.diettracker.presentation.main.cookbook.recipeDetails.RecipeDetailsActivity
import de.karzek.diettracker.presentation.main.cookbook.recipeManipulation.RecipeManipulationActivity
import de.karzek.diettracker.presentation.main.diary.meal.dialog.MealSelectorDialog
import de.karzek.diettracker.presentation.model.MealDisplayModel
import de.karzek.diettracker.presentation.model.RecipeDisplayModel
import kotlinx.android.synthetic.main.fragment_cookbook.*
import kotlinx.android.synthetic.main.snippet_transparent_loading_view.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by MarjanaKarzek on 12.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 12.05.2018
 */
class CookbookFragment : BaseFragment(), CookbookContract.View {

    @Inject
    lateinit var presenter: CookbookContract.Presenter

    private var noRecipesPlaceholder: String? = null
    private var noResultsPlaceholder: String? = null

    private val databaseDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.GERMANY)

    override fun onResume() {
        super.onResume()
        showLoading()
        presenter.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_cookbook, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.cookbook, menu)

        val searchManager = activity!!.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.cookbook_search).actionView as SearchView
        searchView.queryHint = getString(R.string.recipe_search_hint)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))
        searchView.isIconified = true
        searchView.maxWidth = Integer.MAX_VALUE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query != "")
                    presenter.getRecipesMatchingQuery(query)
                else
                    presenter.start()
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (query != "")
                    presenter.getRecipesMatchingQuery(query)
                else
                    presenter.start()
                return false
            }
        })
        add_recipe.setOnClickListener { startActivity(RecipeManipulationActivity.newAddIntent(context!!)) }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading()

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setTitle("")
        setupRecyclerView()
        noResultsPlaceholder = getString(R.string.recipe_search_query_without_result_placeholder)
        noRecipesPlaceholder = getString(R.string.cookbook_placeholder)

        presenter.setView(this)
        presenter.start()
        presenter.checkForOnboardingView()
    }

    private fun setupRecyclerView() {
        cookbook_recyclerview.setHasFixedSize(true)
        cookbook_recyclerview.layoutManager = LinearLayoutManager(context)
        cookbook_recyclerview.adapter = RecipeCookbookSearchResultListAdapter(presenter, presenter, presenter, presenter)
    }

    override fun setupFragmentComponent() {
        TrackerApplication.get(context!!).createCookbookComponent().inject(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        TrackerApplication.get(context!!).releaseCookbookComponent()
    }

    override fun hideLoading() {
        loading_view.visibility = View.GONE
    }

    override fun showLoading() {
        loading_view.visibility = View.VISIBLE
    }

    override fun showPlaceholder() {
        cookbook_placeholder.text = noRecipesPlaceholder
        cookbook_placeholder.visibility = View.VISIBLE
    }

    override fun updateRecyclerView(recipes: ArrayList<RecipeDisplayModel>) {
        (cookbook_recyclerview.adapter as RecipeCookbookSearchResultListAdapter).setList(recipes)
    }

    override fun hideRecyclerView() {
        cookbook_recyclerview.visibility = View.GONE
    }

    override fun hidePlaceholder() {
        cookbook_placeholder.visibility = View.GONE
    }

    override fun startRecipeDetailsActivity(id: Int) {
        startActivity(RecipeDetailsActivity.newIntent(context!!, id))
        hideLoading()
    }

    override fun startEditRecipe(id: Int) {
        startActivity(RecipeManipulationActivity.newEditIntent(context!!, id))
    }

    override fun showConfirmRecipeDeletionDialog(id: Int) {
        val builder = AlertDialog.Builder(context!!)

        builder.setMessage(getString(R.string.dialog_message_confirm_recipe_deletion))
        builder.setPositiveButton(getString(R.string.dialog_action_delete)) { dialogInterface, i -> presenter.onDeleteRecipeConfirmed(id) }
        builder.setNegativeButton(getString(R.string.dialog_action_dismiss), DialogInterface.OnClickListener { dialogInterface, i -> return@OnClickListener })
        builder.create().show()
    }

    override fun hideQueryWithoutResultPlaceholder() {
        cookbook_placeholder.visibility = View.GONE
    }

    override fun showRecyclerView() {
        cookbook_recyclerview.visibility = View.VISIBLE
    }

    override fun showQueryWithoutResultPlaceholder() {
        cookbook_placeholder.text = noResultsPlaceholder
        cookbook_placeholder.visibility = View.VISIBLE
    }

    override fun openFilterOptionsDialog(filterOptions: ArrayList<String>) {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        val previous = childFragmentManager.findFragmentByTag("dialog")
        if (previous != null)
            fragmentTransaction.remove(previous)
        fragmentTransaction.addToBackStack(null)

        val dialogFragment = RecipeFilterOptionsDialog()
        val bundle = Bundle()
        bundle.putStringArrayList("filterOptions", filterOptions)
        dialogFragment.arguments = bundle

        dialogFragment.show(fragmentTransaction, "dialog")
    }

    override fun openSortOptionsDialog(sortOption: String, asc: Boolean) {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        val previous = childFragmentManager.findFragmentByTag("dialog")
        if (previous != null)
            fragmentTransaction.remove(previous)
        fragmentTransaction.addToBackStack(null)

        val dialogFragment = RecipeSortOptionsDialog()
        val bundle = Bundle()
        bundle.putString("sortOption", sortOption)
        bundle.putBoolean("asc", asc)
        dialogFragment.arguments = bundle

        dialogFragment.show(fragmentTransaction, "dialog")
    }

    override fun showAddPortionForRecipeDialog(id: Int, meals: ArrayList<MealDisplayModel>) {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        val previous = childFragmentManager.findFragmentByTag("dialog")
        if (previous != null)
            fragmentTransaction.remove(previous)
        fragmentTransaction.addToBackStack(null)

        val dialogFragment = MealSelectorDialog()
        val bundle = Bundle()
        bundle.putInt("id", id)
        bundle.putParcelableArrayList("meals", meals)
        bundle.putString("instructions", getString(R.string.add_portion_to_diary_instructions))
        dialogFragment.arguments = bundle

        dialogFragment.show(fragmentTransaction, "dialog")
    }

    override fun showRecipeAddedToast() {
        Toast.makeText(context, getString(R.string.success_message_portion_added), Toast.LENGTH_SHORT).show()
    }

    override fun showOnboardingScreen(onboardingTag: Int) {
        //startActivity(OnboardingActivity.newIntent(getContext(), onboardingTag));
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.cookbook_search_filter) {
            presenter.onFilterOptionSelected()
        } else if (item.itemId == R.id.cookbook_search_sort) {
            presenter.onSortOptionSelected()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun filterOptionsSelected(filterOptions: ArrayList<String>) {
        presenter.filterRecipesBy(filterOptions)
    }

    override fun sortOptionSelected(sortOption: String?, asc: Boolean) {
        presenter.sortRecipesBy(sortOption, asc)
    }

    override fun mealSelectedInDialog(recipeId: Int, meal: MealDisplayModel) {
        presenter.addPortionToDiary(recipeId, meal, databaseDateFormat.format(Calendar.getInstance().time))
    }
}
