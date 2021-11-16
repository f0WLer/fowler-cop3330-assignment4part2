package ucf.assignments.todo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ListTest {

    @Test
    void title() {
        /*
        - Create a list with one title. Store this title in a temp variable.
        - Use List.title() and rename the list.
        - Assert that the old temp title and the new List.title are different.
         */
    }

    @Test
    void addItem() {
        /*
        - Create a new empty list.
        - use List.add x times.
        - Assert that List.items is x length.
         */
    }

    @Test
    void removeItem() {
        /*
        - Create a new list and add x items to it.
        - Use List.deleteItem() to remove y items.
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