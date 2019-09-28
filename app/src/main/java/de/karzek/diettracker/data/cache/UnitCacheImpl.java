package de.karzek.diettracker.data.cache;

import java.util.List;

import de.karzek.diettracker.data.cache.interfaces.GroceryCache;
import de.karzek.diettracker.data.cache.interfaces.UnitCache;
import de.karzek.diettracker.data.cache.model.GroceryEntity;
import de.karzek.diettracker.data.cache.model.UnitEntity;
import de.karzek.diettracker.data.model.UnitDataModel;
import io.reactivex.Observable;
import io.realm.Case;
import io.realm.Realm;

/**
 * Created by MarjanaKarzek on 27.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 27.05.2018
 */
public class UnitCacheImpl implements UnitCache {

    @Override
    public Observable<Boolean> putAllUnits(List<UnitEntity> unitEntities) {
        Realm realm = Realm.getDefaultInstance();
        for (UnitEntity entity : unitEntities)
            realm.copyToRealmOrUpdate(entity);
        realm.commitTransaction();
        realm.close();
        return Observable.just(true);
    }

    @Override
    public Observable<List<UnitEntity>> getAllDefaultUnits(int type) {
        Realm realm = Realm.getDefaultInstance();
        return Observable.just(realm.copyFromRealm(realm.where(UnitEntity.class).equalTo("type", type).sort("id").findAll()));

    }

    @Override
    public Observable<UnitEntity> getUnitByName(String name) {
        Realm realm = Realm.getDefaultInstance();
        return Observable.just(realm.copyFromRealm(realm.where(UnitEntity.class).equalTo("name", name).findFirst()));
    }

    @Override
    public Observable<UnitEntity> getUnitById(int id) {
        Realm realm = Realm.getDefaultInstance();
        return Observable.just(realm.copyFromRealm(realm.where(UnitEntity.class).equalTo("id", id).findFirst()));
    }
}