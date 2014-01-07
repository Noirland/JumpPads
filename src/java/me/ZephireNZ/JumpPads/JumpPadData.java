package me.ZephireNZ.JumpPads;

import org.bukkit.block.BlockFace;

public class JumpPadData {

    private double mult;
    private int height;
    private BlockFace direction;

    public JumpPadData(double mult, int height, BlockFace direction) {
        this.mult = mult;
        this.height = height;
        this.direction = direction;
    }

    public double getMult() {
        return mult;
    }

    public int getHeight() {
        return height;
    }

    public BlockFace getDirection() {
        return direction;
    }
}
