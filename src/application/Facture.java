package application;
import java.time.LocalDate;


public class Facture {
		
	private String codeFacture;
	private LocalDate dateFacture;
	private double montantPayer;
	public Facture() {
		super();
	}
	public Facture(String codeFacture, LocalDate dateFacture, double montantPayer) {
		super();
		this.codeFacture = codeFacture;
		this.dateFacture = dateFacture;
		this.montantPayer = montantPayer;
	}
	public String getCodeFacture() {
		return codeFacture;
	}
	public void setCodeFacture(String codeFacture) {
		this.codeFacture = codeFacture;
	}
	public LocalDate getDateFacture() {
		return dateFacture;
	}
	public void setDateFacture(LocalDate dateFacture) {
		this.dateFacture = dateFacture;
	}
	public double getMontantPayer() {
		return montantPayer;
	}
	public void setMontantPayer(double montantPayer) {
		this.montantPayer = montantPayer;
	}
	
	
}
