package br.com.valueprojects.mock_spring.controller;

import infra.VencedorDao;
import br.com.valueprojects.mock_spring.model.Participante;
import br.com.valueprojects.mock_spring.model.Sms;
import br.com.valueprojects.mock_spring.service.SmsService;

public class SmsController {

    private final SmsService smsService;
    private final VencedorDao vencedorDao;

    public SmsController(SmsService smsService, VencedorDao vencedorDao) {
        this.smsService = smsService;
        this.vencedorDao = vencedorDao;
    }

    public void processarVencedor(Participante vencedor) {
        // Salva o vencedor
        vencedorDao.salvar(vencedor);
        // Cria o SMS
        Sms sms = new Sms(vencedor, "Parabéns, você venceu!");
        // Envia o SMS
        boolean enviou = smsService.enviar(sms);
    }
}
