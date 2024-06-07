package curso.api.rest.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Clientes {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String nome;
	private String cpf;	
	private String descricaoServico;
	private Boolean garantia;
	private String email;
	private String telefone;
	private String cep;
	private String datadacompra;
	private String numerodaos;
	private String itensenviadospelocliente;
	private String modelodamaquina;
	private String numeronfcompra;
	private String statusservico;
	
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getDescricaoServico() {
		return descricaoServico;
	}
	public void setDescricaoServico(String descricaoServico) {
		this.descricaoServico = descricaoServico;
	}
	public Boolean getGarantia() {
		return garantia;
	}
	public void setGarantia(Boolean garantia) {
		this.garantia = garantia;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getDatadacompra() {
		return datadacompra;
	}
	public void setDatadacompra(String datadacompra) {
		this.datadacompra = datadacompra;
	}
	public String getNumerodaos() {
		return numerodaos;
	}
	public void setNumerodaos(String numerodaos) {
		this.numerodaos = numerodaos;
	}
	public String getItensenviadospelocliente() {
		return itensenviadospelocliente;
	}
	public void setItensenviadospelocliente(String itensenviadospelocliente) {
		this.itensenviadospelocliente = itensenviadospelocliente;
	}
	public String getModelodamaquina() {
		return modelodamaquina;
	}
	public void setModelodamaquina(String modelodamaquina) {
		this.modelodamaquina = modelodamaquina;
	}
	public String getNumeronfcompra() {
		return numeronfcompra;
	}
	public void setNumeronfcompra(String numeronfcompra) {
		this.numeronfcompra = numeronfcompra;
	}
	public String getStatusservico() {
		return statusservico;
	}
	public void setStatusservico(String statusservico) {
		this.statusservico = statusservico;
	}
	
	

}
