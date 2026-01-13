package com.ecommerce.complaints.ai.advisor;


import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;

import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SensitiveWordsAdvisor implements CallAroundAdvisor, Ordered {

    private final Set<String> sensitiveWords;
    private final String rejectionMessage;

    public SensitiveWordsAdvisor(Set<String> sensitiveWords, String rejectionMessage) {
        this.sensitiveWords = sensitiveWords.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        this.rejectionMessage = rejectionMessage;
    }

    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        String userText = advisedRequest.userText();
        if (userText != null) {
            String lowerCaseUserText = userText.toLowerCase();
            boolean containsSensitiveWord = sensitiveWords.stream()
                    .anyMatch(lowerCaseUserText::contains);

            if (containsSensitiveWord) {
                ChatResponse chatResponse = new ChatResponse(
                        List.of(new Generation(new AssistantMessage(rejectionMessage)))
                );
                return new AdvisedResponse(chatResponse, Map.of());
            }
        }
        return chain.nextAroundCall(advisedRequest);
    }
    @Override
    public String getName() {
        return "SensitiveWordsAdvisor";
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }


}
