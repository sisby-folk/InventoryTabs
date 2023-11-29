package folk.sisby.inventory_tabs.providers;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public abstract class RegistryTabProvider<T> implements TabProvider {
    public final Set<T> values = new HashSet<>();
    public final Map<Identifier, Predicate<T>> matches = new HashMap<>();

    public boolean consumes(T value) {
        if (matches.values().stream().anyMatch(p -> p.test(value))) {
            values.add(value);
            return true;
        }
        return false;
    }

    public abstract int getPriority();

    public boolean isUnique() {
        return false;
    }
}
