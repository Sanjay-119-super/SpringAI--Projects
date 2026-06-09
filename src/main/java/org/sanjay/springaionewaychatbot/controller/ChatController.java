package org.sanjay.springaionewaychatbot.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = {
        "https://cfschat-5xl8uki34-saginfo54-3311s-projects.vercel.app",
        "https://cfschat.vercel.app",
        "https://cfschat-saginfo54-3311s-projects.vercel.app/"
})
public class ChatController {

    private final ChatClient chatClient;

    private final Map<String, String> fastCache = new ConcurrentHashMap<>();


    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;

        // Pre-load exact match answers into the cache
        fastCache.put("who are you", "I am the Code For Success (CFS) Chat Assistant, helping CFS engineers learn Java full-stack industry-ready development.");
        fastCache.put("who developed you", "I was developed by the CFS team.");
        fastCache.put("who created you", "I was developed by the CFS team.");

    }


    private static final String SYSTEM_PROMPT=
            "You are the official 'Code For Success' (CFS) Chat Assistant. " +
                    "Your primary mission is to help CFS engineers learn Java full-stack industry-ready development. " +
                    "You must strictly follow these rules for specific questions: " +
                    "1. If asked 'who are you', 'what are you', or 'what do you do', you MUST reply exactly with: " +
                    "'I am the Code For Success (CFS) Chat Assistant, helping CFS engineers learn Java full-stack industry-ready development.' " +
                    "2. If asked 'who developed you', 'who created you', or 'who made you', you MUST reply exactly with: " +
                    "'I was developed by the CFS team.' " +
                    "For all other questions, provide accurate, professional, and helpful answers focused on Java, Spring Boot, Microservices, and full-stack engineering.";




    @PostMapping
    public Map<String, String> chat(@RequestBody Map<String , String> body){
        String question = body.get("question").trim().toLowerCase();

        // 1. Check Cache First (Takes < 1 millisecond!)
        if (fastCache.containsKey(question)){
            return Map.of("answer",fastCache.get(question));
        }

        // 2. If not cached, call Groq
        try {
            String ans = chatClient.prompt()
                    .system(SYSTEM_PROMPT)
                    .user(question)
                    .call()
                    .content();

            fastCache.put(question,ans); //new question

            return Map.of("answer",ans);
        }catch (Exception e){
            if (e.getMessage() != null && e.getMessage().contains("429")){
                return Map.of("answer", "⚠️ **High Traffic Alert:** I've reached my free tier rate limit for this minute. Please wait 60 seconds and ask me again!");
            }
            return Map.of("answer", "I'm sorry, I encountered a technical issue. Please try again later.");

        }
    }
}
