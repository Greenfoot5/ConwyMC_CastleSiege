package me.huntifi.castlesiege.misc.mythic;

import io.lumine.mythic.api.skills.IMetaSkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.skills.SkillExecutor;
import io.lumine.mythic.core.skills.SkillMechanic;
import io.lumine.mythic.core.skills.SkillMetadataImpl;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.objects.Flag;

import java.util.Random;

public class GoToEnemyFlag implements IMetaSkill {
    @Override
    public SkillResult cast(SkillMetadata skillMetadata) {
        // Get variables
        Random random = new Random();
        Flag flag = MapController.getCurrentMap().flags[random.nextInt(MapController.getCurrentMap().flags.length)];
        System.out.println("Casing GTEF to " + flag.getName());

        //  - clear
        //  - opendoors
        //  - swim
        //  - meleeattack
        //  - lookatplayers
        //  - randomlookaround

        // Setup for casting skill
        System.out.println("Creating Meta");
        SkillMetadata meta = new SkillMetadataImpl(skillMetadata.getCause(), skillMetadata.getCaster(), skillMetadata.getTrigger());
        System.out.println("Creating Goals");
        String[] goals = new String[]{
                "clear",
                "opendoors",
                "swim",
                "meleeattack",
                "lookatplayers",
                "randomlookaround",
        };

        System.out.println("Adding parameter");
        meta.getParameters().put("goal", goals[0]);
        System.out.println("Meta - " + meta.getParameters());
        SkillMechanic aiGoal = new SkillExecutor(MythicBukkit.inst()).getMechanic("runaigoalselector");
        System.out.println(aiGoal.execute(meta));

//        for (String goal : goals) {
//            meta.getParameters().put("goal", goal);
//            meta.setMetadata("goal", goal);
//            System.out.println("Meta - " + meta.getParameters());
//            aiGoal.execute(meta);
//        }

        //meta.getParameters().put("goal", "gotolocation " + flag.getSpawnPoint().x() + "," + flag.getSpawnPoint().y() + "," + flag.getSpawnPoint().z());
        //meta.setLocationTarget(BukkitAdapter.adapt(new Location(Bukkit.getWorld("pirateambush"), 0, 67, -114)));
        //meta.getVariables().put();
        //SkillMechanic example = new SkillExecutor(MythicBukkit.inst()).getMechanic("runaigoalselector");
        //System.out.println(aiGoal.execute(meta));
        return SkillResult.SUCCESS;
    }
}
