package com.legends.dtedumia;

import com.ferreusveritas.dynamictrees.api.GatherDataHelper;
import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.legends.dtedumia.registry.DTEdumiaRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.resource.PathPackResources;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(DynamicTreesEdumia.MOD_ID)
public class DynamicTreesEdumia {

	public static final String MOD_ID = "dtedumia";

	public DynamicTreesEdumia() {
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(Type.COMMON, DTEdumiaConfigs.GENERAL_SPEC, "dtedumiaconfig.toml");

		modEventBus.addListener(this::gatherData);
		MinecraftForge.EVENT_BUS.register(this);

		RegistryHandler.setup(MOD_ID);

		DTEdumiaRegistries.setup();
	}

	private void gatherData(final GatherDataEvent event) {
		GatherDataHelper.gatherAllData(MOD_ID, event,
				Family.REGISTRY,
				Species.REGISTRY,
				LeavesProperties.REGISTRY);
	}

	public static ResourceLocation location(String name) {
		return new ResourceLocation(MOD_ID, name);
	}


}

