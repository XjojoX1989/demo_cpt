package android.lab.demo_cpt.view

import android.lab.demo_cpt.R
import android.lab.demo_cpt.adapter.CurrencyAdapter
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@HiltAndroidTest
class DemoActivityTest {
    private val hiltRule = HiltAndroidRule(this)
    private val activityTestRule = ActivityTestRule(DemoActivity::class.java)

    @get:Rule
    val rule = RuleChain.outerRule(hiltRule).around(activityTestRule)

    @Test
    fun testDefault() {
        val btnShowList = onView(Matchers.allOf(withId(R.id.btnShowList), ViewMatchers.withText("SHOW LIST"), isDisplayed()))
        btnShowList.check(matches(isDisplayed()))

        val btnSort = onView(Matchers.allOf(withId(R.id.btnSort), ViewMatchers.withText("SORT"),  isDisplayed()))
        btnSort.check(matches(isDisplayed()))
    }

    @Test
    fun testClickShowList() {
        onView(withId(R.id.btnShowList)).perform(click())
        onView(withId(R.id.rvCurrencyList)).check(matches(isDisplayed()))
    }

    @Test
    fun testClickSort(){
        onView(withId(R.id.btnSort)).perform(click())
    }
}