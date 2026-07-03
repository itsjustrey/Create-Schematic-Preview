package dev.titlo10.createschematicpreview.gui.tooltip;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;

import dev.titlo10.createschematicpreview.CreateSchematicPreview;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = CreateSchematicPreview.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class SchematicTooltipFactory {

	@SubscribeEvent
	static void registerTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event) {
		event.register(SchematicPreviewTooltip.class, ClientSchematicPreviewTooltip::new);
	}
}
