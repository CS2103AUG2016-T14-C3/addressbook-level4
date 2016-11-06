package seedu.taskmanager.ui;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import seedu.taskmanager.logic.commands.DoneCommand;
import seedu.taskmanager.logic.commands.NotDoneCommand;
import seedu.taskmanager.logic.Logic;
import seedu.taskmanager.logic.commands.DeleteCommand;
import seedu.taskmanager.model.item.ItemType;
import seedu.taskmanager.model.item.ReadOnlyItem;

//@@author A0065571A
public class ShortItemCard extends UiPart{

    private static final String FXML = "ShortItemListCard.fxml";

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label tags;

    private ReadOnlyItem item;
    @FXML
    private Button doneButton;
    @FXML
    private Button deleteButton;

    private int displayedIndex;
    private Logic logic;
    public static final String DONE_TEXT = "Done";
    public static final String UNDONE_TEXT = "Not Done";
    
    @FXML
    public void doneButtonAction(ActionEvent event) {
        Button button = (Button) event.getSource();
        if (button.getText().equals(UNDONE_TEXT)) {
            button.setText(DONE_TEXT);
            logic.execute(DoneCommand.COMMAND_WORD + " " + displayedIndex);
        } else {
            button.setText(UNDONE_TEXT);
            logic.execute(NotDoneCommand.COMMAND_WORD + " " + displayedIndex);
        }
    }
    
    @FXML
    public void deleteButtonAction(ActionEvent event) {
        logic.execute(DeleteCommand.COMMAND_WORD + " " + displayedIndex);
    }

    public ShortItemCard(){

    }

    public static ShortItemCard load(ReadOnlyItem item, int displayedIndex, Logic logic){
        ShortItemCard card = new ShortItemCard();
        card.item = item;
        card.displayedIndex = displayedIndex;
        card.configure(logic);
        return UiPartLoader.loadUiPart(card);
    }
    
    public void configure(Logic logic) {
        this.logic = logic;
    }

    @FXML
    public void initialize() {
        name.setText(item.getName().value);
        id.setText(displayedIndex + ". ");
        tags.setText(item.tagsString());
        
        setColour();
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
    
    //@@author A0143641M
    private void setColour() {
        if (item.getDone()) {
            id.setStyle("-fx-text-fill: #2E8B57");
            name.setStyle("-fx-text-fill: #2E8B57");
            tags.setStyle("-fx-text-fill: #2E8B57");
        } else if (item.getItemType().value.equals(ItemType.DEADLINE_WORD) || item.getItemType().value.equals(ItemType.EVENT_WORD)) {
            if (item.isNearDeadline()) { // 24 Hours Before End Date
                id.setStyle("-fx-text-fill: #E67E00");
                name.setStyle("-fx-text-fill: #E67E00");
                tags.setStyle("-fx-text-fill: #E67E00");
            }
            if (item.isPastDeadline()) { // Past End Date
                id.setStyle("-fx-text-fill: #FF0000");
                name.setStyle("-fx-text-fill: #FF0000");
                tags.setStyle("-fx-text-fill: #FF0000");
            }
        }
    }
}
