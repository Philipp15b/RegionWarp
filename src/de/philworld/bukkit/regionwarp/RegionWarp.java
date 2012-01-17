package de.philworld.bukkit.regionwarp;

import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

/**
 * Warps a player to regions. 
 * 
 * <h1>Usage</h1>
 * <ul> 
 *  <li>Set the region flag: <code>/region flag {regionId} teleport me</code>. This sets the teleport location to your current position.
 *  <li>Warp to the region with <code>/reg {regionName}</code>.
 *  </ul>
 *  
 *  <h1>Permissions</h1>
 *  <ul>
 *    <li><code>regionwarp.owner</code>: A user can warp to its own regions (where he is the owner).
 *    <li><code>regionwarp.member</code>: A user can warp to regions where he is a member.
 *    <li><code>regionwarp.all</code>: A user can warp to all regions.
 *  </ul>
 * @author Philipp
 *
 */
public class RegionWarp extends JavaPlugin implements CommandExecutor {

	Logger log = Logger.getLogger("Minecraft");
	private WorldGuardPlugin worldGuard; 

	@Override
	public void onDisable() {
		PluginDescriptionFile pff = this.getDescription();
		log.info(pff.getName() +  " " + pff.getVersion() + " is disabled.");
	}

	@Override
	public void onEnable() {		
		PluginManager pm = getServer().getPluginManager();
		if (!pm.isPluginEnabled("WorldGuard")) {
			System.out.println("[RegionWarp] WorldGuard Plugin could not be found.");
			pm.disablePlugin(this);
			return;
		}

		this.worldGuard = ((WorldGuardPlugin) pm.getPlugin("WorldGuard"));

		PluginDescriptionFile pff = this.getDescription();
		log.info(pff.getName() +  " " + pff.getVersion() + " is enabled.");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		// Determine if the sender is a player (and an op), or the console.
		boolean isPlayer  = (sender instanceof Player);

		// Cast the sender to Player if possible.
		Player p = (isPlayer) ? (Player) sender : null;

		// no usage from the console cuz we use the player all the time.
		if(!isPlayer) {
			sender.sendMessage("How could I teleport the console?!");
			return true;
		}

		if (args.length == 0) {
			return false;
		}

		RegionManager regionManager = worldGuard.getGlobalRegionManager().get(p.getWorld());

		ProtectedRegion region = regionManager.getRegion(args[0]);

		if (region == null) {
			p.sendMessage("There is no region with this name.");
			return true;
		}
		
		LocalPlayer wp = worldGuard.wrapPlayer(p);
		
		if(!(p.hasPermission("regionwarp.all") ||
				(p.hasPermission("regionwarp.owner") && region.isOwner(wp)) ||
				(p.hasPermission("regionwarp.member") && region.isMemberOnly(wp)))) {
			p.sendMessage("You are not allowed to teleport to this region!");
			return true;
		}

		Vector teleportVector = (Vector) region.getFlag(DefaultFlag.TELE_LOC);

		if (teleportVector == null)
		{
			p.sendMessage("There is no teleport location for that region set.");
			return true;
		}

		Location teleportLocation = new Location(p.getWorld(), teleportVector.getX(), teleportVector.getY(), teleportVector.getZ());

		p.teleport(teleportLocation);

		p.sendMessage("Warped to region " + region.getId());
		
		return true;
	}

}
