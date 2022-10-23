package com.levimartines.mylearningbackend.services.message;

import com.levimartines.mylearningbackend.exceptions.SqsException;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SqsService implements MessageService {

    private final SqsClient sqsClient;
    @Value("${sqs.coffee.queue-url}")
    private String queueUrl;
    @Value("${sqs.coffee.group-id}")
    private String groupId;

    @Override
    public void sendMessage(String message) {
        try {
            SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(message)
                .messageGroupId(groupId)
                .build();
            sqsClient.sendMessage(request);
        } catch (Exception ex) {
            log.error("Error trying to send message to sqs queue. {}", ex.getMessage());
            throw new SqsException(ex);
        }
    }
}
