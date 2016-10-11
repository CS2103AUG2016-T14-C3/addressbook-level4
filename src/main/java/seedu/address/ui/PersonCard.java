package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import seedu.address.model.item.ReadOnlyItem;

public class PersonCard extends UiPart{

    private static final String FXML = "PersonListCard.fxml";

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label itemType;
    @FXML
    private Label endTime;
    @FXML
    private Label endDate;
    @FXML
    private Label startTime;
    @FXML
    private Label startDate;
    @FXML
    private Label tags;

    private ReadOnlyItem person;
    private int displayedIndex;

    public PersonCard(){

    }

    public static PersonCard load(ReadOnlyItem person, int displayedIndex){
        PersonCard card = new PersonCard();
        card.person = person;
        card.displayedIndex = displayedIndex;
        return UiPartLoader.loadUiPart(card);
    }

    @FXML
    public void initialize() {
        name.setText(person.getName().value);
        id.setText(displayedIndex + ". ");
        itemType.setText(person.getItemType().value);
        endTime.setText(person.getEndTime().value);
        endDate.setText(person.getEndDate().value);
        startTime.setText(person.getStartTime().value);
        startDate.setText(person.getStartDate().value);
        tags.setText(person.tagsString());
    }

    public HBox getLayout() {
        return cardPane;
    }

    @Override
    public void setNode(Node node) {
        cardPane = (HBox)node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
}
