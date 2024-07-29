package me.huntifi.castlesiege.misc.mythic;

import io.lumine.mythic.api.skills.IMetaSkill;
import io.lumine.mythic.api.skills.SkillCaster;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.api.skills.SkillTrigger;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.SkillMechanic;
import io.lumine.mythic.core.skills.SkillMetadataImpl;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.objects.Flag;
import org.bukkit.entity.Entity;

import java.util.Random;

public class GoToEnemyFlag implements IMetaSkill {
    @Override
    public SkillResult cast(SkillMetadata skillMetadata) {
        // Get variables
        SkillCaster caster = skillMetadata.getCaster();
        Entity bukkitEntity = BukkitAdapter.adapt(caster.getEntity());
        Random random = new Random();
        Flag flag = MapController.getCurrentMap().flags[random.nextInt(MapController.getCurrentMap().flags.length)];
        System.out.println("Casing GTEF to " + flag.getName());

        // Setup for casting skill
        SkillMetadata meta = new SkillMetadataImpl(SkillTrigger.get("onTimer"), caster, caster.getEntity());
        meta.setLocationTarget(BukkitAdapter.adapt(flag.getSpawnPoint()));
        //meta.getVariables().put();
        SkillMechanic example = new SkillExecutor(MythicBukkit.inst()).getMechanic("teleport");
        System.out.println(example.execute(meta));
        return SkillResult.SUCCESS;
    }
}
