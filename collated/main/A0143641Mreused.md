# A0143641Mreused
###### \java\seedu\taskmanager\model\item\UniqueItemList.java
``` java
    /**
     * Removes the equivalent item from the list.
     *
     * @throws ItemNotFoundException if no such item could be found in the list.
     */
    public boolean remove(ReadOnlyItem toRemove) throws ItemNotFoundException {
        assert toRemove != null;
        final boolean itemFoundAndDeleted = internalList.remove(toRemove);
        if (!itemFoundAndDeleted) {
            throw new ItemNotFoundException();
        }
        return itemFoundAndDeleted;
    }
    
```
###### \java\seedu\taskmanager\model\TaskManager.java
``` java
    public boolean removeItem(ReadOnlyItem key) throws UniqueItemList.ItemNotFoundException {
        if (items.remove(key)) {
            return true;
        } else {
            throw new UniqueItemList.ItemNotFoundException();
        }
    }

```
