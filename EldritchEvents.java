package eldritchempires;

import java.util.Iterator;
import java.util.List;

import eldritchempires.entity.MagicEssence;
import eldritchempires.entity.Zoblin;
import eldritchempires.entity.ZoblinBomber;
import eldritchempires.entity.ZoblinBoss;
import eldritchempires.entity.ZoblinWarrior;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;

public class EldritchEvents {
	
	int tickCount = 0;
	long lastSpawn = 0;
//	public static int wave = 0;
	int announceRadius = 100;
//	public static boolean waveActive = true;
	String announce = "Incoming: ";
	EldritchWorldData data = new EldritchWorldData();
	
	@ForgeSubscribe
	public void onWorldEvent(WorldEvent event)
	{
		tickCount++;
//		List playerList = event.world.playerEntities;
		if (tickCount >= 5 && event.world.provider.dimensionId == 0 && !event.world.playerEntities.isEmpty())
		{
//			data.markDirty();
//			event.world.perWorldStorage.setData(EldritchWorldData.name, data);
			data = EldritchWorldData.forWorld(event.world);
			if (data != null && !event.world.isRemote)
			{
//				System.out.println("Marker set:" + data.checkMarker());
				
				int portalX = data.getPortalX();
				int portalY = data.getPortalY();
				int portalZ = data.getPortalZ();
				int collectorX = data.getCollectorX();
				int collectorY = data.getCollectorY();
				int collectorZ = data.getCollectorZ();
				int wave = data.getWave();
//				if (event.world.checkChunksExist(markerX, markerY, markerZ, nodeX, nodeY, nodeZ))
//				if (event.world.activeChunkSet != null)
//				{
					
//					System.out.println("Server time: " + event.world.provider.getWorldTime());

				
				if(data.checkPortal() && !data.checkCollector())
				{
					event.world.getChunkProvider().loadChunk(portalX >> 4, portalZ >> 4);
					Chunk portalChunk = event.world.getChunkFromBlockCoords(portalX, portalZ);
					if (portalChunk.isChunkLoaded)
					{
//					System.out.println("Marker found at:" + markerX + " " + markerY + " " + markerZ);
//					System.out.println("BlockID:" + event.world.getBlockId(markerX, markerY, markerZ));
					
						if (event.world.getBlockId(portalX, portalY, portalZ) != Registration.portal.blockID)
						{
							System.out.println("Portal unset" );
							data.unSetPortal();
							data.setActiveWave(false);
							event.world.perWorldStorage.setData(EldritchWorldData.name, data);
						}
					}
				}
				
				if(data.checkCollector())
				{
					event.world.getChunkProvider().loadChunk(collectorX >> 4, collectorZ >> 4);
					Chunk nodeChunk = event.world.getChunkFromBlockCoords(collectorX, collectorZ);
					if (nodeChunk.isChunkLoaded)
					{
//					System.out.println("Marker found at:" + markerX + " " + markerY + " " + markerZ);
//					System.out.println("BlockID:" + event.world.getBlockId(markerX, markerY, markerZ));
					
						if (event.world.getBlockId(collectorX, collectorY, collectorZ) != Registration.collector.blockID)
						{
							System.out.println("Collector unset" );
							data.unSetCollector();
							data.setActiveWave(false);
							event.world.perWorldStorage.setData(EldritchWorldData.name, data);
						}
					}
				}
				
				if (data.checkCollector() && (event.world.provider.getWorldTime() - lastSpawn) < 0)
				{
					lastSpawn = event.world.provider.getWorldTime();
				}
				
				if (data.isWaveActive() && data.checkCollector() && (event.world.provider.getWorldTime() - lastSpawn) >= 600)
				{
					if (!data.checkPortal())
					{
						EldritchMethods.broadcastMessageLocal("A zoblin portal opens", collectorX, collectorY, collectorZ, 100, event.world);
						int[] location = EldritchMethods.createPortal("zoblin", collectorX, collectorY, collectorZ, event.world);
						data.setPortal(location[0], location[1], location[2]);
					}
					waves(wave, portalX, portalY, portalZ, event.world);
//					System.out.println("Spawn code here");
//					List<?> var4 = event.world.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getAABBPool().getAABB(collectorX - announceRadius, collectorY - announceRadius, collectorZ - announceRadius, collectorX + announceRadius, collectorY + announceRadius, collectorZ + announceRadius));
//
//					if (var4 != null && !var4.isEmpty()) {
//						Iterator<?> var5 = var4.iterator();
//
//						while (var5.hasNext()) {
//							EntityPlayer var6 = (EntityPlayer)var5.next();
//							var6.addChatMessage(announce);
//						}
//					}
					if (announce != "")
						EldritchMethods.broadcastMessageLocal(announce, collectorX, collectorY, collectorZ, 100, event.world);
					
					
					announce = "Incoming: ";
					wave++;
					data.setWave(wave++);
					event.world.perWorldStorage.setData(EldritchWorldData.name, data);
					lastSpawn = event.world.provider.getWorldTime();
//					System.out.println("Spawn time: " + lastSpawn);
//				}
				}
				
				if (!data.isWaveActive() && data.checkPortal())
				{
					EldritchMethods.broadcastMessageLocal("The portal closes", portalX, portalY, portalZ, 100, event.world);
					data.unSetPortal();
					event.world.perWorldStorage.setData(EldritchWorldData.name, data);
					event.world.setBlockToAir(portalX, portalY, portalZ);
					event.world.removeBlockTileEntity(portalX, portalY, portalZ);
				}
					
			}
			
//			data.setTest(8);
//			event.world.perWorldStorage.setData(EldritchWorldData.name, data);
//			data = (EldritchWorldData) event.world.perWorldStorage.loadData(EldritchWorldData.class, EldritchWorldData.name);
//			int test = data.getTest();
//
//			
			tickCount = 0;
		}
	}
	
