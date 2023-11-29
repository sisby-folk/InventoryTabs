package folk.sisby.inventory_tabs.util;

import com.mojang.datafixers.util.Either;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Holder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public record RegistryValue<T>(Either<Holder<T>, TagKey<T>> value) {
    public Either<T, TagKey<T>> getValue() {
        return this.value.mapLeft(Holder::value);
    }

    public boolean isTag() {
        return this.value.right().isPresent();
    }

    public static @Nullable <T> RegistryValue<T> fromRegistryString(DynamicRegistryManager manager, RegistryKey<? extends Registry<T>> registry, String value) {
        if (value.startsWith("#")) {
            Identifier tagId = Identifier.tryParse(value.substring(1));
            return tagId != null ? new RegistryValue<>(Either.right(TagKey.of(registry, tagId))) : null;
        } else {
            Identifier id = Identifier.tryParse(value);
            if (id == null) return null;
            return manager.getHolderProvider().getLookup(registry).orElseThrow().getHolder(RegistryKey.of(registry, id)).map(h -> new RegistryValue<>(Either.left(h))).orElse(null);
        }
    }

    public String toRegistryString() {
        return value
                .map(
                        holder -> holder.getKey()
                                .map(RegistryKey::getValue)
                                .map(Identifier::toString)
                                .orElse(null),
                        tag -> "#" + tag.id()
                );
    }

    public boolean is(Holder<T> value) {
        return this.value.map((v) -> v.equals(value), value::isIn);
    }
}
