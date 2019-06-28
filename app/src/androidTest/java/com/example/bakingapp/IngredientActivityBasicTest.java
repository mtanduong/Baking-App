package com.example.bakingapp;

import com.example.bakingapp.activities.IngredientsActivity;
import com.example.bakingapp.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class IngredientActivityBasicTest {

    private int randPosition;

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    public IngredientActivityBasicTest()
    {
        Random rand = new Random();
        randPosition = rand.nextInt(3);
    }
    /**
     * This test will check if we can access the IngredientsActivity and we can scroll all the way down,meaning all items were handled correctly in Recycler.
     */
    @Test
    public void ingredientsActivityBasicTest()
    {
        //mainActivityBasicTest();
        onView(withId(R.id.recyclerview_recipes)).perform(RecyclerViewActions.actionOnItemAtPosition(randPosition, click()));
        onView(withId(R.id.ingredientsTextView)).perform(click());
        onView(withId(R.id.recyclerview_ingredients)).perform
                (RecyclerViewActions.actionOnItemAtPosition(IngredientsActivity.ingredientsLengthForTest - 1, scrollTo()));
    }
}
