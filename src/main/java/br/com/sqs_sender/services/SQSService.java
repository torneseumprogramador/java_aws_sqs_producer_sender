package br.com.sqs_sender.services;

import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

public class SQSService {
    public static void sendMessage(String message){
        AwsCredentialsProvider credentialsProvider = new AwsCredentialsProvider() {
            @Override
            public AwsCredentials resolveCredentials() {
                return new AwsCredentials() {
                    @Override
                    public String accessKeyId() {
                        return System.getenv("AWS_ACCESS_KEY");
                    }
        
                    @Override
                    public String secretAccessKey() {
                        return System.getenv("AWS_SECRET_KEY");
                    }
                };
            }
        };

        SqsClient sqsClient = SqsClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(credentialsProvider)
                .build();

        // ===== Busca uma Fila =====
        GetQueueUrlRequest request = GetQueueUrlRequest.builder()
                .queueName("fila-danilo.fifo") // enfia para fila fifo
                // .queueName("fila-danilo") // enfia para fila padrão
                .queueOwnerAWSAccountId("473247640396").build();
        GetQueueUrlResponse createResult = sqsClient.getQueueUrl(request);
        
        sendMessage(sqsClient, createResult.queueUrl(), message);

        sqsClient.close();
    }

    public static void sendMessage(SqsClient sqsClient, String queueUrl, String message) {
        SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
            .queueUrl(queueUrl)
            .messageGroupId("grupo") // Para filas fifo
            .messageBody(message)
            .build();
        sqsClient.sendMessage(sendMsgRequest);
    }
}