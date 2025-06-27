package com.pixels.zapierClone.automation_platform.config;

import com.pixels.zapierClone.automation_platform.entity.ActionDefinition;
import com.pixels.zapierClone.automation_platform.entity.AppIntegration;
import com.pixels.zapierClone.automation_platform.entity.TriggerDefinition;
import com.pixels.zapierClone.automation_platform.repository.ActionDefinitionRepository;
import com.pixels.zapierClone.automation_platform.repository.AppIntegrationRepository;
import com.pixels.zapierClone.automation_platform.repository.TriggerDefinitionRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final AppIntegrationRepository appIntegrationRepository;
    private final TriggerDefinitionRepository triggerDefinitionRepository;
    private final ActionDefinitionRepository actionDefinitionRepository;

    public DataSeeder(AppIntegrationRepository appIntegrationRepository,
                      TriggerDefinitionRepository triggerDefinitionRepository,
                      ActionDefinitionRepository actionDefinitionRepository) {
        this.appIntegrationRepository = appIntegrationRepository;
        this.triggerDefinitionRepository = triggerDefinitionRepository;
        this.actionDefinitionRepository = actionDefinitionRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (appIntegrationRepository.count() > 0) return;

        // ------------ Gmail ------------
        AppIntegration gmail = new AppIntegration();
        gmail.setName("Gmail");
        gmail.setIdentifier("gmail");
        gmail.setDescription("Send and receive emails.");
        gmail.setLogoUrl("https://placehold.co/40x40/FF0000/FFFFFF?text=G");
        gmail.setAuthType("OAUTH2");
        appIntegrationRepository.save(gmail);

        ActionDefinition sendEmail = new ActionDefinition();
        sendEmail.setAppIntegration(gmail);
        sendEmail.setActionTypeIdentifier("send_email");
        sendEmail.setName("Send Email");
        sendEmail.setDescription("Sends an email via Gmail.");
        sendEmail.setConfigJson("""
            {
              "fields": [
                {"name": "to", "label": "Recipient Email", "type": "text", "required": true},
                {"name": "subject", "label": "Subject", "type": "text", "required": true},
                {"name": "body", "label": "Email Body", "type": "textarea", "required": true}
              ]
            }
        """);
        actionDefinitionRepository.save(sendEmail);

        // ------------ Google Sheets ------------
        AppIntegration gform = new AppIntegration();
        gform.setName("Google Sheets");
        gform.setIdentifier("google_sheets");
        gform.setDescription("Track form responses via connected Google Sheet.");
        gform.setLogoUrl("https://placehold.co/40x40/0C9D58/FFFFFF?text=GS");
        gform.setAuthType("OAUTH2");
        appIntegrationRepository.save(gform);

        TriggerDefinition newGFormResponse = new TriggerDefinition();
        newGFormResponse.setAppIntegration(gform);
        newGFormResponse.setTriggerTypeIdentifier("new_form_response");
        newGFormResponse.setName("New Form Response");
        newGFormResponse.setDescription("Triggered when a new response is added to a Google Form (via linked Sheet).");
        newGFormResponse.setConfigJson("""
            {
              "fields": [
                {"name": "spreadsheet_id", "label": "Spreadsheet ID", "type": "text", "required": true},
                {"name": "sheet_name", "label": "Sheet Name", "type": "text", "required": true}
              ]
            }
        """);
        triggerDefinitionRepository.save(newGFormResponse);

        // ------------ Google Forms ------------
      AppIntegration gformApp = new AppIntegration();
      gformApp.setName("Google Forms");
      gformApp.setIdentifier("google_forms");
      gformApp.setDescription("Trigger workflows from Google Form submissions (via linked Sheet).");
      gformApp.setLogoUrl("https://placehold.co/40x40/673AB7/FFFFFF?text=GF");
      gformApp.setAuthType("OAUTH2");
      appIntegrationRepository.save(gformApp);

      TriggerDefinition newFormResponse = new TriggerDefinition();
      newFormResponse.setAppIntegration(gformApp);
      newFormResponse.setTriggerTypeIdentifier("new_form_submission");
      newFormResponse.setName("New Form Submission");
      newFormResponse.setDescription("Triggered when a new Google Form response is submitted.");
      newFormResponse.setConfigJson("""
          {
            "fields": [
              {"name": "spreadsheet_id", "label": "Spreadsheet ID", "type": "text", "required": true},
              {"name": "sheet_name", "label": "Sheet Name", "type": "text", "required": true}
            ]
          }
      """);
      triggerDefinitionRepository.save(newFormResponse);


        // ------------ Slack ------------
        AppIntegration slack = new AppIntegration();
        slack.setName("Slack");
        slack.setIdentifier("slack");
        slack.setDescription("Send messages to Slack users or channels.");
        slack.setLogoUrl("https://placehold.co/40x40/4A154B/FFFFFF?text=S");
        slack.setAuthType("OAUTH2");
        appIntegrationRepository.save(slack);

        ActionDefinition slackMsg = new ActionDefinition();
        slackMsg.setAppIntegration(slack);
        slackMsg.setActionTypeIdentifier("send_slack_message");
        slackMsg.setName("Send Slack Message");
        slackMsg.setDescription("Sends a message to a Slack channel or user.");
        slackMsg.setConfigJson("""
            {
              "fields": [
                {"name": "channel", "label": "Channel ID", "type": "text", "required": true},
                {"name": "message", "label": "Message Text", "type": "textarea", "required": true}
              ]
            }
        """);
        actionDefinitionRepository.save(slackMsg);
    }
}
