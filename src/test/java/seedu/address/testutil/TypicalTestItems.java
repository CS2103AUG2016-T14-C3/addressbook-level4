package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.item.*;

/**
 *
 */
public class TypicalTestItems {

    public static TestItem alice, benson, carl, daniel, elle, fiona, george, hoon, ida;

    public TypicalTestItems() {
        try {
            alice =  new ItemBuilder().withItemType("event").withStartDate("2016-06-06").withStartTime("05:00").withEndTime("12:01")
                    .withEndDate("2016-08-08").withName("Alice Pauline")
                    .withTags("friends").build();
            benson = new ItemBuilder().withItemType("deadline").withStartDate("").withStartTime("").withEndTime("01:01")
                    .withEndDate("2016-12-06").withName("Benson Meier")
                    .withTags("owesMoney", "friends").build();
            carl = new ItemBuilder().withItemType("task").withName("Carl Kurz").withStartDate("").withStartTime("").withEndDate("").withEndTime("").build();
            daniel = new ItemBuilder().withItemType("event").withName("Daniel Meier").withStartDate("2015-01-01").withStartTime("00:00").withEndDate("2015-01-01").withEndTime("23:59").build();
            elle = new ItemBuilder().withItemType("deadline").withName("Elle Meyer").withStartDate("").withStartTime("").withEndDate("2019-09-09").withEndTime("10:30").build();
            fiona = new ItemBuilder().withItemType("task").withName("Fiona Kunz").withStartDate("").withStartTime("").withEndDate("").withEndTime("").build();
            george = new ItemBuilder().withItemType("event").withName("George Best").withStartDate("2016-01-01").withStartTime("13:59").withEndDate("2017-01-03").withEndTime("15:00").build();

            //Manually added
            hoon = new ItemBuilder().withItemType("deadline").withName("Hoon Meier").withStartDate("").withStartTime("").withEndDate("2016-09-30").withEndTime("21:14").build();
            ida = new ItemBuilder().withItemType("task").withName("Ida Mueller").withStartDate("").withStartTime("").withEndDate("").withEndTime("").build();
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false : "not possible";
        }
    }

    public static void loadAddressBookWithSampleData(AddressBook ab) {

        try {
            ab.addItem(new Item(alice));
            ab.addItem(new Item(benson));
            ab.addItem(new Item(carl));
            ab.addItem(new Item(daniel));
            ab.addItem(new Item(elle));
            ab.addItem(new Item(fiona));
            ab.addItem(new Item(george));
        } catch (UniquePersonList.DuplicatePersonException e) {
            assert false : "not possible";
        }
    }

    public TestItem[] getTypicalPersons() {
        return new TestItem[]{alice, benson, carl, daniel, elle, fiona, george};
    }

    public AddressBook getTypicalAddressBook(){
        AddressBook ab = new AddressBook();
        loadAddressBookWithSampleData(ab);
        return ab;
    }
}