package dev.titlo10.createschematicpreview.util;

import com.simibubi.create.CreateClient;
import dev.titlo10.createschematicpreview.CreateSchematicPreview;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Optional;

public class SchematicUtils {

    public static Optional<String> getSchematicNameFromIndex(int schematicIndex) {
        List<Component> availableSchematics = CreateClient.SCHEMATIC_SENDER.getAvailableSchematics();

        int numAvailableSchematics = availableSchematics.size();
        if (schematicIndex < 0 || schematicIndex >= numAvailableSchematics) {
            CreateSchematicPreview.LOGGER.warn("Unable to retrieve a loaded schematic from the following " +
                    "index: {}. Expected range: 0 to {}", schematicIndex, numAvailableSchematics - 1);
            return Optional.empty();
        }
        return Optional.of(availableSchematics.get(schematicIndex).getString());
    }
}
