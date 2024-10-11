package br.com.valueprojects.mock_spring.service;

import br.com.valueprojects.mock_spring.model.Comunicacao;
import br.com.valueprojects.mock_spring.model.Participante;
import br.com.valueprojects.mock_spring.model.Sms;

public class SmsService implements Comunicacao {

    @Override
    public boolean enviar(Participante vencedor,String sms) {
    	boolean foiEnviado = false;
        // Simula o envio do SMS
        System.out.println(vencedor + sms);
        foiEnviado = true;
        return foiEnviado;
    }
}

