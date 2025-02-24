package me.greenfoot5.castlesiege.kits.kits.sign_kits;

import com.destroystokyo.paper.profile.PlayerProfile;
import me.greenfoot5.castlesiege.kits.items.CSItemCreator;
import me.greenfoot5.castlesiege.kits.items.EquipmentSet;
import me.greenfoot5.castlesiege.kits.kits.SignKit;
import me.greenfoot5.conwymc.data_types.Tuple;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Artillerist kit
 */
public class Artillerist extends SignKit {

    private static final int health = 320;
    private static final double regen = 10.5;
    private static final double meleeDamage = 36;
    private static final int ladderCount = 4;
    private static final int ballCount = 14;
    private static final int powderCount = 56;

    /**
     * Creates a new artillerist
     */
    public Artillerist() {
        super("Artillerist", health, regen, Material.FIREWORK_STAR);

        // Equipment Stuff
        EquipmentSet es = new EquipmentSet();

        // Weapon
        es.hotbar[0] = CSItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
                Component.text("Artillerist's sword", NamedTextColor.GREEN), null, null, meleeDamage);
        // Voted Weapon
        es.votedWeapon = new Tuple<>(
                CSItemCreator.weapon(new ItemStack(Material.STONE_SWORD),
                        Component.text("Artillerist's sword", NamedTextColor.GREEN),
                        Collections.singletonList(Component.text("⁎ Voted: +2 damage", NamedTextColor.AQUA)),
                        Collections.singletonList(new Tuple<>(Enchantment.LOOTING, 0)), meleeDamage + 2),
                0);

        // Chestplate
        es.chest = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_CHESTPLATE),
                Component.text("Leather Leggings", NamedTextColor.GREEN), null, null,
                Color.fromRGB(55, 48, 48));

        // Leggings
        es.legs = CSItemCreator.leatherArmor(new ItemStack(Material.LEATHER_LEGGINGS),
                Component.text("Leather Leggings", NamedTextColor.GREEN), null, null,
                Color.fromRGB(95, 58, 8));

        // Boots
        es.feet = CSItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Reinforced-Iron Boots", NamedTextColor.GREEN), null, null);
        // Voted Boots
        es.votedFeet = CSItemCreator.item(new ItemStack(Material.NETHERITE_BOOTS),
                Component.text("Reinforced-Iron Boots", NamedTextColor.GREEN),
                Collections.singletonList(Component.text("⁎ Voted: Depth Strider II", NamedTextColor.AQUA)),
                Collections.singletonList(new Tuple<>(Enchantment.DEPTH_STRIDER, 2)));

        // Ladders
        es.hotbar[1] = new ItemStack(Material.LADDER, ladderCount);
        es.votedLadders = new Tuple<>(new ItemStack(Material.LADDER, ladderCount + 2), 1);

        //cannonballs
        ItemStack ball = CSItemCreator.item(new ItemStack(Material.PLAYER_HEAD, ballCount),
                Component.text("Cannon Ball", NamedTextColor.GOLD), null, null);
        SkullMeta skullMeta = (SkullMeta) ball.getItemMeta();
        PlayerProfile profile = Bukkit.getServer().createProfile("Cannonball");
        try {
            PlayerTextures textures = profile.getTextures();
            textures.setSkin(URI.create("https://textures.minecraft.net/texture/17d81286281b1c7b13420f70b0f43867aa38eee109b64c3a37b699ea05e8ec05").toURL());
            profile.setTextures(textures);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        skullMeta.setPlayerProfile(profile);
        ball.setItemMeta(skullMeta);
        es.hotbar[2] = ball;

        //cannonballs
        es.hotbar[3] = new ItemStack(Material.GUNPOWDER, powderCount);

        super.equipment = es;

        // Perm Potion Effect
        super.potionEffects.add(new PotionEffect(PotionEffectType.JUMP_BOOST, 999999, 0, true, false));

    }

    @Override
    public ArrayList<Component> getGuiDescription() {
        ArrayList<Component> kitLore = new ArrayList<>();
        kitLore.add(Component.text("Holds the knowledge (and items)", NamedTextColor.GRAY));
        kitLore.add(Component.text("to reload cannons safely", NamedTextColor.GRAY));
        kitLore.addAll(getBaseStats(health, regen, meleeDamage, ladderCount));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Effects:", NamedTextColor.DARK_PURPLE));
        kitLore.add(Component.text("- Jump Boost I", NamedTextColor.GRAY));
        kitLore.add(Component.empty());
        kitLore.add(Component.text("Active:", NamedTextColor.GOLD));
        kitLore.add(Component.text("- Can reload cannons", NamedTextColor.GRAY));
        return kitLore;
    }
}
