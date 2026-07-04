package dev.titlo10.createschematicpreview.mixin;

import com.simibubi.create.content.schematics.table.SchematicTableScreen;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.widget.Label;
import com.simibubi.create.foundation.gui.widget.ScrollInput;

import dev.titlo10.createschematicpreview.util.SchematicUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.util.Mth;

import dev.titlo10.createschematicpreview.mixin_interfaces.PreviewScreenAccess;
import dev.titlo10.createschematicpreview.gui.SchematicPreviewPanel;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

import static dev.titlo10.createschematicpreview.CSPConfig.CONFIG;

@Mixin(SchematicTableScreen.class)
public abstract class SchematicTableScreenMixin implements PreviewScreenAccess {

	@Unique private static final int csp$PANEL_GAP = 4;
	@Unique private static final int csp$SCREEN_MARGIN = 6;
	@Unique private static final int csp$MIN_PANEL_SIZE = 60;

	@Shadow private ScrollInput schematicsArea;
	@Shadow private Label schematicsLabel;
	@Shadow protected AllGuiTextures background;

	@Unique private SchematicPreviewPanel csp$panel;

	@Override
	@Nullable
	public SchematicPreviewPanel csp$getPanel() {
		return csp$panel;
	}

	@Inject(method = "init", at = @At("TAIL"))
	private void csp$initPanel(CallbackInfo ci) {
		csp$panel = new SchematicPreviewPanel();
	}

	@Inject(method = "renderBg", at = @At("TAIL"))
	private void csp$renderPanel(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY,
	                             CallbackInfo ci) {
		if (!CONFIG.previewEnabled.get() || csp$panel == null) return;

		var self = (SchematicTableScreen) (Object) this;
		Minecraft mc = Minecraft.getInstance();

		if (schematicsArea != null)
			SchematicUtils.getSchematicNameFromIndex(schematicsArea.getState())
					.ifPresent(csp$panel::setSelected);

		int screenW = mc.getWindow().getGuiScaledWidth();
		int screenH = mc.getWindow().getGuiScaledHeight();
		int availableW = screenW - csp$SCREEN_MARGIN * 2;
		int availableH = screenH - csp$SCREEN_MARGIN * 2;

		int panelW = Math.clamp(availableW, 1, CONFIG.sidePanelWidth.get());
		int panelH = Math.clamp(availableH, 1, CONFIG.maxHeight.get());
		int minPanelW = Math.min(csp$MIN_PANEL_SIZE, panelW);
		int minPanelH = Math.min(csp$MIN_PANEL_SIZE, panelH);
		int leftPos = self.getGuiLeft();
		int topPos = self.getGuiTop();

		int occupiedLeft = leftPos;
		int occupiedTop = topPos;
		int occupiedRight = leftPos + background.getWidth();
		int occupiedBottom = topPos + background.getHeight() + 4 + AllGuiTextures.PLAYER_INVENTORY.getHeight();

		for (Rect2i area : self.getExtraAreas()) {
			occupiedLeft = Math.min(occupiedLeft, area.getX());
			occupiedTop = Math.min(occupiedTop, area.getY());
			occupiedRight = Math.max(occupiedRight, area.getX() + area.getWidth());
			occupiedBottom = Math.max(occupiedBottom, area.getY() + area.getHeight());
		}

		int leftRoom = Math.max(0, occupiedLeft - csp$PANEL_GAP - csp$SCREEN_MARGIN);
		int rightRoom = Math.max(0, screenW - csp$SCREEN_MARGIN - occupiedRight - csp$PANEL_GAP);
		int aboveRoom = Math.max(0, occupiedTop - csp$PANEL_GAP - csp$SCREEN_MARGIN);
		int belowRoom = Math.max(0, screenH - csp$SCREEN_MARGIN - occupiedBottom - csp$PANEL_GAP);

		int px, py;
		if (leftRoom >= panelW) {
			px = occupiedLeft - csp$PANEL_GAP - panelW;
			py = csp$clamp(topPos, csp$SCREEN_MARGIN, screenH - csp$SCREEN_MARGIN - panelH);
		} else if (belowRoom >= minPanelH || aboveRoom >= minPanelH) {
			boolean useBelow = belowRoom >= minPanelH && (belowRoom >= aboveRoom || aboveRoom < minPanelH);
			int verticalRoom = useBelow ? belowRoom : aboveRoom;

			panelW = 204;
			occupiedLeft = occupiedLeft - 54;

			panelH = Math.min(panelH, verticalRoom);
			px = csp$clamp((occupiedLeft + occupiedRight - panelW) / 2, csp$SCREEN_MARGIN,
					screenW - csp$SCREEN_MARGIN - panelW);
			py = useBelow ? occupiedBottom + csp$PANEL_GAP : occupiedTop - csp$PANEL_GAP - panelH;
		} else {
			int sideRoom = Math.max(leftRoom, rightRoom);
			if (sideRoom < minPanelW) return;

			panelW = sideRoom;
			py = csp$clamp(topPos, csp$SCREEN_MARGIN,
				screenH - csp$SCREEN_MARGIN - panelH);
			px = leftRoom >= rightRoom ? occupiedLeft - csp$PANEL_GAP - panelW
				: occupiedRight + csp$PANEL_GAP;
		}

		long window = mc.getWindow().getWindow();
		boolean leftDown = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;

		csp$panel.updateMouse(mouseX, mouseY, leftDown);
		csp$panel.render(graphics, px, py, panelW, panelH, mouseX, mouseY, partialTicks);
	}

	@Unique
	private int csp$clamp(int value, int min, int max) {
		if (max < min) return min;
		return Mth.clamp(value, min, max);
	}
}
