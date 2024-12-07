package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegistrationPage {
    private final SelenideElement createNewAccountBtn = $(".form__register");
    private final SelenideElement logInLink = $(".form__link");
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitPasswordInput = $("input[name='passwordSubmit']");
    private final SelenideElement signUpBtn = $("button[type='submit']");
    private final SelenideElement errorText = $(".form__error");

    private final String WRONG_PASSWORD_ERROR_TEXT = "Passwords should be equal";

    public RegistrationPage clickCreateNewAccount() {
        createNewAccountBtn.click();
        return new RegistrationPage();
    }

    public RegistrationPage setUsername(String username) {
        usernameInput.setValue(username);
        return new RegistrationPage();
    }

    public RegistrationPage setPassword(String password) {
        passwordInput.setValue(password);
        return new RegistrationPage();
    }

    public RegistrationPage setPasswordSubmit(String password) {
        submitPasswordInput.setValue(password);
        return new RegistrationPage();
    }

    public LoginPage submitRegistration() {
        signUpBtn.click();
        return new LoginPage();
    }

    public RegistrationPage checkErrorTextIsVisible() {
        errorText.shouldBe(visible);
        return new RegistrationPage();
    }

    public RegistrationPage checkErrorTextIsForWrongPassword() {
        errorText
                .shouldBe(visible)
                .shouldHave(text(WRONG_PASSWORD_ERROR_TEXT));
        return new RegistrationPage();
    }
}
