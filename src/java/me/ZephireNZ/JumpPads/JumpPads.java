package me.ZephireNZ.JumpPads;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class JumpPads extends JavaPlugin implements Listener {

    boolean debug = true;
    List<Material> plates = Arrays.asList(Material.STONE_PLATE, Material.WOOD_PLATE, Material.IRON_PLATE, Material.GOLD_PLATE);

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);


    }

    @Override
    public void onDisable() {

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerPressurePlate(PlayerInteractEvent event) {
        if(event.getAction() != Action.PHYSICAL) return;
        Block block = event.getClickedBlock();
        if ((!plates.contains(block.getType()))) return;

        Block blockUnder = block.getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN);
        if (blockUnder.getType() != Material.GOLD_BLOCK) return;

        Sign s = checkForJumpSign(blockUnder);
        JumpPadData data = parseSign(s);
        jump(event.getPlayer(), data);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityPressurePlate(EntityInteractEvent event) {
        if(event.getEntityType() == EntityType.PLAYER) return;
        Block block = event.getBlock();
        if ((!plates.contains(block.getType()))) return;

        Block blockUnder = block.getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN);
        if (blockUnder.getType() != Material.GOLD_BLOCK) return;

        Sign s = checkForJumpSign(blockUnder);
        JumpPadData data = parseSign(s);
        jump(event.getEntity(), data);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSignPlace(SignChangeEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        if(ChatColor.stripColor(event.getLine(0)).equalsIgnoreCase("[jump]")) {
            if(!player.hasPermission("jumppads.create")) {
                event.setCancelled(true);
                block.breakNaturally();
                player.sendMessage(ChatColor.DARK_RED + "You do not have permission to create JumpPads.");

            }
        }
    }

    public JumpPadData parseSign(Sign sign) {
        if (sign == null) return null;

        Double mult;
        int height;
        try{
            mult = Double.parseDouble(sign.getLine(1));
        }catch(NumberFormatException e) {
            mult = 1.0;
        }
        try{
            height = Integer.parseInt(sign.getLine(2));
        }catch(NumberFormatException e) {
            height = 10;
        }
        org.bukkit.material.Sign sMat = (org.bukkit.material.Sign) sign.getBlock().getState().getData();
        BlockFace facing = sMat.getAttachedFace();

        return new JumpPadData(mult, height, facing);
    }

    public Sign checkForJumpSign(Block block) {
        for(BlockFace face : BlockFace.values()) {
            if(block.getRelative(face).getType() == Material.WALL_SIGN) {
                Sign s = (Sign) block.getRelative(face).getState();
                if(ChatColor.stripColor(s.getLine(0)).equalsIgnoreCase("[jump]")) {
                    return s;
                }
            }
        }
        return null;

    }

    public void jump(Entity entity, JumpPadData data) {
        Location pLoc = entity.getLocation();
        Location newLoc = entity.getLocation();

        switch(data.getDirection()) {
            case NORTH:
                newLoc.add(0, 0, -data.getMult());
                break;
            case EAST:
                newLoc.add(data.getMult(), 0, 0);
                break;
            case SOUTH:
                newLoc.add(0, 0, data.getMult());
                break;
            case WEST:
                newLoc.add(-data.getMult(), 0, 0);
                break;
            default:
                return;
        }

        Location loc = entity.getLocation();
        loc.add(0, 0.5, 0);
        entity.teleport(loc);
        entity.setVelocity(VelocityUtil.calculateVelocity(pLoc.toVector(), newLoc.toVector(), data.getHeight()));
    }
}

