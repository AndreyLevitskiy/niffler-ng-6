package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class ProfilePage {
    private final SelenideElement profilePic = $("[data-testid='PersonIcon']");
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement nameInput = $("input[name='name']");
    private final SelenideElement saveChangesButton = $("button[type='submit']");
    private final SelenideElement categoryInput = $("input[name='category']");

    public ProfilePage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    public ProfilePage setName(String name) {
        nameInput.setValue(name);
        return this;
    }

    public ProfilePage saveChanges() {
        saveChangesButton.click();
        return this;
    }

    public ProfilePage setCategory(String category) {
        categoryInput.setValue(category);
        return this;
    }
}
