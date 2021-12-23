package android.lab.demo_cpt.view

import android.lab.demo_cpt.R
import android.lab.demo_cpt.adapter.CurrencyAdapter
import androidx.fragment.app.Fragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed

import androidx.test.espresso.matcher.RootMatchers.withDecorView
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.Assert
import org.junit.Before

@HiltAndroidTest
class CurrencyListFragmentTest {

    private val hiltRule = HiltAndroidRule(this)
    private val activityTestRule = ActivityTestRule(DemoActivity::class.java)

    @get:Rule
    val rule = RuleChain.outerRule(hiltRule).around(activityTestRule)

    @Before
    fun setup() {
        onView(withId(R.id.btnShowList)).perform(click())
    }

    @Test
    fun testFragmentInView(){
       val currentFragment = activityTestRule.activity.supportFragmentManager.findFragmentById(R.id.nav_host)?.childFragmentManager?.fragments?.get(0)
        Assert.assertTrue(currentFragment is CurrencyListFragment)
    }

    @Test
    fun testClickListItem() {
        onView(withId(R.id.rvCurrencyList)).perform(RecyclerViewActions.actionOnItemAtPosition<CurrencyAdapter.CurrencyViewHolder>(2, click()))
        onView(withText("Ethereum"))
            .inRoot(withDecorView(not(`is`(activityTestRule.activity.window.decorView))))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testSortListItem() {
        onView(withId(R.id.btnSort)).perform(click())
        onView(withId(R.id.rvCurrencyList)).perform(RecyclerViewActions.actionOnItemAtPosition<CurrencyAdapter.CurrencyViewHolder>(2, click()))
        onView(withText("Bitcoin Cash"))
            .inRoot(withDecorView(not(`is`(activityTestRule.activity.window.decorView))))
            .check(matches(isDisplayed()))
    }
}