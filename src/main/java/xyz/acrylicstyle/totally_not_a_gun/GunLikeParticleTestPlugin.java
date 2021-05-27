package xyz.acrylicstyle.totally_not_a_gun;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class GunLikeParticleTestPlugin extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        if (meta != null && item.getType() == Material.CLAY_BRICK && "pistol".equals(meta.getDisplayName())) {
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, 2, 2);
            Location loc = e.getPlayer().getEyeLocation();
            Vector baseVec = loc.getDirection();
            for (int i = 5; i < 100; i++) {
                Vector vec = baseVec.clone().multiply(i).add(loc.toVector());
                loc.getWorld().spawnParticle(Particle.REDSTONE, vec.getX(), vec.getY(), vec.getZ(), 1);
                boolean doBreak = false;
                for (Entity entity : loc.getWorld().getNearbyEntities(vec.toLocation(loc.getWorld()), 0.5, 0.5, 0.5)) {
                    // damage all entities before breaking
                    if (entity instanceof LivingEntity) {
                        ((Damageable) entity).damage(4);
                        doBreak = true;
                    }
                }
                if (loc.getWorld().getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ()).getType() != Material.AIR) break;
                if (doBreak) {
                    e.getPlayer().playSound(loc, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 2);
                    break;
                }
            }
        }
    }
}
