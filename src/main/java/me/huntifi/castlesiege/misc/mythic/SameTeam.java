package me.huntifi.castlesiege.misc.mythic;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.skills.conditions.IEntityComparisonCondition;
import me.huntifi.castlesiege.maps.TeamController;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class SameTeam implements IEntityComparisonCondition {

    @Override
    public boolean check(AbstractEntity abstractEntity, AbstractEntity abstractTarget) {
        Entity entity = abstractEntity.getBukkitEntity();
        Entity target = abstractTarget.getBukkitEntity();
        if (entity instanceof Player && target instanceof Player) {
            Player caster = (Player) entity;
            Player targeted = (Player) target;
            return TeamController.getTeam(caster.getUniqueId()) == TeamController.getTeam(targeted.getUniqueId());
        } else {
            return false;
        }
    }
}
