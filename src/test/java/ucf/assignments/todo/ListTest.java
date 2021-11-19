package ucf.assignments.todo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListTest {

    @Test
    void title() {
        /*
        - Create a list with one getTitle. Store this getTitle in a temp variable.
        - Use List.getTitle() and rename the list.
        - Assert that the old temp getTitle and the new List.getTitle are different.
         */
    }

    @Test
    void addItem() {
        /*
        - Create a new empty list.
        - use List.addList x times.
        - Assert that List.items is x length.
         */
    }

    @Test
    void removeItem() {
        /*
        - Create a new list and addList x items to it.
        - Use List.deleteItem() to removeList y items.
        - Assert that the new size of this list is x - y.
         */
    }

    @Test
    void testItemSorting() {
        /*
        - Create a list of x items.
        - Set y items to complete and copy them into a temp array.
        - Assert that List.getCompletedItems returns an array equal to temp array
        - Assert that List.getIncompleteItems returns an array of length x - y.
        - ^^^ Assert that none of the items from the temp array end up here, they should have been removed.
         */
    }
}