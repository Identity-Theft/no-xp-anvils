package identitytheft.noxpanvils.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class GUI implements ModMenuApi {
    private static Option<Boolean> createOption(String key, boolean def, Supplier<Boolean> getter, Consumer<Boolean> setter)
    {
        return Option.<Boolean>createBuilder()
                .name(Text.translatable(key))
                .binding(def, getter, setter)
                .controller(opt -> BooleanControllerBuilder.create(opt)
                        .formatValue(val -> Text.translatable(val ? "no-xp-anvils.no_xp" : "no-xp-anvils.xp"))
                        .coloured(true)
                )
                .build();
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory()
    {
        return parent -> YetAnotherConfigLib.create(
                Config.HANDLER,
                ((defaults, config, builder) -> builder
                        .title(Text.translatable("no-xp-anvils.title"))
                        .category(ConfigCategory.createBuilder()
                                .name(Text.translatable("no-xp-anvils.title"))
                                .option(createOption("no-xp-anvils.rename", defaults.rename, () -> config.rename, newVal -> config.rename = newVal))
                                .option(createOption("no-xp-anvils.repair", defaults.repair, () -> config.repair, newVal -> config.repair = newVal))
                                .option(createOption("no-xp-anvils.enchant", defaults.enchant, () -> config.enchant, newVal -> config.enchant = newVal))
                                .option(createOption("no-xp-anvils.combineGear", defaults.combineGear, () -> config.combineGear, newVal -> config.combineGear = newVal))
                                .option(createOption("no-xp-anvils.combineBooks", defaults.combineBooks, () -> config.combineBooks, newVal -> config.combineBooks = newVal))
                                .build()
                        )
                )
        ).generateScreen(parent);
    }
}
