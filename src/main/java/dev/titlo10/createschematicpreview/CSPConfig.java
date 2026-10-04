package dev.titlo10.createschematicpreview;

import net.minecraftforge.common.ForgeConfigSpec;

public final class CSPConfig {

	public static final CSPConfig CONFIG = new CSPConfig();

	public final ConfigBool previewEnabled;
	public final ConfigInt maxBlockVolume;
	public final ConfigInt sidePanelWidth;
	public final ConfigInt maxHeight;
	public final ConfigInt loadDelayMs;
	public final ConfigFloat defaultZoom;
	public final ConfigFloat defaultYaw;
	public final ConfigFloat defaultPitch;
	public final ForgeConfigSpec specification;

	private CSPConfig() {
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		builder.push("client");
		previewEnabled = new ConfigBool(builder.comment("Enable 3D schematic preview.")
				.define("previewEnabled", true));
		maxBlockVolume = new ConfigInt(builder.comment(
				"Maximum schematic bounding-box volume (width * height * length) that will be previewed.")
				.defineInRange("maxBlockVolume", 216000, 1, Integer.MAX_VALUE));
		sidePanelWidth = new ConfigInt(builder.comment("[In Pixels] Width of the side preview panel.")
				.defineInRange("sidePanelWidth", 156, 48, 512));
		maxHeight = new ConfigInt(builder.comment("[In Pixels] Max height of the preview panel.")
				.defineInRange("panelHeight", 156, 48, 512));
		loadDelayMs = new ConfigInt(builder.comment("[In Milliseconds] How long a schematic must stay highlighted before its 3D preview is built.")
				.defineInRange("previewLoadDelay", 400, 0, 5000));
		defaultZoom = new ConfigFloat(builder.comment("Initial zoom amount for a selected schematic.")
				.defineInRange("defaultZoom", 1.25f, 0.5f, 3f));
		defaultYaw = new ConfigFloat(builder.comment("[In Degrees] Initial horizontal rotation of a selected schematic.")
				.defineInRange("defaultYaw", 45f, -180f, 180f));
		defaultPitch = new ConfigFloat(builder.comment("[In Degrees] Initial vertical tilt of a selected schematic.")
				.defineInRange("defaultPitch", 30f, -90f, 90f));
		builder.pop();
		specification = builder.build();
	}

	public record ConfigBool(ForgeConfigSpec.BooleanValue value) {
		public boolean get() {
			return value.get();
		}
	}

	public record ConfigInt(ForgeConfigSpec.IntValue value) {
		public int get() {
			return value.get();
		}
	}

	public record ConfigFloat(ForgeConfigSpec.DoubleValue value) {
		public float getF() {
			return value.get().floatValue();
		}
	}
}
