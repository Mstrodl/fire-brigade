package tech.coolmathgames.brigade;

import net.devtech.grossfabrichacks.entrypoints.PrePreLaunch;
import net.devtech.grossfabrichacks.instrumentation.InstrumentationApi;

public class EntryPoint implements PrePreLaunch {
    @Override
    public void onPrePreLaunch() {
        InstrumentationApi.pipeClassThroughTransformerBootstrap("com/mojang/brigadier/StringReader");
    }
}
