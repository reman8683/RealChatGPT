package com.reman8683.real_chat_gpt.chatGPT;

import com.google.gson.JsonObject;
import com.reman8683.real_chat_gpt.client.Real_chat_gptClient;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;

import java.io.IOException;
import java.util.List;

public class OpenAiTextCompletion {
    public static final String API_KEY = "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";
    public static String PROMPT;
    public static String AI;
    public static List STOP;

    public static String OpenAiTextCompletion(String transcript) throws IOException {
        JsonObject options = Real_chat_gptClient.GPT_OPTIONS;
        OpenAiService service = new OpenAiService(API_KEY);
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model(options.get("model").getAsString())
                .prompt(transcript)
                .temperature(options.get("temperature").getAsDouble())
                .maxTokens(options.get("maxTokens").getAsInt())
                .topP(options.get("topP").getAsDouble())
                .frequencyPenalty(options.get("frequencyPenalty").getAsDouble())
                .presencePenalty(options.get("presencePenalty").getAsDouble())
                .stop(STOP)
                .build();
        return service.createCompletion(completionRequest).getChoices().get(0).getText();
    }
}
