package com.yuch.storyapp.view.login

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.yuch.storyapp.R
import com.yuch.storyapp.util.LoginLogoutIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Test

class LoginActivityTest {
    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(LoginLogoutIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(LoginLogoutIdlingResource.countingIdlingResource)
    }

    @Test
    fun loginAndLogoutSuccess() {
        val intent = Intent(InstrumentationRegistry.getInstrumentation().targetContext, LoginActivity::class.java)
        ActivityScenario.launch<LoginActivity>(intent)

        onView(withId(R.id.ed_login_email)).perform(typeText("awd@co.co"))
        onView(withId(R.id.ed_login_password)).perform(typeText("12345678"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        onView(withText("Yeah!")).check(matches(isDisplayed()))
        onView(withText("Lanjut")).perform(click())

        onView(withId(R.id.menu2)).perform(click())
    }

    @Test
    fun loginAndLogoutFailed() {
        val intent = Intent(InstrumentationRegistry.getInstrumentation().targetContext, LoginActivity::class.java)
        ActivityScenario.launch<LoginActivity>(intent)

        onView(withId(R.id.ed_login_email)).perform(typeText("awd@co .co"))
        onView(withId(R.id.ed_login_password)).perform(typeText("12345678"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        onView(withText("Oops...")).check(matches(isDisplayed()))
        onView(withText("Coba Lagi")).perform(click())

        onView(withId(R.id.ed_login_email)).perform(clearText())
        onView(withId(R.id.ed_login_email)).perform(typeText("awd@co.co"))
        onView(withId(R.id.ed_login_password)).perform(clearText())
        onView(withId(R.id.ed_login_password)).perform(typeText("123456781"), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        onView(withText("Oops...")).check(matches(isDisplayed()))
        onView(withText("Coba Lagi")).perform(click())
    }
}