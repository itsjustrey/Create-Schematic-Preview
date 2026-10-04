package dev.titlo10.createschematicpreview;

import com.mojang.logging.LogUtils;

import net.createmod.catnip.config.ConfigBase;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;

import static dev.titlo10.createschematicpreview.CSPConfig.CONFIG;

@Mod(CreateSchematicPreview.MOD_ID)
public class CreateSchematicPreview {

	public static final String MOD_ID = "createschematicpreview";
	public static final Logger LOGGER = LogUtils.getLogger();

	public CreateSchematicPreview() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CONFIG.specification);

		LOGGER.info("Create: Schematics Preview loaded");
	}


	public static MutableComponent translatable(String path, Object... args) {
		return Component.translatable(MOD_ID + "." + path, args);
	}
}
