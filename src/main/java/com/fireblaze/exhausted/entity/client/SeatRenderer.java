package com.fireblaze.exhausted.entity.client;

import com.fireblaze.exhausted.entity.SeatEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

public class SeatRenderer extends EntityRenderer<SeatEntity> {
    public SeatRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    // Gib eine **valide** ResourceLocation zurück (z. B. Block-Atlas)
    @Override
    public ResourceLocation getTextureLocation(SeatEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    // Nichts rendern
    @Override
    public void render(SeatEntity entity, float yaw, float pt,
                       PoseStack pose, MultiBufferSource buf, int light) {
        // no-op
    }

    // Gar nicht erst versuchen zu rendern
    @Override
    public boolean shouldRender(SeatEntity entity, Frustum frustum, double x, double y, double z) {
        return false;
    }
}
