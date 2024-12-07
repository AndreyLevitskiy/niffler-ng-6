package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
    private final SelenideElement blockStatistics = $("#stat");
    private final SelenideElement blockHistoryOfSpendings = $("#spendings");

    public EditSpendingPage editSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();
        return new EditSpendingPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
    }

    public void blocksStatisticsAndHistoryShouldBe(boolean isVisible) {
        checkVisibility(blockStatistics, isVisible);
        checkVisibility(blockHistoryOfSpendings, isVisible);
    }

    private void checkVisibility(SelenideElement element, boolean isVisible) {
        if (isVisible) {
            element.shouldBe(visible);
        } else {
            element.shouldNotBe(visible);
        }
    }

}
