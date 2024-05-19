package me.huntifi.castlesiege.misc.mythic;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.skills.conditions.IEntityComparisonCondition;
import me.huntifi.castlesiege.maps.TeamController;
import org.bukkit.entity.Entity;

/**
 * A check to see if two players are on the same team
 */
public class SameTeam implements IEntityComparisonCondition {

    @Override
    public boolean check(AbstractEntity abstractEntity, AbstractEntity abstractTarget) {
        Entity entity = abstractEntity.getBukkitEntity();
        Entity target = abstractTarget.getBukkitEntity();
        return TeamController.getTeam(entity.getUniqueId()) == TeamController.getTeam(target.getUniqueId());
    }
}
