package com.reman8683.real_chat_gpt.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.reman8683.real_chat_gpt.chatGPT.OpenAiTextCompletion;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Environment(EnvType.CLIENT)
public class Real_chat_gptClient implements ClientModInitializer {
    public static JsonObject GPT_OPTIONS;
    @Override
    public void onInitializeClient() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return new Identifier("real_chat_gpt", "settings.json");
            }

            @Override
            public void reload(ResourceManager manager) {
                try {
                    Reader reader = new InputStreamReader(manager.getResource(new Identifier("real_chat_gpt", "settings.json")).get().getInputStream(), StandardCharsets.UTF_8);
                    Gson gson = new Gson();
                    GPT_OPTIONS = gson.fromJson(reader, JsonObject.class);
                    OpenAiTextCompletion.PROMPT = GPT_OPTIONS.get("prompt").getAsString();
                    OpenAiTextCompletion.STOP = gson.fromJson(GPT_OPTIONS.get("stop"), List.class);
                    OpenAiTextCompletion.AI = OpenAiTextCompletion.PROMPT;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
