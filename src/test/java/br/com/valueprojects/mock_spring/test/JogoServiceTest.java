package br.com.valueprojects.mock_spring.test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.valueprojects.mock_spring.model.FinalizaJogo;
import br.com.valueprojects.mock_spring.model.Jogo;
import br.com.valueprojects.mock_spring.model.Participante;
import br.com.valueprojects.mock_spring.model.Sms;
import br.com.valueprojects.mock_spring.service.JogoService;
import br.com.valueprojects.mock_spring.service.SmsService;
import infra.JogoDao;
import infra.VencedorDao;

import java.util.Arrays;
import java.util.List;

public class JogoServiceTest {

    @Mock
    private VencedorDao vencedorDao;

    @Mock
    private JogoDao jogoDao;

    @Mock
    private SmsService smsService;

    @Mock
    private FinalizaJogo finalizaJogo;

    @InjectMocks
    private JogoService jogoService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcessarJogos_jogoFinalizadoDaSemanaAnterior_sucesso() {
        // Arrange
        Jogo jogo1 = mock(Jogo.class);
        Participante vencedor1 = mock(Participante.class);

        // Simulando que o jogo foi finalizado e iniciou na semana anterior
        when(jogo1.isFinalizado()).thenReturn(true);
        when(finalizaJogo.iniciouSemanaAnterior(jogo1)).thenReturn(true);
        when(jogoService.vencedor(jogo1)).thenReturn(vencedor1);

        List<Jogo> jogos = Arrays.asList(jogo1);

        // Act
        jogoService.processarJogos(jogos);

        // Assert
        // Verificando que o vencedor foi salvo antes do envio do SMS
        verify(vencedorDao).salvar(vencedor1);
        verify(jogoDao).salva(jogo1);
        verify(smsService).enviar(new Sms(vencedor1, "Parabéns, você venceu o jogo!"));

        // Garantindo que o SMS não seja enviado antes dos dados serem salvos
        InOrder inOrder = inOrder(vencedorDao, jogoDao, smsService);
        inOrder.verify(vencedorDao).salvar(vencedor1);
        inOrder.verify(jogoDao).salva(jogo1);
        inOrder.verify(smsService).enviar(new Sms(vencedor1, "Parabéns, você venceu o jogo!"));
    }

    @Test
    public void testProcessarJogos_jogoNaoFinalizado_semInteracaoComDaoOuSms() {
        // Arrange
        Jogo jogo1 = mock(Jogo.class);
        
        // Simulando que o jogo não foi finalizado
        when(jogo1.isFinalizado()).thenReturn(false);

        List<Jogo> jogos = Arrays.asList(jogo1);

        // Act
        jogoService.processarJogos(jogos);

        // Assert
        // Verificando que nenhum DAO ou serviço SMS foi chamado
        verifyNoInteractions(vencedorDao, jogoDao, smsService);
    }

    @Test
    public void testProcessarJogos_jogoNaoIniciouSemanaAnterior_semInteracaoComDaoOuSms() {
        // Arrange
        Jogo jogo1 = mock(Jogo.class);
        
        // Simulando que o jogo foi finalizado, mas não iniciou na semana anterior
        when(jogo1.isFinalizado()).thenReturn(true);
        when(finalizaJogo.iniciouSemanaAnterior(jogo1)).thenReturn(false);

        List<Jogo> jogos = Arrays.asList(jogo1);

        // Act
        jogoService.processarJogos(jogos);

        // Assert
        // Verificando que nenhum DAO ou serviço SMS foi chamado
        verifyNoInteractions(vencedorDao, jogoDao, smsService);
    }
}
