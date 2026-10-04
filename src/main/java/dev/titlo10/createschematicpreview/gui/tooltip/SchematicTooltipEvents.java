package dev.titlo10.createschematicpreview.gui.tooltip;

import java.util.List;

import com.mojang.datafixers.util.Either;
import com.simibubi.create.AllItems;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import dev.titlo10.createschematicpreview.CreateSchematicPreview;

import static dev.titlo10.createschematicpreview.CSPConfig.CONFIG;
import static dev.titlo10.createschematicpreview.CreateSchematicPreview.translatable;

@Mod.EventBusSubscriber(modid = CreateSchematicPreview.MOD_ID, value = Dist.CLIENT)
public class SchematicTooltipEvents {

	private static final int MIN_PREVIEW_SIZE = 48;
	private static final int SCREEN_EDGE_PADDING = 16;
	private static final int TOOLTIP_TEXT_HEIGHT_ALLOWANCE = 48;

	@SubscribeEvent(priority = EventPriority.LOWEST)
	static void addPreviewHint(ItemTooltipEvent event) {
		if (!CONFIG.previewEnabled.get() || event.getEntity() == null || missingSchematicFile(event.getItemStack())) return;

		String fileName = event.getItemStack().getOrCreateTag().getString("File");
		if (fileName == null || fileName.isEmpty()) return;

		List<Component> tooltip = event.getToolTip();
		int index = previewHintIndex(tooltip, fileName);
		tooltip.add(index, previewHint());
	}

	@SubscribeEvent
	static void addPreviewComponent(RenderTooltipEvent.GatherComponents event) {
		ItemStack stack = event.getItemStack();
		if (!CONFIG.previewEnabled.get() || !Screen.hasAltDown() || missingSchematicFile(stack))
			return;

		String fileName = stack.getOrCreateTag().getString("File");
		if (fileName == null || fileName.isEmpty())
			return;

		int width = Mth.clamp(event.getScreenWidth() - SCREEN_EDGE_PADDING, MIN_PREVIEW_SIZE, CONFIG.sidePanelWidth.get());
		int height = Mth.clamp(event.getScreenHeight() - TOOLTIP_TEXT_HEIGHT_ALLOWANCE, MIN_PREVIEW_SIZE, CONFIG.maxHeight.get());

		List<Either<FormattedText, TooltipComponent>> elements = event.getTooltipElements();
		elements.add(previewComponentIndex(elements), Either.right(new SchematicPreviewTooltip(fileName, width, height)));
		event.setMaxWidth(Math.max(event.getMaxWidth(), width));
	}

	private static boolean missingSchematicFile(ItemStack stack) {
		return stack.isEmpty() || !AllItems.SCHEMATIC.isIn(stack)
				|| !stack.hasTag() || !stack.getTag().contains("File");
	}

	private static Component previewHint() {
		boolean alt = Screen.hasAltDown();
		MutableComponent hint = Component.empty();
		hint.append(translatable("gui.tooltip.hold_preview.prefix")
			.withStyle(ChatFormatting.DARK_GRAY));
		hint.append(translatable("gui.tooltip.key_alt")
			.withStyle(alt ? ChatFormatting.WHITE : ChatFormatting.GRAY));
		hint.append(translatable("gui.tooltip.hold_preview.suffix")
			.withStyle(ChatFormatting.DARK_GRAY));
		return hint;
	}

	private static int previewComponentIndex(List<Either<FormattedText, TooltipComponent>> elements) {
		int index = previewHintElementIndex(elements);
		index = (index == -1) ? Math.min(1, elements.size()) : index + 1;

		if (index < elements.size() && elements.get(index).map(SchematicTooltipEvents::isEmptyText, component -> false))
			index++;
		return index;
	}

	private static int previewHintIndex(List<Component> tooltip, String fileName) {
		for (int i = 1; i < tooltip.size(); i++) {
			if (tooltip.get(i).getString().equals(fileName))
				return i == 1 ? 1 : 2;
		}
		return Math.min(2, tooltip.size());
	}

	private static int previewHintElementIndex(List<Either<FormattedText, TooltipComponent>> elements) {
		for (int i = 1; i < elements.size(); i++) {
			if (elements.get(i).map(SchematicTooltipEvents::isPreviewHint, component -> false))
				return i;
		}
		return -1;
	}

	// TODO - This only works for English
	private static boolean isPreviewHint(FormattedText text) {
		return text instanceof Component component && component.getString().contains("Preview");
	}

	private static boolean isEmptyText(FormattedText text) {
		return text instanceof Component component && component.getString().isEmpty();
	}
}
