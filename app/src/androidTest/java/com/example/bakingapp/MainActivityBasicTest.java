package com.example.bakingapp;

import com.example.bakingapp.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class MainActivityBasicTest {

    private int randPosition;

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    public MainActivityBasicTest()
    {
        Random rand = new Random();
        randPosition = rand.nextInt(3);
    }

    //Test to see if we can access MainActivity and randomly clicks on a possible recipe recycler list item
    @Test
    public void mainActivityBasicTest()
    {
        onView(withId(R.id.recyclerview_recipes)).perform(RecyclerViewActions.actionOnItemAtPosition(randPosition, click()));
    }
}
