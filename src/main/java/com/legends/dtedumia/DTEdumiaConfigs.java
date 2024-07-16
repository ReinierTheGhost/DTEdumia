package com.legends.dtedumia;

import net.minecraftforge.common.ForgeConfigSpec;

public class DTEdumiaConfigs {

	public static final ForgeConfigSpec GENERAL_SPEC;

	static {
		ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
		setupConfig(configBuilder);
		GENERAL_SPEC = configBuilder.build();
	}


	private static void setupConfig(ForgeConfigSpec.Builder builder) {

		builder.push("Dynamic Trees + Edumia Compat Mod Config");


		builder.pop();
	}
}
