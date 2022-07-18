package me.huntifi.castlesiege.maps.bots;

import me.huntifi.castlesiege.kits.items.WoolHat;
import me.huntifi.castlesiege.maps.MapController;
import me.huntifi.castlesiege.maps.bots.BotKits.SwordsmanBot;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.EntityType;

public class CreateBots {

    public static NPC createSwordsmanBot() {

        NamePool.registerNamePool();

        //Creates an npc with a name randomly chosen from the namepool
        NPC newNPC = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, NamePool.getRandomName());
        //The bots are assigned to the right team
        MapController.botJoinsATeam(newNPC);
        //give them their woolhead
        WoolHat.setHead(newNPC);
        //give the NPC their kit (for now this is swordsman)
        SwordsmanBot.setSwordsman(newNPC);

        return newNPC;
    }

}
