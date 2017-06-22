package com.vitech.bakersbook;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class BakersBookActivityTest2 {

    @Rule
    public ActivityTestRule<BakersBookActivity> mActivityTestRule = new ActivityTestRule<>(BakersBookActivity.class);

    @Test
    public void bakersBookActivityTest2() {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recipe_list),
                        withParent(allOf(withId(android.R.id.content),
                                withParent(withId(R.id.decor_content_parent)))),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open Navigation Drawer"),
                        withParent(allOf(withId(R.id.action_bar),
                                withParent(withId(R.id.action_bar_container)))),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.all_recipes),
                        withParent(allOf(withId(R.id.recipe_drawer),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(2, click()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.ingredient_name), withText("sifted cake flour"),
                        childAtPosition(
                                allOf(withId(R.id.ingredient_item),
                                        childAtPosition(
                                                withId(R.id.ingredients_list),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("sifted cake flour")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.ingredient_quantity), withText("400.0"),
                        childAtPosition(
                                allOf(withId(R.id.ingredient_item),
                                        childAtPosition(
                                                withId(R.id.ingredients_list),
                                                0)),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("400.0")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.ingredient_measure), withText("G"),
                        childAtPosition(
                                allOf(withId(R.id.ingredient_item),
                                        childAtPosition(
                                                withId(R.id.ingredients_list),
                                                0)),
                                2),
                        isDisplayed()));
        textView3.check(matches(withText("G")));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
