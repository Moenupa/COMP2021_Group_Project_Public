package hk.edu.polyu.comp.comp2021.cvfs.model;

@SuppressWarnings("ALL")
public class Disk extends Directory {
    private final int capacity;

    public Disk(int capacity) {
        super("Disk", null);
        this.capacity = capacity;
        setSize(0);
    }

    /**
     * Update the current size of the disk.
     * Print a warning and return if the disk capacity is not enough.
     *
     * @param offset Positive if the size increases, vice versa.
     */
    @Override
    public void updateSizeBy(int offset) {
        if (getSize() + offset > getCapacity()) {
            throw new IllegalStateException("No Enough Space Left.");
        }
        setSize(getSize() + offset);
    }

    /**
     * Safely get the disk size.
     *
     * @return The capacity of the disk.
     */
    public int getCapacity() {
        return capacity;
    }

    @SuppressWarnings("unused")
    @Override
    public StringBuilder getPath() {
        StringBuilder str = new StringBuilder();
        str.append("Disk");
        return str;
    }
}
