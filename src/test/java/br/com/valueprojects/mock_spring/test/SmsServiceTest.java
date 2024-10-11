package br.com.valueprojects.mock_spring.test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import br.com.valueprojects.mock_spring.model.Sms;
import br.com.valueprojects.mock_spring.model.Participante;
import br.com.valueprojects.mock_spring.service.SmsService;

public class SmsServiceTest {

    @Test
    public void deveEnviarSmsComSucesso() {
    	
        SmsService smsService = new SmsService();
        Participante vencedor = new Participante("João");

        Sms sms = new Sms(vencedor, "Parabéns, você venceu o jogo!");

        boolean resultado = smsService.enviar(sms);

        assertTrue(resultado);

    }
}
