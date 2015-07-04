# JumpPads #

A small plugin that allows you to easily create configurable jump pads in the world. Only those with permission can create them, preventing exploitation.

## Creating a jump pad ##

1. Place a block to support the pressure plate. This can be whatever block you want.
2. Place any type of pressure plate on top of this block.
3. *Directly* below this block, place a block of gold.
4. Place a sign on this gold block. The side *opposite* the sign is the direction of the jump.
5. Use the layout below:

```
[jump]
DISTANCE
HEIGHT
```

Replace `DISTANCE` and `HEIGHT` on lines 2 and 3 with decimal numbers. These must be greater than 0, however the actual value to distance ratio is a quite unpredictable - best thing is to fiddle, really.

## Permissions ##

`jumppads.create` - Ability to create jump pads