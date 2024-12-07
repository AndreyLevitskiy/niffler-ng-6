package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.RegistrationPage;
import org.junit.jupiter.api.Test;

public class RegistrationWebTest {

    private static final Config CFG = Config.getInstance();
    private final MainPage mainPage = new MainPage();
    private final RegistrationPage registrationPage = new RegistrationPage();
    private final Faker faker = new Faker();

    @Test
    void shouldRegisterNewUser() {
        Selenide.open(CFG.frontUrl(), RegistrationPage.class)
                .clickCreateNewAccount()
                .setUsername(faker.name().username())
                .setPassword("12345")
                .setPasswordSubmit("12345")
                .submitRegistration();
        mainPage.blocksStatisticsAndHistoryShouldBe(true);
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        Selenide.open(CFG.frontUrl(), RegistrationPage.class)
                .clickCreateNewAccount()
                .setUsername("duck")
                .setPassword("12345")
                .setPasswordSubmit("12345")
                .submitRegistration();
        registrationPage.checkErrorTextIsVisible();
        mainPage.blocksStatisticsAndHistoryShouldBe(false);
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        Selenide.open(CFG.frontUrl(), RegistrationPage.class)
                .clickCreateNewAccount()
                .setUsername(faker.name().username())
                .setPassword("12345")
                .setPasswordSubmit("54321")
                .submitRegistration();
        registrationPage.checkErrorTextIsForWrongPassword();
        mainPage.blocksStatisticsAndHistoryShouldBe(false);
    }
}
