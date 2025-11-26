package com.example.photosearch

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.photosearch.ui.theme.LoginScreen
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun elementosVisibles_enLoginScreen() {

        composeTestRule.setContent {
            LoginScreen(
                onLoginSuccess = {},
                onNavigateToRegister = {}
            )
        }

        composeTestRule.onNodeWithTag("emailField").assertExists()
        composeTestRule.onNodeWithTag("passwordField").assertExists()
        composeTestRule.onNodeWithTag("loginButton").assertExists()
        composeTestRule.onNodeWithTag("goToRegisterButton").assertExists()
    }

    @Test
    fun escribirEmailYPassword_yPresionarLogin() {

        composeTestRule.setContent {
            LoginScreen(
                onLoginSuccess = {},
                onNavigateToRegister = {}
            )
        }

        composeTestRule.onNodeWithTag("emailField")
            .performTextInput("test@gmail.com")

        composeTestRule.onNodeWithTag("passwordField")
            .performTextInput("12345")

        composeTestRule.onNodeWithTag("loginButton")
            .performClick()
    }

    @Test
    fun presionarBotonIrARegistro() {

        var registroClicked = false

        composeTestRule.setContent {
            LoginScreen(
                onLoginSuccess = {},
                onNavigateToRegister = { registroClicked = true }
            )
        }

        composeTestRule.onNodeWithTag("goToRegisterButton")
            .performClick()

        assert(registroClicked)
    }
}
