package de.karzek.diettracker.domain.interactor.useCase.grocery;

import de.karzek.diettracker.data.repository.GroceryRepositoryImpl;
import de.karzek.diettracker.data.repository.repositoryInterface.GroceryRepository;
import de.karzek.diettracker.domain.mapper.GroceryDomainMapper;
import de.karzek.diettracker.data.model.GroceryDataModel;
import de.karzek.diettracker.domain.interactor.useCase.useCaseInterface.grocery.GetGroceryByIdUseCase;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by MarjanaKarzek on 27.05.2018.
 *
 * @author Marjana Karzek
 * @version 1.0
 * @date 27.05.2018
 */
public class GetGroceryByIdUseCaseImpl implements GetGroceryByIdUseCase {

    private final GroceryRepository repository;
    private final GroceryDomainMapper mapper;

    public GetGroceryByIdUseCaseImpl(GroceryRepository repository, GroceryDomainMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Observable<Output> execute(Input input) {
        return repository.getGroceryByID(input.getId()).map(new Function<GroceryDataModel, Output>() {
            @Override
            public Output apply(GroceryDataModel grocery){
                return new Output(Output.SUCCESS, mapper.transform(grocery));
            }
        });
    }

}