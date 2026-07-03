package dev.titlo10.createschematicpreview.client;

import dev.titlo10.createschematicpreview.mixin_interfaces.PreviewScreenAccess;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

import dev.titlo10.createschematicpreview.SchematicPreviewConfig;
import dev.titlo10.createschematicpreview.CreateSchematicPreview;

@EventBusSubscriber(modid = CreateSchematicPreview.MODID, value = Dist.CLIENT)
public class PreviewInputHandler {

	@SubscribeEvent
	static void onMouseScrolled(ScreenEvent.MouseScrolled.Pre event) {
		if (!SchematicPreviewConfig.previewEnabled)
			return;
		if (!(event.getScreen() instanceof PreviewScreenAccess access))
			return;

		SchematicPreviewPanel panel = access.createschematicpreview$getPanel();
		if (panel != null && panel.isMouseOver(event.getMouseX(), event.getMouseY())) {
			panel.onScroll(event.getScrollDeltaY());
			event.setCanceled(true);
		}
	}
}
