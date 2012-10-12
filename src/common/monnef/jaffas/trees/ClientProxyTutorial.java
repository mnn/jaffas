package monnef.jaffas.trees;

import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxyTutorial extends CommonProxyTutorial {
    @Override
    public void registerRenderThings() {
        MinecraftForgeClient.preloadTexture("/jaffas_02.png");
    }

    @Override
    public void setFancyGraphicsLevel(BlockFruitLeaves leaves, boolean value) {
        leaves.setGraphicsLevel(value);
    }



}
