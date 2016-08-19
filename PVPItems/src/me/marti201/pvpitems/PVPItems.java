package me.marti201.pvpitems;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.java.JavaPlugin;

public class PVPItems extends JavaPlugin implements Listener {

	List<String> allowed = new ArrayList<String>();
	String message = ChatColor.RED + "You can't hit with that!";

	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);

		saveDefaultConfig();
		allowed = getConfig().getStringList("allowed-items");
		message = ChatColor.translateAlternateColorCodes('&', getConfig().getString("message"));

	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		boolean cancel = false;
		Player damager = null;
		if (e.getDamager().getType() == EntityType.PLAYER)
			damager = (Player) e.getDamager();
		else if (e.getDamager() instanceof Projectile) {
			Projectile proj = (Projectile) e.getDamager();
			if (proj.getShooter() == null || !(proj.getShooter() instanceof Player))
				return;
			damager = (Player) proj.getShooter();
			if (!(proj instanceof Arrow))
				cancel = true;
		} else
			return;

		if (e.getCause() == DamageCause.ENTITY_ATTACK
				&& !allowed.contains(damager.getItemInHand().getType().toString()))
			cancel = true;

		if (cancel) {
			e.setCancelled(true);
			damager.sendMessage(message);
		}

	}

}
