package de.karzek.diettracker.data.cache;

import java.util.List;

import de.karzek.diettracker.data.cache.interfaces.DiaryEntryCache;
import de.karzek.diettracker.data.cache.interfaces.UnitCache;
import de.karzek.diettracker.data.cache.model.DiaryEntryEntity;
import de.karzek.diettracker.data.cache.model.GroceryEntity;
import de.karzek.diettracker.data.cache.model.MealEntity;
import de.karzek.diettracker.data.cache.model.UnitEntity;
import de.karzek.diettracker.data.model.DiaryEntryDataModel;
import de.karzek.diettracker.data.model.MealDataModel;
import de.karzek.diettracker.presentation.util.Constants;
import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by MarjanaKarzek on 27.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 27.05.2018
 */
public class DiaryEntryCacheImpl implements DiaryEntryCache {

    @Override
    public Observable<Boolean> putDiaryEntry(DiaryEntryEntity diaryEntryEntity) {
        Realm realm = Realm.getDefaultInstance();
        realm.copyToRealmOrUpdate(diaryEntryEntity);
        realm.commitTransaction();
        realm.close();
        return Observable.just(true);
    }

    @Override
    public Observable<List<DiaryEntryEntity>> getAllDiaryEntriesMatching(String meal, String date) {
        Realm realm = Realm.getDefaultInstance();
        if (meal != null && meal.equals(Constants.ALL_MEALS))
            return Observable.just(realm.copyFromRealm(realm.where(DiaryEntryEntity.class).equalTo("date", date).notEqualTo("grocery.id",0).sort("id").findAll()));
        else
            return Observable.just(realm.copyFromRealm(realm.where(DiaryEntryEntity.class).equalTo("meal.name", meal).equalTo("date", date).notEqualTo("grocery.id",0).sort("id").findAll()));
    }

    @Override
    public Observable<Boolean> deleteDiaryEntry(int id) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<DiaryEntryEntity> result = realm.where(DiaryEntryEntity.class).equalTo("id",id).findAll();
                result.deleteAllFromRealm();
            }
        });
        return Observable.just(true);
    }

    @Override
    public Observable<Boolean> updateMealOfDiaryEntry(int id, MealEntity meal) {
        Realm realm = Realm.getDefaultInstance();
        DiaryEntryEntity entity = realm.copyFromRealm(realm.where(DiaryEntryEntity.class).equalTo("id",id).findFirst());

        if (entity == null)
            return Observable.just(false);

        entity.setMeal(meal);
        realm.copyToRealmOrUpdate(entity);
        realm.commitTransaction();
        realm.close();
        return Observable.just(true);
    }

    @Override
    public Observable<DiaryEntryEntity> getWaterStatus(String date) {
        Realm realm = Realm.getDefaultInstance();

        if (realm.where(DiaryEntryEntity.class).equalTo("grocery.id", 0).equalTo("date", date).findFirst() == null){
            startWriteTransaction();
            DiaryEntryEntity entity = realm.createObject(DiaryEntryEntity.class,getNextId());
            entity.setMeal(null);
            entity.setAmount(0);
            entity.setUnit(realm.where(UnitEntity.class).equalTo("name","ml").findFirst());
            entity.setGrocery(realm.where(GroceryEntity.class).equalTo("id",0).findFirst());
            entity.setDate(date);

            putDiaryEntry(entity);
        }

        return Observable.just(realm.copyFromRealm(realm.where(DiaryEntryEntity.class).equalTo("grocery.id", 0).equalTo("date", date).findFirst()));

    }

    @Override
    public Observable<Boolean> updateAmountOfWater(float amount, String date) {
        Realm realm = Realm.getDefaultInstance();

        DiaryEntryEntity entity = realm.where(DiaryEntryEntity.class).equalTo("grocery.id", 0).equalTo("date", date).findFirst();

        startWriteTransaction();
        entity.setAmount(amount);
        realm.insertOrUpdate(entity);
        realm.commitTransaction();
        realm.close();

        return Observable.just(true);
    }

    @Override
    public Observable<Boolean> addAmountOfWater(float amount, String date) {
        Realm realm = Realm.getDefaultInstance();

        DiaryEntryEntity entity = realm.where(DiaryEntryEntity.class).equalTo("grocery.id", 0).equalTo("date", date).findFirst();

        startWriteTransaction();
        entity.setAmount(entity.getAmount() + amount);
        realm.insertOrUpdate(entity);
        realm.commitTransaction();
        realm.close();

        return Observable.just(true);
    }

    @Override
    public Observable<DiaryEntryEntity> getDiaryEntryById(int id) {
        Realm realm = Realm.getDefaultInstance();
        return Observable.just(realm.copyFromRealm(realm.where(DiaryEntryEntity.class).equalTo("id", id).findFirst()));

    }

    @Override
    public Observable<Boolean> deleteAllDiaryEntriesMatchingMealId(int mealId) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<DiaryEntryEntity> result = realm.where(DiaryEntryEntity.class).equalTo("meal.id", mealId).findAll();
                result.deleteAllFromRealm();
            }
        });
        return Observable.just(true);
    }

    private void startWriteTransaction(){
        Realm realm = Realm.getDefaultInstance();
        if(!realm.isInTransaction()){
            realm.beginTransaction();
        }
    }

    private int getNextId(){
        Number currentIdNum = Realm.getDefaultInstance().where(DiaryEntryEntity.class).max("id");
        int nextId;
        if(currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }
        return nextId;
    }
}