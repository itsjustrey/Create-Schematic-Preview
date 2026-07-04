package dev.titlo10.createschematicpreview;

import com.mojang.logging.LogUtils;

import net.createmod.catnip.config.ConfigBase;
import net.createmod.catnip.config.ui.BaseConfigScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;

import java.util.function.Supplier;

import static dev.titlo10.createschematicpreview.CSPConfig.CONFIG;

@Mod(value = CreateSchematicPreview.MOD_ID, dist = Dist.CLIENT)
public class CreateSchematicPreview {

	public static final String MOD_ID = "createschematicpreview";
	public static final Logger LOGGER = LogUtils.getLogger();

	public CreateSchematicPreview(IEventBus __, ModContainer container) {
		container.registerConfig(ModConfig.Type.CLIENT, CONFIG.specification);
		container.registerExtensionPoint(IConfigScreenFactory.class, (_container, screen)
				-> new BaseConfigScreen(screen, _container.getModId()));

		LOGGER.info("Create: Schematics Preview loaded");
	}


	public static MutableComponent translatable(String path, Object... args) {
		return Component.translatable(MOD_ID + "." + path, args);
	}
}
