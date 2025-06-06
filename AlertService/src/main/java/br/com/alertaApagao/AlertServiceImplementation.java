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
                "9lt3nmc9blhv4ma2f3paqn9dz7",
                "dac8hwlnc3nt52yzh9j18ibumnaugw4cm4hl4ufrtzi7j4zcxdajx0gja4"
        );

        // Create user
        User user = new User("mattheuscavasini@gmail.com")
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
