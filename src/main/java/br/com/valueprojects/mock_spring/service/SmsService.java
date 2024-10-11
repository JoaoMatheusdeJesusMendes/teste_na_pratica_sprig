package br.com.valueprojects.mock_spring.service;

import br.com.valueprojects.mock_spring.model.Comunicacao;
import br.com.valueprojects.mock_spring.model.Participante;
import br.com.valueprojects.mock_spring.model.Sms;

public class SmsService implements Comunicacao {

    @Override
    public boolean enviar(Sms sms) {
    	boolean foiEnviado = false;
        System.out.println(sms.getVencedor() + sms.getTexto());
        foiEnviado = true;
        return foiEnviado;
    }
}

