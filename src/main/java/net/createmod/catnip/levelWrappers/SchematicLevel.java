package net.createmod.catnip.levelWrappers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

/**
 * Compatibility bridge for Create 6.0.8, whose renderer references the
 * pre-relocation Catnip package name.
 */
public class SchematicLevel extends net.createmod.catnip.utility.levelWrappers.WrappedLevel {

	private final net.createmod.catnip.utility.levelWrappers.SchematicLevel delegate;
	public final BlockPos anchor;
	public boolean renderMode;

	public SchematicLevel(net.createmod.catnip.utility.levelWrappers.SchematicLevel delegate) {
		super(delegate);
		this.delegate = delegate;
		this.anchor = delegate.anchor;
	}

	public BoundingBox getBounds() {
		return delegate.getBounds();
	}

	public Iterable<BlockEntity> getRenderedBlockEntities() {
		return delegate.getRenderedBlockEntities();
	}

	@Override
	public BlockState getBlockState(BlockPos pos) {
		delegate.renderMode = renderMode;
		return delegate.getBlockState(pos);
	}

	@Override
	public BlockEntity getBlockEntity(BlockPos pos) {
		return delegate.getBlockEntity(pos);
	}
}
