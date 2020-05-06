package application;
import java.time.LocalDate;

public class Contrat {

	private String codeContrat;
	private LocalDate dateContrat;
	private LocalDate dateEcheance;
	public Contrat() {
		super();
	}
	public Contrat(String codeContrat, LocalDate dateContrat, LocalDate dateEcheance) {
		super();
		this.codeContrat = codeContrat;
		this.dateContrat = dateContrat;
		this.dateEcheance = dateEcheance;
	}
	public String getCodeContrat() {
		return codeContrat;
	}
	public void setCodeContrat(String codeContrat) {
		this.codeContrat = codeContrat;
	}
	public LocalDate getDateContrat() {
		return dateContrat;
	}
	public void setDateContrat(LocalDate dateContrat) {
		this.dateContrat = dateContrat;
	}
	public LocalDate getDateEcheance() {
		return dateEcheance;
	}
	public void setDateEcheance(LocalDate dateEcheance) {
		this.dateEcheance = dateEcheance;
	}
	
	
}
