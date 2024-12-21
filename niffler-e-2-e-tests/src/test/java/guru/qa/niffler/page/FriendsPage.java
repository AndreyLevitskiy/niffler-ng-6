package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsPage {

    private final SelenideElement menuItemFriends = $( "[class='link nav-link'][href='/people/friends']");
    private final SelenideElement tabItemAllFriends = $( "[href='/people/all']");
    private final ElementsCollection listFriends = $$("[class='MuiTypography-root MuiTypography-h5 css-1c8s35b'][class='MuiTableRow-root MuiTableRow-hover css-fgzvqs'] p");
    private final ElementsCollection listOfFriendsRequests = $$("#requests tr");
    private final ElementsCollection listOfAllPeople = $$("#all tr");

    private final String WAITING_CHIPS = "[class='MuiChip-root MuiChip-filled MuiChip-sizeMedium MuiChip-colorDefault MuiChip-filledDefault css-bvvzrr']";

    public FriendsPage openFriends() {
        menuItemFriends.click();
        return this;
    }

    public FriendsPage checkFriendExists(String friendName) {
        for (SelenideElement friend : listFriends) {
            if (friend.getText().equals(friendName)) {
                friend.shouldBe(visible.because("Friend with name " + friendName + " should be visible"));
            }
        }
        return this;
    }

    public FriendsPage checkFriendNotExists(String friendName) {
        for (SelenideElement friend : listFriends) {
            if (friend.getText().equals(friendName)) {
                friend.shouldNotBe(visible.because("Friend with name " + friendName + " should not be visible"));
            }
        }
        return this;
    }

    public FriendsPage checkPeopleInIncomeRequests(String income) {
        listOfFriendsRequests.findBy(text(income)).should(exist);
        return this;
    }

    public FriendsPage clickOnTabAllPeople() {
        tabItemAllFriends.click();
        return this;
    }

    public FriendsPage checkOutcomeInvitationInAllPeopleTable(String outcome) {
        SelenideElement personInList =  listOfAllPeople.findBy(text(outcome)).should(exist);
        personInList.$(WAITING_CHIPS).shouldBe(visible);
        return this;
    }
}
