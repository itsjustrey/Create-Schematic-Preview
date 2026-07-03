package dev.titlo10.createschematicpreview;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

import org.slf4j.Logger;

@Mod(value = CreateSchematicPreview.MODID, dist = Dist.CLIENT)
public class CreateSchematicPreview {

	public static final String MODID = "createschematicpreview";
	private static final Logger LOGGER = LogUtils.getLogger();

	public CreateSchematicPreview(IEventBus __, ModContainer modContainer) {
		modContainer.registerConfig(ModConfig.Type.CLIENT, SchematicPreviewConfig.SPEC);
		LOGGER.info("Create: Schematics Preview loaded");
	}
}
