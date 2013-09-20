package eldritchempires.entity;

import java.util.Iterator;
import java.util.List;

import eldritchempires.Registration;
import eldritchempires.EldritchWorldData;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.WorldSavedData;

public class TileEntityNode extends TileEntity
{
	private int i = 0;
	private double randX = 0.0D;
	private double randZ = 0.0D;
	private int searchRadius = 2;
	int blockType = -1;
	EldritchWorldData data = new EldritchWorldData();
	
	public TileEntityNode()
    {

    }
	
//	public void updateEntity() 
//	{
//		if (i < 300)
//		{
//			i++;
//		}
//		else
//		{
////			if(this.worldObj.loadItemData(WorldSavedDataEE.class, "EEWorldSaveData") == null){
////				WorldSavedData data = new WorldSavedDataEE("EEWorldSaveData");
////				this.worldObj.setItemData("EEWorldSaveData", data);
////			}
//
////			worldSavedDataEE = (WorldSavedDataEE) this.worldObj.loadItemData(WorldSavedDataEE.class, "EEWorldSaveData");
//			
////			worldSaveData = (WorldSaveData) world.loadItemData(WorldSaveData.class, "LegendzWorldSaveData");
////			
//			if (!this.worldObj.isRemote)
//			{
//				data.setTest(8);
//				this.worldObj.perWorldStorage.setData(EldritchWorldData.name, data);
//				data = (EldritchWorldData) this.worldObj.perWorldStorage.loadData(EldritchWorldData.class, EldritchWorldData.name);
//				int test = data.getTest();
//
//				Minecraft.getMinecraft().thePlayer.addChatMessage("Tile Entity Exists!" + test);
//			}
//			
//			List<?> var4 = this.worldObj.getEntitiesWithinAABB(MagicEssence.class, AxisAlignedBB.getAABBPool().getAABB(this.xCoord - searchRadius, this.yCoord - searchRadius, this.zCoord - searchRadius, this.xCoord + searchRadius, this.yCoord + searchRadius, this.zCoord + searchRadius));
//
//			if (var4 != null && !var4.isEmpty()) {
//				Iterator<?> var5 = var4.iterator();
//
//				while (var5.hasNext()) {
//					EntityLiving var6 = (EntityLiving)var5.next();
//					if (!this.worldObj.isRemote && !var6.isDead)
//					{
//					    var6.setDead();
//						ItemStack droppedItem = new ItemStack(Registration.condensedEssence, 1);
//						EntityItem entityitem = new EntityItem(this.worldObj, (double)this.xCoord, (double)this.yCoord + 1, (double)this.zCoord, droppedItem);
//						entityitem.delayBeforeCanPickup = 10;
//						this.worldObj.spawnEntityInWorld(entityitem);
//					}
//				}
//			}
//			
//			int k = this.worldObj.rand.nextInt(4);
//			if (k == 0)
//			{
//				randX = -30.5D;
//			}
//			
//			if (k == 1)
//			{
//				randX = 30.5D;
//			}
//			
//			if (k == 2)
//			{
//				randZ = -30.5D;
//			}
//			
//			if (k == 3)
//			{
//				randZ = 30.5D;
//			}
//			
//			i = 0;
//			
//			if (blockType == -1)
//			{
//				blockType = this.blockMetadata;
//			}
//			
//			if (!this.worldObj.isRemote && blockType == 0)
//			{
////				for (int j = 0;j < 2; j++)
////				{
//					Zoblin zoblin = new Zoblin(this.worldObj);
//					zoblin.setLocationAndAngles((double)this.xCoord + randX, (double)zoblin.getFirstUncoveredBlockHeight(this.xCoord + (int)randX, this.zCoord + (int)randZ) + 1.0D, (double)this.zCoord + randZ, 0.0F, 0.0F);
//					zoblin.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(2.099D);
//					zoblin.attacking = true;
//					zoblin.nodex = this.xCoord;
//					zoblin.nodey = this.yCoord;
//					zoblin.nodez = this.zCoord;
//					this.worldObj.spawnEntityInWorld(zoblin);
////				}
//				ZoblinBomber zoblinBomber = new ZoblinBomber(this.worldObj);
//				zoblinBomber.setLocationAndAngles((double)this.xCoord + randX, (double)zoblinBomber.getFirstUncoveredBlockHeight(this.xCoord + (int)randX, this.zCoord + (int)randZ) + 1.0D, (double)this.zCoord + randZ, 0.0F, 0.0F);
//				zoblinBomber.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(2.099D);
//				zoblinBomber.attacking = true;
//				zoblinBomber.nodex = this.xCoord;
//				zoblinBomber.nodey = this.yCoord;
//				zoblinBomber.nodez = this.zCoord;
//				this.worldObj.spawnEntityInWorld(zoblinBomber);
//				
//				MagicEssence magicEssence = new MagicEssence(this.worldObj);
//				magicEssence.setLocationAndAngles((double)this.xCoord + randX, (double)zoblinBomber.getFirstUncoveredBlockHeight(this.xCoord + (int)randX, this.zCoord + (int)randZ) + 1.0D, (double)this.zCoord + randZ, 0.0F, 0.0F);
//				magicEssence.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(2.699D);
//				magicEssence.attacking = true;
//				magicEssence.nodex = this.xCoord;
//				magicEssence.nodey = this.yCoord;
//				magicEssence.nodez = this.zCoord;
//				this.worldObj.spawnEntityInWorld(magicEssence);
//			}
//			
//			randX = 0.0D;
//			randZ = 0.0D;
//		}
//	}
//	
//    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
//    {
//        super.writeToNBT(par1NBTTagCompound);
//        par1NBTTagCompound.setInteger("BlockType", blockType);
//    }
//
//    /**
//     * (abstract) Protected helper method to read subclass entity data from NBT.
//     */
//    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
//    {
//        super.readFromNBT(par1NBTTagCompound);
//        if (par1NBTTagCompound.hasKey("BlockType"))
//        {
//            blockType = par1NBTTagCompound.getInteger("BlockType");
//        }
//    }
//	
}
