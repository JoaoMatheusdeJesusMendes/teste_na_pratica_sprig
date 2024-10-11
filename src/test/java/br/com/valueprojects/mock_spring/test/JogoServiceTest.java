package br.com.valueprojects.mock_spring.test;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.InOrder;

import br.com.valueprojects.mock_spring.model.*;
import br.com.valueprojects.mock_spring.service.JogoService;
import br.com.valueprojects.mock_spring.service.SmsService;
import infra.JogoDao;
import infra.VencedorDao;

public class JogoServiceTest {

    @Mock
    private VencedorDao vencedorDao;

    @Mock
    private JogoDao jogoDao;

    @Mock
    private SmsService smsService;

    @Mock
    private FinalizaJogo finalizaJogo;

    @Mock
    private Juiz juiz;

    @InjectMocks
    private JogoService jogoService;

    public JogoServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void deveProcessarJogosFinalizadosDaSemanaAnteriorComSucesso() {
        // Arrange
        Jogo jogo1 = mock(Jogo.class);
        Participante vencedor1 = mock(Participante.class);
        Resultado resultado = mock(Resultado.class);
        Sms sms = mock(Sms.class); // Mock do Sms

        // Configurando o mock para retornar um nome válido e uma mensagem válida
        when(vencedor1.getNome()).thenReturn("João");
        when(sms.getTexto()).thenReturn("Parabéns, você venceu o jogo!"); // Simulando o retorno da mensagem
        when(sms.getVencedor()).thenReturn(vencedor1); // Associando o vencedor ao SMS

        // Simulando o comportamento esperado
        when(jogo1.isFinalizado()).thenReturn(true);
        when(finalizaJogo.iniciouSemanaAnterior(jogo1)).thenReturn(true);
        when(juiz.getPrimeiroColocado()).thenReturn(10.0);
        doNothing().when(juiz).julga(any(Jogo.class));

        List<Resultado> resultados = Arrays.asList(resultado);
        when(jogo1.getResultados()).thenReturn(resultados);
        when(resultado.getMetrica()).thenReturn(10.0);
        when(resultado.getParticipante()).thenReturn(vencedor1);

        List<Jogo> jogos = Arrays.asList(jogo1);

        // Act
        jogoService.processarJogos(jogos);

        // Assert
        // Verifica se o vencedor foi salvo
        verify(vencedorDao).salvar(vencedor1);
        // Verifica se o jogo foi salvo
        verify(jogoDao).salva(jogo1);
        // Verifica se o SMS foi enviado após salvar o vencedor e o jogo
        verify(smsService).enviar(argThat(s -> 
            s.getVencedor().getNome().equals(vencedor1.getNome()) &&
            s.getTexto().equals("Parabéns, você venceu o jogo!")
        ));

        // Verificando ordem das interações
        InOrder inOrder = inOrder(vencedorDao, jogoDao, smsService);
        // Certificando que o vencedor foi salvo primeiro, depois o jogo e por último o SMS
        inOrder.verify(vencedorDao).salvar(vencedor1);
        inOrder.verify(jogoDao).salva(jogo1);
        inOrder.verify(smsService).enviar(any(Sms.class));
    }


    @Test
    public void naoDeveProcessarJogosNaoFinalizados() {
        // Arrange
        Jogo jogo1 = mock(Jogo.class);
        
        // Simulando que o jogo não foi finalizado
        when(jogo1.isFinalizado()).thenReturn(false);

        List<Jogo> jogos = Arrays.asList(jogo1);

        // Act
        jogoService.processarJogos(jogos);

        // Assert
        // Certifica que não houve interações com os DAOs e o SMS Service
        verifyNoInteractions(vencedorDao, jogoDao, smsService);
    }

    @Test
    public void naoDeveProcessarJogosNaoIniciadosSemanaAnterior() {
        // Arrange
        Jogo jogo1 = mock(Jogo.class);
        
        // Simulando que o jogo foi finalizado, mas não iniciou na semana anterior
        when(jogo1.isFinalizado()).thenReturn(true);
        when(finalizaJogo.iniciouSemanaAnterior(jogo1)).thenReturn(false);

        List<Jogo> jogos = Arrays.asList(jogo1);

        // Act
        jogoService.processarJogos(jogos);

        // Assert
        // Certifica que não houve interações com os DAOs e o SMS Service
        verifyNoInteractions(vencedorDao, jogoDao, smsService);
    }
}

