package folk.sisby.inventory_tabs.providers;

public class UniqueItemTabProvider extends ItemTabProvider {
    @Override
    public int getPriority() {
        return 40;
    }

    @Override
    public boolean isUnique() {
        return true;
    }
}
