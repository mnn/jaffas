package monnef.jaffas.food;

import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxyTutorial extends CommonProxyTutorial{
	@Override
	public void registerRenderThings(){
		MinecraftForgeClient.preloadTexture("/jaffas_01.png");
	}
}
