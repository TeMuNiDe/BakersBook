package com.vitech.bakersbook;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.intent.Intents.intended;



import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.action.ViewActions.*;

import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.vitech.bakersbook.RecipeActivity.EXTRA_RECIPE;
import static com.vitech.bakersbook.RecipeActivity.EXTRA_RECIPES;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class BakersBookActivityTest {

    @Rule
    public IntentsTestRule<BakersBookActivity> mActivityTestRule = new IntentsTestRule<>(BakersBookActivity.class);

    @Test
    public void bakersBookActivityTest() {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recipe_list),
                        withParent(allOf(withId(android.R.id.content),
                                withParent(withId(R.id.decor_content_parent)))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));
        intended(allOf(hasExtraWithKey(EXTRA_RECIPE),hasExtraWithKey(EXTRA_RECIPES)));


    }







}
