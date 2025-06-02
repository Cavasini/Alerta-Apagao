package br.com.alertaApagao;

import com.notificationapi.NotificationApi;
import com.notificationapi.model.NotificationRequest;
import com.notificationapi.model.SmsOptions;
import com.notificationapi.model.User;

import javax.jws.WebService;
import java.util.HashMap;
import java.util.Map;

@WebService(endpointInterface = "br.com.alertaApagao.AlertService")
public class AlertServiceImplementation implements AlertService{

    @Override
    public void sendSMS(String message, String phone) {
        NotificationApi api = new NotificationApi(
                "53mzty7qxvmp7n9iae6hhdk7rv",
                "ig6y7ois7n27sjlqk9sz4h4l5hqyh3r0auz1d0ithzyh1quoc8v2xfy31y"
        );

        // Create user
        User user = new User("matheus.lopeszzz71@gmail.com")
                .setNumber(phone); // Please replace our test number with a valid number to receive this message.;

        // Create merge tags
        Map<String, Object> mergeTags = new HashMap<>();
        mergeTags.put("comment", "OLE");

        // Create and send notification request
        NotificationRequest request = new NotificationRequest("newnotification", user)
                .setMergeTags(mergeTags)
                .setSms(new SmsOptions()
                        .setMessage(message)
                );

        System.out.println("Sending notification request...");
        String response = api.send(request);
        System.out.println("Response: " + response);
    }
}