	public void waves(int wave, int x, int y, int z, World world)
	{
		switch (wave){
			case 0: 
				announce = "Zoblins approaching!";
				break;
			case 1:
				spawnWave("zoblin", 2, x, y, z, world);
				break;
			case 2:
				spawnWave("zoblin", 2, x, y, z, world);
				spawnWave("zoblinBomber", 1, x, y, z, world);
				break;
			case 3:
				spawnWave("zoblin", 2, x, y, z, world);
				spawnWave("zoblinBomber", 2, x, y, z, world);
				spawnWave("magicEssence", 1, x, y, z, world);
				break;
			case 4:
				spawnWave("zoblin", 2, x, y, z, world);
				spawnWave("zoblinBomber", 1, x, y, z, world);
				break;
			case 5:
				spawnWave("zoblin", 2, x, y, z, world);
				spawnWave("zoblinBomber", 2, x, y, z, world);
				spawnWave("magicEssence", 1, x, y, z, world);
				break;
			case 6:
				spawnWave("zoblin", 2, x, y, z, world);
				spawnWave("zoblinBomber", 1, x, y, z, world);
				break;
			case 7:
				spawnWave("zoblin", 2, x, y, z, world);
				spawnWave("zoblinBomber", 2, x, y, z, world);
				spawnWave("magicEssence", 1, x, y, z, world);
				break;
			case 8:
				spawnWave("zoblin", 2, x, y, z, world);
				spawnWave("zoblinWarrior", 1, x, y, z, world);
				spawnWave("zoblinBomber", 1, x, y, z, world);
				break;
			case 9:
				spawnWave("zoblin", 3, x, y, z, world);
				spawnWave("zoblinBomber", 2, x, y, z, world);
				spawnWave("magicEssence", 1, x, y, z, world);
				break;
			case 10:
				spawnWave("zoblinBoss", 1, x, y, z, world);
				spawnWave("magicEssence", 1, x, y, z, world);
//				waveActive = false;
				break;
			case 11:
				announce = "";
				break;
			case 12: 
				announce = "More zoblins approaching!";
				break;
			case 13:
				spawnWave("zoblinWarrior", 2, x, y, z, world);
				break;
			case 14:
				spawnWave("zoblin", 2, x, y, z, world);
				spawnWave("zoblinWarrior", 1, x, y, z, world);
				spawnWave("zoblinBomber", 1, x, y, z, world);
				spawnWave("magicEssence", 1, x, y, z, world);
				break;
			case 15:
				spawnWave("zoblin", 2, x, y, z, world);
				spawnWave("zoblinWarrior", 1, x, y, z, world);
				spawnWave("zoblinBomber", 2, x, y, z, world);
				break;
			case 16:
				spawnWave("zoblinWarrior", 2, x, y, z, world);
				spawnWave("zoblinBomber", 3, x, y, z, world);
				spawnWave("magicEssence", 1, x, y, z, world);
				break;
			case 17:
				spawnWave("zoblinWarrior", 4, x, y, z, world);
				break;
			case 18:
				spawnWave("zoblin", 4, x, y, z, world);
				spawnWave("zoblinBomber", 2, x, y, z, world);
				spawnWave("magicEssence", 1, x, y, z, world);
				break;
			case 19:
				spawnWave("zoblin", 2, x, y, z, world);
				spawnWave("zoblinWarrior", 2, x, y, z, world);
				spawnWave("zoblinBomber", 1, x, y, z, world);
				break;
			case 20:
				spawnWave("zoblinWarrior", 3, x, y, z, world);
				spawnWave("zoblinBomber", 2, x, y, z, world);
				spawnWave("magicEssence", 1, x, y, z, world);
				break;
			case 21:
				spawnWave("zoblin", 2, x, y, z, world);
				spawnWave("zoblinBomber", 4, x, y, z, world);
				break;
			case 22:
				spawnWave("zoblinWarrior", 2, x, y, z, world);
				spawnWave("zoblinBoss", 1, x, y, z, world);
				spawnWave("magicEssence", 1, x, y, z, world);
				break;
			case 23:
				announce = "The portal closes";
				world.setBlockToAir(x, y, z);
				world.removeBlockTileEntity(x, y, z);
				world.setBlockMetadataWithNotify(data.getCollectorX(), data.getCollectorY(), data.getCollectorZ(), 0, 2);
				data.unSetPortal();
				world.perWorldStorage.setData(EldritchWorldData.name, data);
				data.setActiveWave(false);
				break;
		}
	}
	
