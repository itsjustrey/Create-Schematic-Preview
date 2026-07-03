package dev.titlo10.createschematicpreview.gui.tooltip;

import dev.titlo10.createschematicpreview.client.SchematicPreviewPanel;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import org.jetbrains.annotations.NotNull;

public class ClientSchematicPreviewTooltip implements ClientTooltipComponent {

	private static final SchematicPreviewPanel PANEL = new SchematicPreviewPanel();

	private final SchematicPreviewTooltip preview;

	public ClientSchematicPreviewTooltip(SchematicPreviewTooltip preview) {
		this.preview = preview;
	}

	@Override
	public int getHeight() {
		return preview.height();
	}

	@Override
	public int getWidth(@NotNull Font font) {
		return preview.width();
	}

	@Override
	public void renderImage(@NotNull Font font, int x, int y, @NotNull GuiGraphics graphics) {
		PANEL.setSelected(preview.fileName());
		PANEL.render(graphics, x, y, preview.width(), preview.height(), -1, -1, 0);
	}
}
