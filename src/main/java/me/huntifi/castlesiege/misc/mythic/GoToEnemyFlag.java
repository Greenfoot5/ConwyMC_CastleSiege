package me.huntifi.castlesiege.misc.mythic;

import io.lumine.mythic.api.skills.*;
import io.lumine.mythic.bukkit.BukkitAPIHelper;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import io.lumine.mythic.core.skills.SkillMetadataImpl;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.objects.Flag;
import org.bukkit.entity.Entity;

import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;

public class GoToEnemyFlag implements IMetaSkill {
    @Override
    public SkillResult cast(SkillMetadata skillMetadata) {
        SkillCaster caster = skillMetadata.getCaster();
        Entity bukkitEntity = BukkitAdapter.adapt(caster.getEntity());
        Random random = new Random();
        Flag flag = MapController.getCurrentMap().flags[random.nextInt(MapController.getCurrentMap().flags.length)];
        System.out.println("Casing GTEF to " + flag.getName());
        SkillMetadata meta = new SkillMetadataImpl(SkillTrigger.get("onSpawn"), caster, caster.getEntity());
        Consumer<SkillMetadata> consumer = metadata -> metadata.setLocationTarget(BukkitAdapter.adapt(flag.getSpawnPoint()));
        System.out.println((new BukkitAPIHelper()).castSkill(bukkitEntity, "goto", consumer));
        return SkillResult.SUCCESS;
    }
}
