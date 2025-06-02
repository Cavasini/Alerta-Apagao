package com.alertaApagao.monitoringService;

import com.alertaApagao.monitoringService.service.MonitoringService;
import com.alertaApagao.monitoringService.stub_classes.AlertService;
import com.alertaApagao.monitoringService.stub_classes.AlertServiceImplementationService;
import jakarta.xml.ws.Service;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URL;

@SpringBootApplication
@EnableScheduling
public class MonitoringServiceApplication {

	public static void main(String[] args) throws MalformedURLException {
		SpringApplication.run(MonitoringServiceApplication.class, args);
	}

}