	public void spawnWave(String mobName, int count, int x, int y, int z, World world)
	{
		String name = "";
		
		for (int i = 1; i < (count + 1); i++)
		{
			if (mobName == "zoblin")
			{
				Zoblin entity = new Zoblin(world);
				entity.setLocationAndAngles((double)x, (double)y + 1, (double)z, 0.0F, 0.0F);
//				zoblin.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(2.099D);
				entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(2.099D);
				entity.attacking = true;
				entity.nodeX = data.getCollectorX();
				entity.nodeY = data.getCollectorY();
				entity.nodeZ = data.getCollectorZ();
				world.spawnEntityInWorld(entity);
				name = "Zoblin";
			}
			if (mobName == "zoblinBomber")
			{
				ZoblinBomber entity = new ZoblinBomber(world);
				entity.setLocationAndAngles((double)x, (double)y + 1, (double)z, 0.0F, 0.0F);
//				zoblinBomber.func_110148_a(SharedMonsterAttributes.field_111263_d).func_111128_a(1.599D);
				entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(1.599D);
				entity.attacking = true;
				entity.nodeX = data.getCollectorX();
				entity.nodeY = data.getCollectorY();
				entity.nodeZ = data.getCollectorZ();
        		double xd = data.getCollectorX() - x;
        		double yd = data.getCollectorY() - y;
        		double zd = data.getCollectorZ() - z;
        		double distance = Math.sqrt(xd*xd + yd*yd + zd*zd);
				entity.timer = (int)(distance/2);
//				System.out.println("ZoblinBomber distance timer: " + distance + " " + zoblinBomber.timer );
				world.spawnEntityInWorld(entity);
				name = "Zoblin Bomber";
			}
			if (mobName == "magicEssence")
			{
				MagicEssence entity = new MagicEssence(world);
				entity.setLocationAndAngles((double)x, (double)y + 1, (double)z, 0.0F, 0.0F);
				entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(2.599D);
				entity.attacking = true;
				entity.nodeX = data.getCollectorX();
				entity.nodeY = data.getCollectorY();
				entity.nodeZ = data.getCollectorZ();
				world.spawnEntityInWorld(entity);
				name = "Magic Essence";
			}
			if (mobName == "zoblinBoss")
			{
				ZoblinBoss entity = new ZoblinBoss(world);
				entity.setLocationAndAngles((double)x, (double)y + 1, (double)z, 0.0F, 0.0F);
				entity.attacking = true;
				entity.collectorX = data.getCollectorX();
				entity.collectorY = data.getCollectorY();
				entity.collectorZ = data.getCollectorZ();
				world.spawnEntityInWorld(entity);
				name = "Zoblin Boss";
			}
			if (mobName == "zoblinWarrior")
			{
				ZoblinWarrior entity = new ZoblinWarrior(world);
				entity.setLocationAndAngles((double)x, (double)y + 1, (double)z, 0.0F, 0.0F);
				entity.attacking = true;
				entity.nodeX = data.getCollectorX();
				entity.nodeY = data.getCollectorY();
				entity.nodeZ = data.getCollectorZ();
				entity.setCurrentItemOrArmor(0, new ItemStack(Item.swordIron));
				world.spawnEntityInWorld(entity);
				name = "Zoblin Warrior";
			}

		}
		announce = announce + name + " x" + count + "  ";
	}
	
}
