package ucf.assignments.data;

import java.util.ArrayList;
import ucf.assignments.todo.List;

public class AppMemory {
    private final ArrayList<List> lists;

    public AppMemory() { this.lists = new ArrayList<>(); }

    public List get(int listIndex) { return this.lists.get(listIndex); }
    public ArrayList<List> getAll() { return this.lists; };

public void add(List list) { this.lists.add(list); }

public void remove(int listIndex) {
        if (listIndex < this.lists.size())
            this.lists.remove(listIndex);
}

public int size() { return this.lists.size(); }

public void clear() { this.lists.clear(); }
}
