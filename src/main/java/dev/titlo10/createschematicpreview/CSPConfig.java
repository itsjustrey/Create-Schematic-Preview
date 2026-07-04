package dev.titlo10.createschematicpreview;

import net.createmod.catnip.config.ConfigBase;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@EventBusSubscriber(Dist.CLIENT)
public class CSPConfig extends ConfigBase {

	public static final CSPConfig CONFIG = register(CSPConfig::new);

	public final ConfigBool previewEnabled = b(true, "previewEnabled",
			"Enable 3D schematic preview.");

	public final ConfigInt maxBlockVolume = i(216000, 1,"maxBlockVolume",
			"Maximum schematic bounding-box volume (width * height * length) that will be previewed. " +
					"Larger schematics show a 'too large' message instead, to avoid render hitches.");

	public final ConfigInt sidePanelWidth = i(156, 48, 512, "sidePanelWidth",
			"[In Pixels]",
			"Width of the side preview panel.");

	public final ConfigInt maxHeight = i(156, 48, 512, "panelHeight",
			"[In Pixels]", "Max height of the preview panel.");

	public final ConfigInt loadDelayMs = i(400, 0, 5000, "previewLoadDelay",
			"[In Milliseconds]",
			"How long a schematic must stay highlighted before its 3D preview is built.",
			"This allows schematic previews to be loaded lazily and therefore Schematics you scroll past are never built.",
			"(0 = build immediately on selection).");

	public final ConfigFloat defaultZoom = f(1.25f, 0.5f, 3f, "defaultZoom",
			"Initial zoom amount for a selected schematic");

	public final ConfigFloat defaultYaw = f(45f, -180f, 180f, "defaultYaw",
			"[In Degrees]",
			"Initial horizontal rotation of a selected schematic.");

	public final ConfigFloat defaultPitch = f(30f, -90f, 90f, "defaultPitch",
			"Initial vertical tilt of a selected schematic.");
	@Override
	public @NotNull String getName() {
		return "client";
	}

	private static <T extends ConfigBase> T register(Supplier<T> factory) {
		Pair<T, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(builder -> {
			T config = factory.get();
			config.registerAll(builder);
			return config;
		});
		T config = specPair.getLeft();
		config.specification = specPair.getRight();
		return config;
	}

	@SubscribeEvent
	public static void onLoad(ModConfigEvent.Loading e) {
		if (CONFIG.specification == e.getConfig().getSpec()) CONFIG.onLoad();
	}

	@SubscribeEvent
	public static void onReload(ModConfigEvent.Reloading e) {
		if (CONFIG.specification == e.getConfig().getSpec()) CONFIG.onLoad();
	}
}
