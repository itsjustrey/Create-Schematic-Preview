package dev.titlo10.createschematicpreview.gui.tooltip;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;

import dev.titlo10.createschematicpreview.CreateSchematicPreview;

@SuppressWarnings("removal")
@Mod.EventBusSubscriber(modid = CreateSchematicPreview.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SchematicTooltipFactory {

	@SubscribeEvent
	static void registerTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event) {
		event.register(SchematicPreviewTooltip.class, ClientSchematicPreviewTooltip::new);
	}
}
