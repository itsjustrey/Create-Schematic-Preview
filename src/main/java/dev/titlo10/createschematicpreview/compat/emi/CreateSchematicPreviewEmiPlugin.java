package dev.titlo10.createschematicpreview.compat.emi;

import com.simibubi.create.content.schematics.table.SchematicTableScreen;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.widget.Bounds;

@EmiEntrypoint
public class CreateSchematicPreviewEmiPlugin implements EmiPlugin {

	@Override
	public void register(EmiRegistry registry) {
		registry.addExclusionArea(SchematicTableScreen.class, (screen, consumer) -> {
			for (var area : screen.getExtraAreas())
				consumer.accept(new Bounds(area.getX(), area.getY(), area.getWidth(), area.getHeight()));
		});
	}
}
