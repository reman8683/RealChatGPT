package com.reman8683.real_chat_gpt.mixin;

import com.reman8683.real_chat_gpt.chatGPT.OpenAiTextCompletion;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(ChatScreen.class)
public abstract class GPTmixin extends Screen {
    @Shadow protected abstract void setText(String text);

    @Shadow protected TextFieldWidget chatField;
    private ButtonWidget stopSleepingButton;

    protected GPTmixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "init()V")
    private void injected(CallbackInfo ci) {
        this.stopSleepingButton = ButtonWidget.builder(Text.of("ChatGPT"), (button) -> {
            String text = this.chatField.getText();
            new Thread(()->{
                try {
                    this.stopSleepingButton.setMessage(Text.of("Waiting..."));
                    String aiTmp = OpenAiTextCompletion.OpenAiTextCompletion(OpenAiTextCompletion.AI + text + String.format("\n%s ", OpenAiTextCompletion.STOP.get(1)));
                    this.setText(aiTmp.replaceAll("(\r\n|\r|\n|\n\r)", ""));
                    OpenAiTextCompletion.AI += String.format("%s\n%s %s\n%s ", text, OpenAiTextCompletion.STOP.get(1), aiTmp, OpenAiTextCompletion.STOP.get(0));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                this.stopSleepingButton.setMessage(Text.of("ChatGPT"));
            }).start();
        }).dimensions(2, this.height - 37, 50, 20).build();
        this.addDrawableChild(this.stopSleepingButton);
    }
}