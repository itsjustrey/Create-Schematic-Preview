package dev.titlo10.createschematicpreview.gui;

import dev.titlo10.createschematicpreview.mixin_interfaces.PreviewScreenAccess;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.client.event.ScreenEvent;

import dev.titlo10.createschematicpreview.CreateSchematicPreview;

import static dev.titlo10.createschematicpreview.CSPConfig.CONFIG;

@Mod.EventBusSubscriber(modid = CreateSchematicPreview.MOD_ID, value = Dist.CLIENT)
public class PreviewInputHandler {

	@SubscribeEvent
	static void onMouseScrolled(ScreenEvent.MouseScrolled.Pre event) {
		if (!CONFIG.previewEnabled.get()) return;
		if (!(event.getScreen() instanceof PreviewScreenAccess access)) return;

		SchematicPreviewPanel panel = access.csp$getPanel();
		if (panel != null && panel.isMouseOver(event.getMouseX(), event.getMouseY())) {
			panel.onScroll(event.getScrollDelta());
			event.setCanceled(true);
		}
	}
}
