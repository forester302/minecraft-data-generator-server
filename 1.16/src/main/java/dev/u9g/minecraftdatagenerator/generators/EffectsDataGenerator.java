package dev.u9g.minecraftdatagenerator.generators;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.u9g.minecraftdatagenerator.mixin.StatusEffectAccessor;
import dev.u9g.minecraftdatagenerator.util.DGU;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EffectsDataGenerator implements IDataGenerator {

    public static JsonObject generateEffect(Registry<StatusEffect> registry, StatusEffect statusEffect) {
        JsonObject effectDesc = new JsonObject();
        Identifier registryKey = registry.getKey(statusEffect).orElseThrow().getValue();

        effectDesc.addProperty("id", registry.getRawId(statusEffect));
        if (statusEffect == StatusEffects.UNLUCK) {
            effectDesc.addProperty("name", "BadLuck");
            effectDesc.addProperty("displayName", "Bad Luck");
        } else {
            effectDesc.addProperty("name", Arrays.stream(registryKey.getPath().split("_")).map(StringUtils::capitalize).collect(Collectors.joining()));
            effectDesc.addProperty("displayName", DGU.translateText(statusEffect.getTranslationKey()));
        }

        effectDesc.addProperty("type", ((StatusEffectAccessor) statusEffect).type() == StatusEffectType.BENEFICIAL ? "good" : "bad");
        return effectDesc;
    }

    @Override
    public String getDataName() {
        return "effects";
    }

    @Override
    public JsonArray generateDataJson() {
        JsonArray resultsArray = new JsonArray();
        Registry<StatusEffect> statusEffectRegistry = Registry.STATUS_EFFECT;
        statusEffectRegistry.forEach(effect -> resultsArray.add(generateEffect(statusEffectRegistry, effect)));
        return resultsArray;
    }
}
