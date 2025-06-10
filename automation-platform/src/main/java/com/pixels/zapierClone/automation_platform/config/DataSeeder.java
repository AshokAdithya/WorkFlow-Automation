package com.pixels.zapierClone.automation_platform.config;

import com.pixels.zapierClone.automation_platform.entity.Action;
import com.pixels.zapierClone.automation_platform.entity.AppIntegration;
import com.pixels.zapierClone.automation_platform.entity.Trigger;
import com.pixels.zapierClone.automation_platform.repository.AppIntegrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private AppIntegrationRepository appIntegrationRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (appIntegrationRepository.count() == 0) {
            AppIntegration gmail = new AppIntegration();
            gmail.setName("Gmail");
            gmail.setIdentifier("gmail");
            gmail.setDescription("Email service by Google.");
            gmail.setLogoUrl("[https://placehold.co/40x40/FF0000/FFFFFF?text=G](https://placehold.co/40x40/FF0000/FFFFFF?text=G)");
            gmail.setAuthType("OAUTH2");

            Trigger newEmailTrigger = new Trigger();
            newEmailTrigger.setAppIntegration(gmail);
            newEmailTrigger.setTriggerTypeIdentifier("new_email");
            newEmailTrigger.setName("New Email");
            newEmailTrigger.setDescription("Triggers when a new email arrives.");
            newEmailTrigger.setConfigJson("{\"fields\": [{\"name\": \"folder\", \"label\": \"Folder\", \"type\": \"text\", \"required\": false, \"default\": \"Inbox\"}]}");

            Action sendEmailAction = new Action();
            sendEmailAction.setAppIntegration(gmail);
            sendEmailAction.setActionTypeIdentifier("send_email");
            sendEmailAction.setName("Send Email");
            sendEmailAction.setDescription("Sends an email from your account.");
            sendEmailAction.setConfigJson("{\"fields\": [{\"name\": \"to\", \"label\": \"To (Email)\", \"type\": \"text\", \"required\": true}, {\"name\": \"subject\", \"label\": \"Subject\", \"type\": \"text\", \"required\": true}, {\"name\": \"body\", \"label\": \"Body\", \"type\": \"textarea\", \"required\": true}]}");

            Action createDraftAction = new Action();
            createDraftAction.setAppIntegration(gmail);
            createDraftAction.setActionTypeIdentifier("create_draft");
            createDraftAction.setName("Create Draft");
            createDraftAction.setDescription("Creates a new email draft.");
            createDraftAction.setConfigJson("{\"fields\": [{\"name\": \"to\", \"label\": \"To (Email)\", \"type\": \"text\", \"required\": true}, {\"name\": \"subject\", \"label\": \"Subject\", \"type\": \"text\", \"required\": true}, {\"name\": \"body\", \"label\": \"Body\", \"type\": \"textarea\", \"required\": true}]}");


            gmail.setTriggers(Arrays.asList(newEmailTrigger));
            gmail.setActions(Arrays.asList(sendEmailAction, createDraftAction));
            appIntegrationRepository.save(gmail);

            AppIntegration slack = new AppIntegration();
            slack.setName("Slack");
            slack.setIdentifier("slack");
            slack.setDescription("Team messaging and collaboration.");
            slack.setLogoUrl("[https://placehold.co/40x40/4A154B/FFFFFF?text=S](https://placehold.co/40x40/4A154B/FFFFFF?text=S)");
            slack.setAuthType("OAUTH2");

            Action sendMessageAction = new Action();
            sendMessageAction.setAppIntegration(slack);
            sendMessageAction.setActionTypeIdentifier("send_channel_message");
            sendMessageAction.setName("Send Channel Message");
            sendMessageAction.setDescription("Sends a message to a Slack channel.");
            sendMessageAction.setConfigJson("{\"fields\": [{\"name\": \"channel\", \"label\": \"Channel Name or ID\", \"type\": \"text\", \"required\": true}, {\"name\": \"message_text\", \"label\": \"Message\", \"type\": \"textarea\", \"required\": true}, {\"name\": \"as_user\", \"label\": \"Send as Bot?\", \"type\": \"boolean\", \"default\": true}]}");

            Action postPrivateMessageAction = new Action();
            postPrivateMessageAction.setAppIntegration(slack);
            postPrivateMessageAction.setActionTypeIdentifier("post_private_message");
            postPrivateMessageAction.setName("Post Private Message");
            postPrivateMessageAction.setDescription("Sends a direct message to a user.");
            postPrivateMessageAction.setConfigJson("{\"fields\": [{\"name\": \"user_id\", \"label\": \"User ID or Email\", \"type\": \"text\", \"required\": true}, {\"name\": \"message_text\", \"label\": \"Message\", \"type\": \"textarea\", \"required\": true}]}");


            slack.setTriggers(Arrays.asList());
            slack.setActions(Arrays.asList(sendMessageAction, postPrivateMessageAction));
            appIntegrationRepository.save(slack);

            AppIntegration googleSheets = new AppIntegration();
            googleSheets.setName("Google Sheets");
            googleSheets.setIdentifier("google_sheets");
            googleSheets.setDescription("Create or update spreadsheet rows.");
            googleSheets.setLogoUrl("[https://placehold.co/40x40/0C9D58/FFFFFF?text=GS](https://placehold.co/40x40/0C9D58/FFFFFF?text=GS)");
            googleSheets.setAuthType("OAUTH2");

            Action addRowAction = new Action();
            addRowAction.setAppIntegration(googleSheets);
            addRowAction.setActionTypeIdentifier("add_row");
            addRowAction.setName("Add Row to Spreadsheet");
            addRowAction.setDescription("Adds a new row to a Google Sheet.");
            addRowAction.setConfigJson("{\"fields\": [{\"name\": \"spreadsheet_id\", \"label\": \"Spreadsheet ID\", \"type\": \"text\", \"required\": true}, {\"name\": \"sheet_name\", \"label\": \"Sheet Name\", \"type\": \"text\", \"required\": true}, {\"name\": \"row_data\", \"label\": \"Row Data (JSON)\", \"type\": \"textarea\", \"required\": true}]}");

            googleSheets.setTriggers(Arrays.asList());
            googleSheets.setActions(Arrays.asList(addRowAction));
            appIntegrationRepository.save(googleSheets);

            AppIntegration webhooks = new AppIntegration();
            webhooks.setName("Webhooks by AutomateX");
            webhooks.setIdentifier("webhooks");
            webhooks.setDescription("Receive or send webhooks.");
            webhooks.setLogoUrl("[https://placehold.co/40x40/FF7D00/FFFFFF?text=WH](https://placehold.co/40x40/FF7D00/FFFFFF?text=WH)");
            webhooks.setAuthType("NONE");

            Trigger catchHookTrigger = new Trigger();
            catchHookTrigger.setAppIntegration(webhooks);
            catchHookTrigger.setTriggerTypeIdentifier("catch_hook");
            catchHookTrigger.setName("Catch Hook");
            catchHookTrigger.setDescription("Triggers when a webhook is received.");
            catchHookTrigger.setConfigJson("{\"fields\": [{\"name\": \"webhook_url_suffix\", \"label\": \"Webhook URL Suffix\", \"type\": \"text\", \"required\": false, \"description\": \"Optional custom suffix for the webhook URL\"}]}");
            catchHookTrigger.setWebhookPath("my-test-workflow-hook-123");

            Action sendWebhookAction = new Action();
            sendWebhookAction.setAppIntegration(webhooks);
            sendWebhookAction.setActionTypeIdentifier("send_webhook");
            sendWebhookAction.setName("Send Webhook");
            sendWebhookAction.setDescription("Sends an outbound webhook (HTTP request).");
            sendWebhookAction.setConfigJson("{\"fields\": [{\"name\": \"url\", \"label\": \"URL\", \"type\": \"text\", \"required\": true}, {\"name\": \"method\", \"label\": \"Method\", \"type\": \"select\", \"options\": [\"POST\", \"GET\", \"PUT\", \"DELETE\"], \"required\": true, \"default\": \"POST\"}, {\"name\": \"headers\", \"label\": \"Headers (JSON)\", \"type\": \"textarea\", \"required\": false}, {\"name\": \"body\", \"label\": \"Body (JSON)\", \"type\": \"textarea\", \"required\": false}]}");

            webhooks.setTriggers(Arrays.asList(catchHookTrigger));
            webhooks.setActions(Arrays.asList(sendWebhookAction));
            appIntegrationRepository.save(webhooks);
        }
    }
}
