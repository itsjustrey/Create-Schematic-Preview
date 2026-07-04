package dev.titlo10.createschematicpreview.gui;

import dev.titlo10.createschematicpreview.mixin_interfaces.PreviewScreenAccess;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

import dev.titlo10.createschematicpreview.CreateSchematicPreview;

import static dev.titlo10.createschematicpreview.CSPConfig.CONFIG;

@EventBusSubscriber(modid = CreateSchematicPreview.MOD_ID, value = Dist.CLIENT)
public class PreviewInputHandler {

	@SubscribeEvent
	static void onMouseScrolled(ScreenEvent.MouseScrolled.Pre event) {
		if (!CONFIG.previewEnabled.get()) return;
		if (!(event.getScreen() instanceof PreviewScreenAccess access)) return;

		SchematicPreviewPanel panel = access.csp$getPanel();
		if (panel != null && panel.isMouseOver(event.getMouseX(), event.getMouseY())) {
			panel.onScroll(event.getScrollDeltaY());
			event.setCanceled(true);
		}
	}
}
