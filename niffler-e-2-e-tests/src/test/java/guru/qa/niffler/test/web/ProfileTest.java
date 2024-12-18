package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(UsersQueueExtension.class)
public class ProfileTest {

    private static final Config CFG = Config.getInstance();
    private final MainPage mainPage = new MainPage();

    @Disabled
    @Category(
            username = "duck",
            archived = true
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345");
        mainPage.blocksStatisticsAndHistoryShouldBe(true);

        Selenide.open(CFG.frontUrl() + "profile", ProfilePage.class)
                .checkArchivedCategoryExists(category.name());
    }

    @Disabled
    @Category(
            username = "duck",
            archived = false
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login("duck", "12345");
        mainPage.blocksStatisticsAndHistoryShouldBe(true);

        Selenide.open(CFG.frontUrl() + "profile", ProfilePage.class)
                .checkActiveCategoryExists(category.name());
    }
}
