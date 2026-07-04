package dev.titlo10.createschematicpreview.gui.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

public record SchematicPreviewTooltip(String fileName, int width, int height) implements TooltipComponent {}
