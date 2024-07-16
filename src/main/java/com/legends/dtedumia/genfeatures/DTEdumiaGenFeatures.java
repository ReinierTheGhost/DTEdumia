package com.legends.dtedumia.genfeatures;

import com.ferreusveritas.dynamictrees.api.registry.Registry;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeature;
import com.legends.dtedumia.DynamicTreesEdumia;

public class DTEdumiaGenFeatures {

    public static final GenFeature RANDOM_STRIPPED_BRANCHES = new RandomStrippedBranches(DynamicTreesEdumia.location("random_stripped_branches"));

    public static void register(final Registry<GenFeature> registry) {
        registry.registerAll(RANDOM_STRIPPED_BRANCHES);
    }

}
