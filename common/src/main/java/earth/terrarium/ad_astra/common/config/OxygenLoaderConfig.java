package earth.terrarium.ad_astra.common.config;

import com.teamresourceful.resourcefulconfig.common.annotations.Category;
import com.teamresourceful.resourcefulconfig.common.annotations.ConfigEntry;
import com.teamresourceful.resourcefulconfig.common.config.EntryType;
import com.teamresourceful.resourcefulconfig.web.annotations.WebInfo;
import earth.terrarium.botarium.common.fluid.utils.FluidHooks;

@Category(id = "oxygenLoader", translation = "text.resourcefulconfig.ad_astra.option.oxygenLoader")
@WebInfo(icon = "leaf")
public final class OxygenLoaderConfig {

    @ConfigEntry(
        id = "maxEnergy",
        type = EntryType.LONG,
        translation = "text.resourcefulconfig.ad_astra.option.oxygenLoader.maxEnergy"
    )
    public static long maxEnergy = 9000L;

    @ConfigEntry(
        id = "energyPerTick",
        type = EntryType.LONG,
        translation = "text.resourcefulconfig.ad_astra.option.oxygenLoader.energyPerTick"
    )
    public static long energyPerTick = 10L;

    @ConfigEntry(
        id = "tankSize",
        type = EntryType.LONG,
        translation = "text.resourcefulconfig.ad_astra.option.oxygenLoader.tankSize"
    )
    public static long tankSize = FluidHooks.buckets(3);
}