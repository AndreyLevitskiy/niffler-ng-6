package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {
    private final SelenideElement profilePic = $("[data-testid='PersonIcon']");
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement nameInput = $("input[name='name']");
    private final SelenideElement userName = $("#username");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement categoryInput = $("input[name='category']");
    private final SelenideElement archivedSwitcher = $(".MuiSwitch-input");
    private final ElementsCollection categoryChips = $$(".MuiChip-filled.MuiChip-colorPrimary");
    private final ElementsCollection categoryChipsArchived = $$(".MuiChip-filled.MuiChip-colorDefault");

    public ProfilePage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    public ProfilePage setName(String name) {
        nameInput.setValue(name);
        return this;
    }

    public ProfilePage checkActiveCategoryExists(String category) {
        categoryChips.find(text(category))
                .shouldBe(visible);
        return this;
    }

    public ProfilePage checkArchivedCategoryExists(String category) {
        archivedSwitcher.click();
        categoryChipsArchived.find(text(category))
                .shouldBe(visible);
        return this;
    }
}
