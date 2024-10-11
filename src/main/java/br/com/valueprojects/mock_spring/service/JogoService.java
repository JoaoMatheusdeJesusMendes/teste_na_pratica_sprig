package br.com.valueprojects.mock_spring.service;

import br.com.valueprojects.mock_spring.model.FinalizaJogo;
import br.com.valueprojects.mock_spring.model.Jogo;
import infra.JogoDao;
import infra.VencedorDao;
import br.com.valueprojects.mock_spring.model.Sms;
import br.com.valueprojects.mock_spring.model.Juiz;
import br.com.valueprojects.mock_spring.model.Participante;
import br.com.valueprojects.mock_spring.model.Resultado;

import java.util.List;

public class JogoService {
    private VencedorDao vencedorDao;
    private SmsService smsService;
    private JogoDao jogoDao;
    private FinalizaJogo finalizaJogo;
    private Juiz juiz;

    public JogoService(VencedorDao vencedorDao, SmsService smsService, JogoDao jogoDao, FinalizaJogo finalizaJogo, Juiz juiz) {
        this.vencedorDao = vencedorDao;
        this.smsService = smsService;
        this.jogoDao = jogoDao;
        this.finalizaJogo = finalizaJogo;
        this.juiz = juiz;
    }


    public void processarJogos(List<Jogo> jogos) {
        for (Jogo jogo : jogos) {
            // Supondo que Jogo tem um método isFinalizado()
            if (jogo.isFinalizado() && finalizaJogo.iniciouSemanaAnterior(jogo)) {
                // Identificar o vencedor
            	Participante vencedor = vencedor(jogo);             
                // Salvar o jogo e o vencedor
                vencedorDao.salvar(vencedor);
                jogoDao.salva(jogo);
                // Enviar SMS para o vencedor
                smsService.enviar(new Sms(vencedor, "Parabéns, você venceu o jogo!"));
            }
        }
    }
    
    public Participante vencedor(Jogo jogo) {
        Participante vencedor = null;
        juiz.julga(jogo);
        double pontosvencedor = juiz.getPrimeiroColocado();
        for (Resultado resultado: jogo.getResultados()) {
            if (resultado.getMetrica() == pontosvencedor) {
                vencedor = resultado.getParticipante();
            }
        }
        return vencedor;
    }

}
