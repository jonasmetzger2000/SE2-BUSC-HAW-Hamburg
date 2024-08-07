package de.haw_hamburg.mensamatch.adapter.persistence.meal;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.DeleteOptions;
import de.haw_hamburg.mensamatch.adapter.persistence.meal.model.MealDao;
import de.haw_hamburg.mensamatch.domain.meal.MealRepository;
import de.haw_hamburg.mensamatch.domain.meal.model.Meal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

@Repository
@RequiredArgsConstructor
public class MongoMealRepository implements MealRepository {

    private final MongoCollection<MealDao> collection;

    @Override
    public void store(Meal meal) {
        if (!(collection.countDocuments(and(eq("name", meal.getName()), eq("day", meal.getDay()))) > 0)) {
            collection.insertOne(MealDao.from(meal));
        }
    }

    @Override
    public List<Meal> getFrom(LocalDate date) {
        List<Meal> meals = new ArrayList<>();
        final FindIterable<MealDao> day = collection.find(eq("day", date), MealDao.class);
        for (MealDao meal : day) {
            meals.add(meal.toMeal());
        }
        return meals;
    }

    @Override
    public void deleteOlder30Days() {
        collection.deleteMany(lte("day", LocalDate.now().minusDays(30)));
    }
}
