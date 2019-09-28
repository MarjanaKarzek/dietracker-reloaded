package de.karzek.diettracker.data.repository;

import java.util.ArrayList;
import java.util.List;

import de.karzek.diettracker.data.cache.interfaces.RecipeCache;
import de.karzek.diettracker.data.cache.model.RecipeEntity;
import de.karzek.diettracker.data.mapper.RecipeDataMapper;
import de.karzek.diettracker.data.model.RecipeDataModel;
import de.karzek.diettracker.data.repository.datasource.local.RecipeLocalDataSourceImpl;
import de.karzek.diettracker.data.repository.repositoryInterface.RecipeRepository;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by MarjanaKarzek on 27.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 27.05.2018
 */
public class RecipeRepositoryImpl implements RecipeRepository {

    private final RecipeDataMapper mapper;
    private final RecipeCache recipeCache;

    public RecipeRepositoryImpl(RecipeCache recipeCache, RecipeDataMapper mapper) {
        this.recipeCache = recipeCache;
        this.mapper = mapper;
    }

    @Override
    public Observable<Boolean> putRecipe(RecipeDataModel recipeDataModel) {
        return new RecipeLocalDataSourceImpl(recipeCache).putRecipe(mapper.transformToEntity(recipeDataModel));
    }

    @Override
    public Observable<List<RecipeDataModel>> getAllRecipes(ArrayList<String> filterOptions, String sortOption, boolean asc) {
        return new RecipeLocalDataSourceImpl(recipeCache).getAllRecipes(filterOptions, sortOption, asc).map(new Function<List<RecipeEntity>, List<RecipeDataModel>>() {
            @Override
            public List<RecipeDataModel> apply(List<RecipeEntity> recipeEntities) {
                return mapper.transformAll(recipeEntities);
            }
        });
    }

    @Override
    public Observable<RecipeDataModel> getRecipeById(int id) {
        return new RecipeLocalDataSourceImpl(recipeCache).getRecipeById(id).map(new Function<RecipeEntity, RecipeDataModel>() {
            @Override
            public RecipeDataModel apply(RecipeEntity recipeEntity) {
                return mapper.transform(recipeEntity);
            }
        });
    }

    @Override
    public Observable<Boolean> updateRecipe(RecipeDataModel recipe) {
        return new RecipeLocalDataSourceImpl(recipeCache).updateRecipe(mapper.transformToEntity(recipe));
    }

    @Override
    public Observable<Boolean> deleteRecipe(int id) {
        return new RecipeLocalDataSourceImpl(recipeCache).deleteRecipe(id);
    }

    @Override
    public Observable<List<RecipeDataModel>> getAllRecipesMatching(String query, ArrayList<String> filterOptions, String sortOption, boolean asc) {
        return new RecipeLocalDataSourceImpl(recipeCache).getAllRecipesMatching(query, filterOptions, sortOption, asc).map(new Function<List<RecipeEntity>, List<RecipeDataModel>>() {
            @Override
            public List<RecipeDataModel> apply(List<RecipeEntity> recipeEntities) {
                return mapper.transformAll(recipeEntities);
            }
        });
    }

    @Override
    public Observable<List<RecipeDataModel>> getAllRecipesMatching(String query) {
        return new RecipeLocalDataSourceImpl(recipeCache).getAllRecipesMatching(query).map(new Function<List<RecipeEntity>, List<RecipeDataModel>>() {
            @Override
            public List<RecipeDataModel> apply(List<RecipeEntity> recipeEntities) {
                return mapper.transformAll(recipeEntities);
            }
        });
    }
}