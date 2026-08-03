package dev.titlo10.createschematicpreview.mixin;

import com.simibubi.create.content.schematics.table.SchematicTableScreen;
import com.simibubi.create.foundation.gui.AllGuiTextures;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

import static dev.titlo10.createschematicpreview.CSPConfig.CONFIG;

@Mixin(SchematicTableScreen.class)
public abstract class SchematicTableScreenMixin implements PreviewScreenAccess {

	@Unique private static final int csp$PANEL_GAP = 4;
	@Unique private static final int csp$SCREEN_MARGIN = 6;
	@Unique private static final int csp$MIN_PANEL_SIZE = 60;

	@Shadow protected AllGuiTextures background;
	@Shadow private ScrollInput schematicsArea;
	@Shadow private List<Rect2i> extraAreas;
	@Unique private SchematicPreviewPanel csp$panel;
	@Unique @Nullable private Rect2i csp$previewArea;

	@Override
	@Nullable
	public SchematicPreviewPanel csp$getPanel() {
		return csp$panel;
	}

	@Inject(method = "init", at = @At("TAIL"))
	private void csp$initPanel(CallbackInfo ci) {
		csp$panel = new SchematicPreviewPanel();
		csp$previewArea = csp$calculatePreviewArea();
	}

	@Inject(method = "renderBg", at = @At("TAIL"))
	private void csp$renderPanel(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY,
	                             CallbackInfo ci) {
		csp$previewArea = csp$calculatePreviewArea();
		if (csp$previewArea == null || csp$panel == null) return;

		Minecraft mc = Minecraft.getInstance();

		if (schematicsArea != null)
			SchematicUtils.getSchematicNameFromIndex(schematicsArea.getState())
					.ifPresent(csp$panel::setSelected);

		long window = mc.getWindow().getWindow();
		boolean leftDown = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;

		csp$panel.updateMouse(mouseX, mouseY, leftDown);
		csp$panel.render(graphics, csp$previewArea.getX(), csp$previewArea.getY(),
				csp$previewArea.getWidth(), csp$previewArea.getHeight(), mouseX, mouseY, partialTicks);
	}

	@Inject(method = "getExtraAreas", at = @At("RETURN"), cancellable = true)
	private void csp$includePreviewArea(CallbackInfoReturnable<List<Rect2i>> cir) {
		if (csp$previewArea == null) return;

		List<Rect2i> areas = new ArrayList<>(cir.getReturnValue());
		areas.add(csp$previewArea);
		cir.setReturnValue(List.copyOf(areas));
	}

	@Unique
	@Nullable
	private Rect2i csp$calculatePreviewArea() {
		if (!CONFIG.previewEnabled.get()) return null;

		var self = (SchematicTableScreen) (Object) this;
		Minecraft mc = Minecraft.getInstance();
		int screenW = mc.getWindow().getGuiScaledWidth();
		int screenH = mc.getWindow().getGuiScaledHeight();
		int availableW = screenW - csp$SCREEN_MARGIN * 2;
		int availableH = screenH - csp$SCREEN_MARGIN * 2;

		int panelW = Math.clamp(availableW, 1, CONFIG.sidePanelWidth.get());
		int panelH = Math.clamp(availableH, 1, CONFIG.maxHeight.get());
		int minPanelH = Math.min(csp$MIN_PANEL_SIZE, panelH);
		int leftPos = self.getGuiLeft();
		int topPos = self.getGuiTop();

		int occupiedLeft = leftPos;
		int occupiedTop = topPos;
		int occupiedRight = leftPos + background.getWidth();
		int occupiedBottom = topPos + background.getHeight() + 4 + AllGuiTextures.PLAYER_INVENTORY.getHeight();

		for (Rect2i area : extraAreas) {
			occupiedLeft = Math.min(occupiedLeft, area.getX());
			occupiedTop = Math.min(occupiedTop, area.getY());
			occupiedRight = Math.max(occupiedRight, area.getX() + area.getWidth());
			occupiedBottom = Math.max(occupiedBottom, area.getY() + area.getHeight());
		}

		int leftRoom = Math.max(0, occupiedLeft - csp$PANEL_GAP - csp$SCREEN_MARGIN);
		int aboveRoom = Math.max(0, occupiedTop - csp$PANEL_GAP - csp$SCREEN_MARGIN);
		int belowRoom = Math.max(0, screenH - csp$SCREEN_MARGIN - occupiedBottom - csp$PANEL_GAP);

		int px;
		int py;
		if (leftRoom >= panelW) {
			px = occupiedLeft - csp$PANEL_GAP - panelW;
			py = csp$clamp(topPos, csp$SCREEN_MARGIN, screenH - csp$SCREEN_MARGIN - panelH);
		} else if (belowRoom >= minPanelH || aboveRoom >= minPanelH) {
			panelW = 204;
			occupiedLeft -= 54;

			panelH = Math.min(panelH, aboveRoom);
			px = csp$clamp((occupiedLeft + occupiedRight - panelW) / 2, csp$SCREEN_MARGIN,
					screenW - csp$SCREEN_MARGIN - panelW);
			py = occupiedTop - csp$PANEL_GAP - panelH;
		} else {
			return null;
		}

		return new Rect2i(px, py, panelW, panelH);
	}

	@Unique
	private int csp$clamp(int value, int min, int max) {
		if (max < min) return min;
		return Mth.clamp(value, min, max);
	}
}
