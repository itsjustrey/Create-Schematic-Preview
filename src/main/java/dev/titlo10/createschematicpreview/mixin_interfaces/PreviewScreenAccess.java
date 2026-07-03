package dev.titlo10.createschematicpreview.mixin_interfaces;

import dev.titlo10.createschematicpreview.client.SchematicPreviewPanel;
import org.jetbrains.annotations.Nullable;

public interface PreviewScreenAccess {
	@Nullable
	SchematicPreviewPanel createschematicpreview$getPanel();
}
