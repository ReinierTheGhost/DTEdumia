package com.legends.dtedumia.growthlogic;

import com.ferreusveritas.dynamictrees.api.registry.Registry;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.legends.dtedumia.DynamicTreesEdumia;

public class DTEdumiaGrowthLogicKits {

	public static final GrowthLogicKit TALL_DECIDUOUS = new TallDeciduousLogic(
			DynamicTreesEdumia.location("tall_deciduous"));
	public static final GrowthLogicKit TWISTING_TREE = new TwistingTreeLogic(
			DynamicTreesEdumia.location("twisting_tree"));
	public static final GrowthLogicKit VARIATE_HEIGHT = new VariateHeightLogic(
			DynamicTreesEdumia.location("variate_height"));
	public static final GrowthLogicKit POPLAR = new PoplarLogic(DynamicTreesEdumia.location("poplar"));
	public static final GrowthLogicKit ASPEN = new AspenLogic(DynamicTreesEdumia.location("aspen"));
	public static final GrowthLogicKit BAOBAB = new BaobabLogic(DynamicTreesEdumia.location("baobab"));

	public static void register(final Registry<GrowthLogicKit> registry) {
		registry.registerAll(TALL_DECIDUOUS, TWISTING_TREE, VARIATE_HEIGHT, POPLAR, BAOBAB);
	}

}
