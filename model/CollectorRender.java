package eldritchempires.model;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import eldritchempires.entity.TileEntityNode;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ResourceLocation;

public class CollectorRender extends TileEntitySpecialRenderer {

    private static final ResourceLocation collector = new ResourceLocation("eldritchempires:model/texture/collector.png");
    private static final ResourceLocation inactiveCollector = new ResourceLocation("eldritchempires:model/texture/inactiveCollector.png");
    
    private final CollectorModel modelCollector = new CollectorModel();
    
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) 
	{
		this.renderTileEntityCollectorAt((TileEntityNode)tileentity, d0, d1, d2, f);

	}
	
    public void renderTileEntityCollectorAt(TileEntityNode par1TileEntity, double par2, double par4, double par6, float par8)
    {
        int i = par1TileEntity.getBlockMetadata();

        if (i == 0)
        {
        	this.func_110628_a(collector);;
        }

        if (i == 1)
        {
        	this.func_110628_a(inactiveCollector);;
        }

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float)par2 + 0.5F, (float)par4 + 1.5F, (float)par6 + 0.5F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        modelCollector.renderCollector(0.0625F);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

}
