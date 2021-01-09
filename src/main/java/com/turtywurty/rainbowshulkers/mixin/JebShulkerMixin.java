package com.turtywurty.rainbowshulkers.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.turtywurty.rainbowshulkers.RainbowShulkers;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.ShulkerRenderer;
import net.minecraft.client.renderer.entity.layers.ShulkerColorLayer;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;

@Mixin(ShulkerColorLayer.class)
public class JebShulkerMixin {

	@Inject(method = "render(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/entity/monster/ShulkerEntity;FFFFFF)V", 
			at = @At(
					value = "INVOKE", 
					target = "Lnet/minecraft/client/renderer/IRenderTypeBuffer;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/IVertexBuilder;"), 
			locals = LocalCapture.CAPTURE_FAILSOFT)
	private void render(CallbackInfo ci, ResourceLocation resourcelocation, MatrixStack matrixStackIn,
			IRenderTypeBuffer bufferIn, int packedLightIn, ShulkerEntity entitylivingbaseIn, float limbSwing,
			float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		int ticks = entitylivingbaseIn.ticksExisted / 25 + entitylivingbaseIn.getEntityId();
		int colorLength = DyeColor.values().length;
		int colorIndex = ticks % colorLength;
		int tickColorIndex = (ticks + 1) % colorLength;
		float math = ((float) (entitylivingbaseIn.ticksExisted % 25) + partialTicks) / 25.0F;
		float[] color1 = SheepEntity.getDyeRgb(DyeColor.byId(colorIndex));
		float[] color2 = SheepEntity.getDyeRgb(DyeColor.byId(tickColorIndex));
		float red = color1[0] * (1.0F - math) + color2[0] * math;
		float green = color1[1] * (1.0F - math) + color2[1] * math;
		float blue = color1[2] * (1.0F - math) + color2[2] * math;
		resourcelocation = ShulkerRenderer.SHULKER_ENDERGOLEM_TEXTURE[RainbowShulkers
				.getRgbDye(new float[] { red, green, blue }) != null
						? RainbowShulkers.getRgbDye(new float[] { red, green, blue }).getId()
						: 0];
		System.out.println(resourcelocation);
	}
}
