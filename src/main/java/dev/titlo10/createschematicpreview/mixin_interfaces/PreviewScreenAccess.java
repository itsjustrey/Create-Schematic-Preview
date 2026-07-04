package dev.titlo10.createschematicpreview.mixin_interfaces;

import dev.titlo10.createschematicpreview.gui.SchematicPreviewPanel;
import org.jetbrains.annotations.Nullable;

public interface PreviewScreenAccess {
	@Nullable
	SchematicPreviewPanel csp$getPanel();
}
