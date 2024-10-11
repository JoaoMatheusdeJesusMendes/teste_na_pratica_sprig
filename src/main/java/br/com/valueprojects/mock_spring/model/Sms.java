package br.com.valueprojects.mock_spring.model;

public class Sms{
	private String texto;
	private Participante vencedor;
	
	public Sms(Participante vencedor, String texto) {
		this.vencedor = vencedor;
		this.texto = texto;
	}
	
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public Participante getVencedor() {
		return vencedor;
	}
	public void setVencedor(Participante vencedor) {
		this.vencedor = vencedor;
	}
	public String getNumero() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getMensagem() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
