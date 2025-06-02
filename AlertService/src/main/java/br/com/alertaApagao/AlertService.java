package br.com.alertaApagao;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface AlertService {

    @WebMethod
    void sendSMS(String message, String phone);
}
